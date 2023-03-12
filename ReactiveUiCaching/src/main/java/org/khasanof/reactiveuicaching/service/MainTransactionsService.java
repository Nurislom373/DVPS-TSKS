package org.khasanof.reactiveuicaching.service;

import lombok.extern.slf4j.Slf4j;
import org.khasanof.reactiveuicaching.domain.TransactionEntity;
import org.khasanof.reactiveuicaching.enums.FromToEnum;
import org.khasanof.reactiveuicaching.repository.TransactionRepository;
import org.khasanof.reactiveuicaching.service.context.ContextTransactionService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Author: Nurislom
 * <br/>
 * Date: 3/7/2023
 * <br/>
 * Time: 3:48 PM
 * <br/>
 * Package: org.khasanof.uicachingstrategy.service
 */
@Slf4j
@Service
public class MainTransactionsService {

    private final ContextTransactionService contextTransactionService;
    private final TransactionRepository transactionRepository;

    public MainTransactionsService(@Qualifier("springMethodContextTransactionService") ContextTransactionService contextTransactionService, TransactionRepository transactionRepository) {
        this.contextTransactionService = contextTransactionService;
        this.transactionRepository = transactionRepository;
    }

    /**
     * @param cardNumber You must enter a 16-digit card number
     * @param from       You must enter the interval from how many to how many
     * @param to         You must enter the interval to how many to how many
     * @return Returns a list of transactions returned from the getAllTransactionsByService method
     */
    public Mono<List<TransactionEntity>> getAllTransactionsByCardAndDates(String cardNumber, LocalDateTime from, LocalDateTime to) {
        checkParametersGetAllTransactionsByCardAndDates(cardNumber, from, to);
        return getAllTransactionsByService(contextTransactionService.getService(cardNumber), cardNumber, from, to);
    }

    public Mono<Map<String, List<TransactionEntity>>> getAllTransactionsByCardsAndDates(List<String> cards, LocalDateTime from, LocalDateTime to) {
        checkParametersGetAllTransactionsByCardsAndDates(cards, from, to);
        return Mono.just(cards.stream().map(c -> new AbstractMap.SimpleEntry<>(c,
                        getAllTransactionsByService(contextTransactionService.getService(c), c, from, to)
                                .block()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)));
    }

    private Mono<List<TransactionEntity>> getAllTransactionsByService(TransactionService service, String cardNumber,
                                                                      LocalDateTime from, LocalDateTime to) {
        boolean isComposite = cardNumber.equals("*");
        return transactionRepository.count()
                .publishOn(Schedulers.boundedElastic())
                .flatMap((c) -> {
                    if (c == 0) {
                        return getTransactionsExternalServiceWhenCacheIsEmpty(service, cardNumber, from, to);
                    } else if (!isComposite && transactionRepository.getCardCacheCount(cardNumber)
                            .blockOptional().orElse(0L) == 0) {
                        return getTransactionsExternalServiceWhenCacheIsEmpty(service, cardNumber, from, to);
                    } else {
                        return getListMono(service, cardNumber, from, to, isComposite);
                    }
                });
    }

    private Mono<List<TransactionEntity>> getListMono(TransactionService service, String cardNumber, LocalDateTime from,
                                                      LocalDateTime to, boolean isComposite) {
        return getFromToDateRepository(cardNumber, isComposite)
                .flatMap(timeMap -> {
                    int compareFrom = timeMap.get(FromToEnum.FROM).compareTo(from);
                    int compareTo = timeMap.get(FromToEnum.TO).compareTo(to);

                    boolean isCache = (timeMap.get(FromToEnum.FROM).isBefore(from) && timeMap.get(FromToEnum.TO)
                            .isAfter(from)) || (timeMap.get(FromToEnum.FROM).isBefore(to)
                            && timeMap.get(FromToEnum.TO).isAfter(to));

                    boolean isBetweenCache = (timeMap.get(FromToEnum.FROM).isAfter(from) &&
                            timeMap.get(FromToEnum.TO).isBefore(to));

                    return getTransactionCacheAndExternal(service, cardNumber, from, to, isComposite, timeMap,
                            compareFrom, compareTo, isCache, isBetweenCache);
                });
    }

