package org.khasanof.service;

import org.khasanof.domain.transaction.Transaction;
import org.khasanof.dto.transaction.TransactionCardGetDTO;
import org.khasanof.dto.transaction.TransactionMultiCardGetDTO;
import org.khasanof.enums.FromToEnum;
import org.khasanof.repository.TransactionRepository;
import org.khasanof.service.context.ContextTransactionService;
import org.khasanof.service.transactionServices.TransactionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.LocalDateTime;
import java.util.*;

/**
 * Author: Nurislom
 * <br/>
 * Date: 3/7/2023
 * <br/>
 * Time: 3:48 PM
 * <br/>
 * Package: org.khasanof.uicachingstrategy.service
 */
@Service
public class SpringMainTransactionsService implements MainTransactionService {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final ContextTransactionService contextTransactionService;

    private final TransactionRepository transactionRepository;

    public SpringMainTransactionsService(@Qualifier("annotationContextTransactionService") ContextTransactionService contextTransactionService, TransactionRepository transactionRepository) {
        this.contextTransactionService = contextTransactionService;
        this.transactionRepository = transactionRepository;
    }

    /**
     * This method is used to get all transactions in the given interval.
     *
     * @param dto Enter {@link TransactionCardGetDTO}
     * @return Returns a list of transactions returned from the getAllTransactionsByService method
     */
    @Override
    public Mono<List<Transaction>> getAllTransactionsByCardAndDates(TransactionCardGetDTO dto) {
        checkParametersGetAllTransactionsByCardAndDates(dto.getCardNumber(), dto.getFrom(), dto.getTo());
        if (dto.getCardNumber().equals("*")) {
            return Flux.fromIterable(contextTransactionService.getServices().entrySet())
                    .flatMap(e -> getAllTransactionsByService(e.getValue(), e.getKey(), dto.getFrom(), dto.getTo()))
                    .reduce(new ArrayList<>(), (list1, list2) -> {
                        list1.addAll(list2);
                        return list1;
                    });
        }
        return getAllTransactionsByService(contextTransactionService.getService(dto.getCardNumber()),
            dto.getCardNumber(), dto.getFrom(), dto.getTo());
    }

    /**
     * This method is similar to the above method, the only difference is that we can get all transactions
     * between several card numbers.
     *
     * @param dto Enter {@link TransactionMultiCardGetDTO}
     * @return returns a list of transactions returned from multiple getAllTransactionsByService methods
     */
    @Override
    public Mono<Map<String, List<Transaction>>> getAllTransactionsByCardsAndDates(TransactionMultiCardGetDTO dto) {
        checkParametersGetAllTransactionsByCardsAndDates(dto.getCards(), dto.getFrom(), dto.getTo());
        return Flux.fromIterable(dto.getCards())
                .flatMap(c -> getAllTransactionsByService(contextTransactionService.getService(c), c,
                    dto.getFrom(), dto.getTo())
                        .flatMap(list -> Mono.just(new AbstractMap.SimpleEntry<>(c, list))))
                .collectMap(Map.Entry::getKey, Map.Entry::getValue);
    }

    private Mono<List<Transaction>> getAllTransactionsByService(TransactionService service, String cardNumber,
                                                                LocalDateTime from, LocalDateTime to) {
        boolean isComposite = cardNumber.length() == 4;
        return transactionRepository.count()
                .log("Enter getAllTransactionsByService() Method")
                .flatMap((c) -> {
                    if (c == 0) {
                        log.info("Get External Service");
                        return getTransactionsExternalServiceWhenCacheIsEmpty(service, cardNumber, from, to);
                    }
                    return Mono.just(isComposite)
                            .log("Enter isComposite")
                            .flatMap(var -> {
                                if (isComposite) return transactionRepository.getCardCacheCountStart(
                                        concat(cardNumber, "%"));
                                return transactionRepository.getCardCacheCount(cardNumber);
                            }).flatMap((o) -> {
                                System.out.println("o = " + o);
                                if (o == 0) {
                                    return getTransactionsExternalServiceWhenCacheIsEmpty(service, cardNumber, from, to);
                                }
                                return getListMono(service, cardNumber, from, to, isComposite);
                            });
                });
    }