    private Mono<List<TransactionEntity>> getTransactionCacheAndExternal(TransactionService service, String cardNumber, LocalDateTime from, LocalDateTime to, boolean isComposite, Map<FromToEnum, LocalDateTime> timeMap, int compareFrom, int compareTo, boolean isCache, boolean isBetweenCache) {
        if (compareFrom <= 0 && compareTo >= 0) {

            return getTransactionsOnlyCache(cardNumber, isComposite, from, to);

        } else if ((!isCache && !isBetweenCache)) {

            return getTransactionOnlyExternalServiceWhenCacheRangeGreaterThan(service, cardNumber, isComposite, from, to,
                    timeMap, compareFrom);

        } else if (compareFrom <= 0) {

            return getTransactionWhenCacheRangeToGreaterThanRequestTo(service, cardNumber, isComposite, from, to, timeMap);

        } else if (compareTo >= 0) {

            return getTransactionsWhenCacheRangeFromGreaterThanRequestFrom(service, cardNumber, isComposite, from, to, timeMap);

        } else {

            return getTransactionsWhenCacheBetweenFromAndToDates(service, cardNumber, isComposite, from, to, timeMap);
        }
    }

    private Mono<List<TransactionEntity>> getTransactionsWhenCacheBetweenFromAndToDates(TransactionService service, String cardNumber, boolean isComposite, LocalDateTime from, LocalDateTime to, Map<FromToEnum, LocalDateTime> timeMap) {
        return Mono.just(isComposite)
                .flatMap((var) -> {
                    if (isComposite)
                        return transactionRepository.findAllByCreatedAtIsBetween(timeMap.get(FromToEnum.FROM),
                                timeMap.get(FromToEnum.TO)).collectList();
                    return transactionRepository.findAllByCreatedAtIsBetween(
                                    cardNumber, timeMap.get(FromToEnum.FROM), timeMap.get(FromToEnum.TO))
                            .collectList();
                }).flatMap((var) -> Flux.concat(service.getAllTransactionsByDates(cardNumber,
                                        from, timeMap.get(FromToEnum.FROM)),
                                service.getAllTransactionsByDates(cardNumber, timeMap.get(FromToEnum.TO), to))
                        .publishOn(Schedulers.boundedElastic())
                        .mapNotNull(o -> {
                            Mono.just(o).subscribe(transactionRepository::save);
                            return o;
                        })
                        .collectList()
                        .map(list -> {
                            list.addAll(var);
                            return list;
                        }));
    }

    private Mono<List<TransactionEntity>> getTransactionWhenCacheRangeToGreaterThanRequestTo(TransactionService service, String cardNumber, boolean isComposite, LocalDateTime from, LocalDateTime to, Map<FromToEnum, LocalDateTime> timeMap) {
        return Mono.just(isComposite)
                .flatMap(any -> {
                    if (any) return transactionRepository.findAllByCreatedAtIsBetween(from, to)
                            .collectList();
                    return transactionRepository.findAllByCreatedAtIsBetween(cardNumber,
                            from, timeMap.get(FromToEnum.TO)).collectList();
                }).flatMap((var) -> service.getAllTransactionsByDates(cardNumber, from, to)
                        .collectList()
                        .publishOn(Schedulers.boundedElastic())
                        .mapNotNull(list -> {
                            if (list != null && !list.isEmpty()) {
                                Mono.just(list).subscribe(transactionRepository::saveAll);
                                list.addAll(var);
                            }
                            return list;
                        }).switchIfEmpty(Mono.just(var))
                );
    }

    private Mono<List<TransactionEntity>> getTransactionsWhenCacheRangeFromGreaterThanRequestFrom(TransactionService service, String cardNumber, boolean isComposite, LocalDateTime from, LocalDateTime to, Map<FromToEnum, LocalDateTime> timeMap) {
        return Mono.just(isComposite)
                .flatMap(any -> {
                    if (isComposite) return transactionRepository.findAllByCreatedAtIsBetween(from, to).collectList();
                    return transactionRepository.findAllByCreatedAtIsBetween(cardNumber,
                            timeMap.get(FromToEnum.FROM), to).collectList();
                }).flatMap((var) -> service.getAllTransactionsByDates(cardNumber, from, to)
                        .collectList()
                        .publishOn(Schedulers.boundedElastic())
                        .mapNotNull(list -> {
                            if (var != null && !var.isEmpty()) {
                                Mono.just(var).subscribe(transactionRepository::saveAll);
                                list.addAll(var);
                            }
                            return list;
                        }).switchIfEmpty(Mono.just(var))
                );
    }