    private Mono<List<Transaction>> getListMono(TransactionService service, String cardNumber, LocalDateTime from,
                                                      LocalDateTime to, boolean isComposite) {
        log.info("Enter getListMono() Method");
        return getFromToDateRepository(cardNumber, isComposite)
                .flatMap(timeMap -> {
                    System.out.println("timeMap = " + timeMap);
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

    private Mono<List<Transaction>> getTransactionCacheAndExternal(TransactionService service, String cardNumber, LocalDateTime from, LocalDateTime to, boolean isComposite, Map<FromToEnum, LocalDateTime> timeMap, int compareFrom, int compareTo, boolean isCache, boolean isBetweenCache) {
        if (compareFrom <= 0 && compareTo >= 0) {
            // Fixed
            return getTransactionsOnlyCache(cardNumber, isComposite, from, to);
        } else if ((!isCache && !isBetweenCache)) {
            // Fixed
            return getTransactionOnlyExternalServiceWhenCacheRangeGreaterThan(service, cardNumber, isComposite, from, to,
                    timeMap, compareFrom);
        } else if (compareFrom <= 0) {
            // Fixed
            return getTransactionWhenCacheRangeToGreaterThanRequestTo(service, cardNumber, isComposite, from, to, timeMap);
        } else if (compareTo >= 0) {
            // Fixed
            return getTransactionsWhenCacheRangeFromGreaterThanRequestFrom(service, cardNumber, isComposite, from, to, timeMap);
        } else {
            // Fixed
            return getTransactionsWhenCacheBetweenFromAndToDates(service, cardNumber, isComposite, from, to, timeMap);
        }
    }

    private Mono<List<Transaction>> getTransactionsWhenCacheBetweenFromAndToDates(TransactionService service, String cardNumber, boolean isComposite, LocalDateTime from, LocalDateTime to, Map<FromToEnum, LocalDateTime> timeMap) {
        return Mono.just(isComposite)
                .log("getTransactionsWhenCacheBetweenFromAndToDates()")
                .flatMap((var) -> {
                    if (isComposite)
                        return transactionRepository.findAllByStartQuery(concat(cardNumber, "%"),
                                timeMap.get(FromToEnum.FROM), timeMap.get(FromToEnum.TO)).collectList();
                    return transactionRepository.findAllByQuery(
                                    cardNumber, timeMap.get(FromToEnum.FROM), timeMap.get(FromToEnum.TO))
                            .collectList();
                })
                .doOnNext((list) -> log.info("Cache Transaction Count : " + list.size()))
                .flatMap((var) -> service.getAllTransactionsByDates(cardNumber, from, timeMap.get(FromToEnum.FROM))
                        .collectList()
                        .zipWith(service.getAllTransactionsByDates(cardNumber, timeMap.get(FromToEnum.TO), to)
                                .collectList(), (one, two) -> {
                            one.addAll(two);
                            return one;
                        }).doOnNext((list) -> log.info("Two Service List Size : " + list.size()))
                        .onErrorReturn(new ArrayList<>())
                        .publishOn(Schedulers.boundedElastic())
                        .flatMap((list) -> transactionRepository.saveAll(list)
                                .collectList()
                                .doOnNext((cList) -> log.info("Saved Transactions : " + cList.size()))
                                .doOnError((th) -> log.info("On Error " + th))
                                .map((cList) -> {
                                    if (!cList.isEmpty()) {
                                        var.addAll(cList);
                                    }
                                    return var;
                                }))
                );
    }

    private Mono<List<Transaction>> getTransactionWhenCacheRangeToGreaterThanRequestTo(TransactionService service, String cardNumber, boolean isComposite, LocalDateTime from, LocalDateTime to, Map<FromToEnum, LocalDateTime> timeMap) {
        log.info("getTransactionWhenCacheRangeToGreaterThanRequestTo");
        return Mono.just(isComposite)
                .flatMap(any -> {
                    if (any)
                        return transactionRepository.findAllByStartQuery(concat(cardNumber, "%"),
                                        from, timeMap.get(FromToEnum.TO))
                                .collectList();
                    return transactionRepository.findAllByQuery(cardNumber,
                            from, timeMap.get(FromToEnum.TO)).collectList();
                })
                .doOnNext((list) -> log.info("Cache Transactions Count : " + list.size()))
                .flatMap((var) -> service.getAllTransactionsByDates(cardNumber, timeMap.get(FromToEnum.TO), to)
                        .collectList()
                        .doOnNext((list) -> log.info("External Transactions Count : " + list.size()))
                        .flatMap(list -> transactionRepository.saveAll(list)
                                .collectList()
                                .map((s) -> {
                                    s.addAll(var);
                                    return s;
                                }).doOnError((s) -> log.info("on Error" + s))
                                .doOnSuccess((s) -> log.info("Successfully Saved Transactions : " + s.size()))
                                .switchIfEmpty(Mono.just(var))
                        ));
    }

    private Mono<List<Transaction>> getTransactionsWhenCacheRangeFromGreaterThanRequestFrom(TransactionService service, String cardNumber, boolean isComposite, LocalDateTime from, LocalDateTime to, Map<FromToEnum, LocalDateTime> timeMap) {
        log.info("getTransactionsWhenCacheRangeFromGreaterThanRequestFrom()");
        return Mono.just(isComposite)
                .flatMap(any -> {
                    if (isComposite) return transactionRepository.findAllByStartQuery(
                            concat(cardNumber, "%"), timeMap.get(FromToEnum.FROM), to).collectList();
                    return transactionRepository.findAllByQuery(cardNumber,
                            timeMap.get(FromToEnum.FROM), to).collectList();
                }).doOnNext((list) -> log.info("Cache Transactions Count : " + list.size()))
                .flatMap((var) -> service.getAllTransactionsByDates(cardNumber, from, timeMap.get(FromToEnum.FROM))
                        .collectList()
                        .publishOn(Schedulers.boundedElastic())
                        .doOnNext((list) -> log.info("External Transaction Count : " + list.size()))
                        .flatMap(list -> transactionRepository.saveAll(list)
                                .collectList()
                                .map((s) -> {
                                    s.addAll(var);
                                    return s;
                                })
                                .doOnError((s) -> log.info("on Error" + s))
                                .doOnSuccess((s) -> log.info("Successfully Saved Transactions : " + s.size()))
                                .switchIfEmpty(Mono.just(var)))
                );
    }

    private Mono<List<Transaction>> getTransactionOnlyExternalServiceWhenCacheRangeGreaterThan(TransactionService service, String cardNumber, boolean isComposite, LocalDateTime from, LocalDateTime to, Map<FromToEnum, LocalDateTime> timeMap, int compareFrom) {
        return Mono.just(compareFrom)
                .log("getTransactionOnlyExternalServiceWhenCacheRangeGreaterThan()")
                .flatMap((var1) -> {
                    if (var1 < 0) return service.getAllTransactionsByDates(cardNumber,
                                    timeMap.get(FromToEnum.TO), to)
                            .collectList();
                    return service.getAllTransactionsByDates(cardNumber, from, timeMap.get(FromToEnum.FROM))
                            .collectList();
                })
                .doOnNext((list) -> log.info("External Returned List Size : " + list.size()))
                .publishOn(Schedulers.boundedElastic())
                .flatMap((var2) -> transactionRepository.saveAll(var2)
                        .collectList().doOnError((s) -> log.info("on Error" + s))
                        .doOnSuccess((s) -> log.info("Successfully Saved Transactions : " + s.size()))
                        .map((list) -> list.stream().filter(f -> f.getCreatedAt().isAfter(from)
                                && f.getCreatedAt().isBefore(to)).toList())
                        .doOnNext((list) -> log.info("Filtered Transactions Count : " + list.size()))
                        .switchIfEmpty(Mono.empty()));
    }

    private Mono<List<Transaction>> getTransactionsOnlyCache(String cardNumber, boolean isComposite, LocalDateTime from, LocalDateTime to) {
        log.info("Enter Only Get Cache Method");
        if (isComposite) {
            log.info("No CardNumber!");
            return transactionRepository.findAllByStartQuery(concat(cardNumber, "%"), from, to)
                    .collectList()
                    .doOnNext((list) -> log.info("Database Transaction Count : " + list.size()));
        }
        log.info("With CardNumber!");
        return transactionRepository.findAllByQuery(cardNumber, from, to)
                .collectList()
                .doOnNext((list) -> log.info("Database Transaction Count : " + list.size()));
    }

    private Mono<List<Transaction>> getTransactionsExternalServiceWhenCacheIsEmpty(TransactionService service, String cardNumber, LocalDateTime from, LocalDateTime to) {
        log.info("getTransactionsExternalServiceWhenCacheIsEmpty()");
        return service.getAllTransactionsByDates(cardNumber, from, to)
                .log("Debug Get Cache")
                .collectList()
                .doOnNext((var) -> log.info("External Service Return List Size : " + var.size()))
                .flatMap(list -> transactionRepository.saveAll(list)
                        .collectList().doOnNext((cList) -> log.info("Saved Transactions : " + cList.size()))
                        .doOnError((thr) -> log.info("On Error " + thr)));
    }

    private Mono<Map<FromToEnum, LocalDateTime>> getFromToDateRepository(String cardNumber, boolean isComposite) {
        return Mono.just(isComposite)
                .log("Enter getFromToDateRepository()")
                .map(any -> {
                    if (isComposite) {
                        return transactionRepository.findAll()
                                .filter(f -> f.getToCard().startsWith(cardNumber) || f.getFromCard().startsWith(cardNumber))
                                .sort(Comparator.comparing(Transaction::getCreatedAt));
                    } else {
                        return transactionRepository.findAll()
                                .filter(t -> t.getToCard().equals(cardNumber) || t.getFromCard().equals(cardNumber))
                                .sort(Comparator.comparing(Transaction::getCreatedAt));
                    }
                }).log("Debug Get Count")
                .flatMap(o -> o.collectList()
                        .map(m -> new HashMap<FromToEnum, LocalDateTime>() {{
                            put(FromToEnum.FROM, m.get(0).getCreatedAt());
                            put(FromToEnum.TO, m.get(m.size() - 1).getCreatedAt());
                        }}));
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
        HashSet<String> set = new HashSet<>(cards);
        if (cards.size() != set.size()) {
            throw new RuntimeException("Duplicate Card Numbers!");
        }
    }

    private String concat(String var, String literal) {
        return var.concat(literal);
    }

}