    private Mono<List<TransactionEntity>> getTransactionOnlyExternalServiceWhenCacheRangeGreaterThan(TransactionService service, String cardNumber, boolean isComposite, LocalDateTime from, LocalDateTime to, Map<FromToEnum, LocalDateTime> timeMap, int compareFrom) {
        return Mono.just(compareFrom)
                .flatMap((var1) -> {
                    if (var1 < 0) return service.getAllTransactionsByDates(cardNumber,
                                    timeMap.get(FromToEnum.TO), to)
                            .collectList();
                    return service.getAllTransactionsByDates(cardNumber, from, timeMap.get(FromToEnum.FROM))
                            .collectList();
                })
                .publishOn(Schedulers.boundedElastic()).doOnNext((var2) -> {
                    if (var2 != null && !var2.isEmpty())
                        Mono.just(var2).subscribe(transactionRepository::saveAll);
                }).flatMap((var) -> {
                    if (isComposite) return transactionRepository.findAllByCreatedAtIsBetween(from, to)
                            .collectList();
                    return transactionRepository.findAllByCreatedAtIsBetween(cardNumber, from, to)
                            .collectList();
                }).switchIfEmpty(Mono.empty());
    }

    private Mono<List<TransactionEntity>> getTransactionsOnlyCache(String cardNumber, boolean isComposite, LocalDateTime from, LocalDateTime to) {
        if (isComposite) {
            return transactionRepository.findAllByCreatedAtIsBetween(from, to)
                    .sort(Comparator.comparing(TransactionEntity::getId))
                    .collectList();
        }
        return transactionRepository.findAllByCreatedAtIsBetween(cardNumber, from, to)
                .sort(Comparator.comparing(TransactionEntity::getId))
                .collectList();
    }

    private Mono<List<TransactionEntity>> getTransactionsExternalServiceWhenCacheIsEmpty(TransactionService service, String cardNumber, LocalDateTime from, LocalDateTime to) {
        return service.getAllTransactionsByDates(cardNumber, from, to)
                .collectList()
                .flatMap(list -> {
                    if (list != null && !list.isEmpty()) {
                        Mono.just(list).subscribe(transactionRepository::saveAll);
                        return Mono.just(list);
                    }
                    return Mono.empty();
                });
    }

    private Mono<Map<FromToEnum, LocalDateTime>> getFromToDateRepository(String cardNumber, boolean isComposite) {
        return Mono.just(isComposite).map(any -> {
            if (isComposite) {
                return transactionRepository.findAll();
            } else {
                return transactionRepository.findAll()
                        .filter(t -> t.getToCard().equals(cardNumber) || t.getFromCard().equals(cardNumber));
            }
        }).map(Flux::toStream).flatMap(o -> {
            List<TransactionEntity> list = o.toList();
            return Mono.just(new HashMap<>() {{
                put(FromToEnum.FROM, list.get(0).getCreatedAt());
                put(FromToEnum.TO, list.get(list.size() - 1).getCreatedAt());
            }});
        });
    }

    private void checkParametersGetAllTransactionsByCardAndDates(String cardNumber, LocalDateTime from, LocalDateTime to) {
        if (Objects.isNull(from) || Objects.isNull(to)) {
            throw new RuntimeException("Invalid Date!");
        }
        if (from.isEqual(to)) {
            throw new RuntimeException("From Date is Equal To Date!");
        }
        if (from.isAfter(to)) {
            throw new RuntimeException("From Date is Greater Than To Date!");
        }
        if (cardNumber.length() != 16 && cardNumber.length() > 1) {
            throw new RuntimeException("Invalid CardNUmber!");
        }
    }

    private void checkParametersGetAllTransactionsByCardsAndDates(List<String> cards, LocalDateTime from, LocalDateTime to) {
        if (Objects.isNull(from) || Objects.isNull(to)) {
            throw new RuntimeException("Invalid Date!");
        }
        if (from.isEqual(to)) {
            throw new RuntimeException("From Date is Equal To Date!");
        }
        if (from.isAfter(to)) {
            throw new RuntimeException("From Date is Greater Than To Date!");
        }
        boolean anyMatch = cards.stream()
                .anyMatch(any -> any.length() != 16);
        if (anyMatch) {
            throw new RuntimeException("Invalid CardNumber!");
        }
    }

}
