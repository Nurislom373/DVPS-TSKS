package org.khasanof.uicachingstrategy.service;

import lombok.extern.slf4j.Slf4j;
import org.khasanof.uicachingstrategy.domain.TransactionEntity;
import org.khasanof.uicachingstrategy.enums.FromToEnum;
import org.khasanof.uicachingstrategy.repository.TransactionRepository;
import org.khasanof.uicachingstrategy.service.context.ContextTransactionService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    public List<TransactionEntity> getAllTransactionsByCardAndDates(String cardNumber, LocalDateTime from, LocalDateTime to) {
        checkParametersGetAllTransactionsByCardAndDates(cardNumber, from, to);
        return getAllTransactionsByService(contextTransactionService.getService(cardNumber), cardNumber, from, to);
    }

    public Map<String, List<TransactionEntity>> getAllTransactionsByCardsAndDates(List<String> cards, LocalDateTime from, LocalDateTime to) {
        checkParametersGetAllTransactionsByCardsAndDates(cards, from, to);
        return cards.stream().map(c -> new AbstractMap.SimpleEntry<>(c,
                        getAllTransactionsByService(contextTransactionService.getService(c), c, from, to)))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private List<TransactionEntity> getAllTransactionsByService(TransactionService service, String cardNumber, LocalDateTime from, LocalDateTime to) {

        boolean isComposite = cardNumber.equals("*");

        if (transactionRepository.count() == 0) {

            return getTransactionsExternalServiceWhenCacheIsEmpty(service, cardNumber, from, to);

        } else if ((!isComposite && transactionRepository.getCardCacheCount(cardNumber) == 0)) {

            return getTransactionsExternalServiceWhenCacheIsEmpty(service, cardNumber, from, to);

        } else {
            Map<FromToEnum, LocalDateTime> timeMap = getFromToDateRepository(cardNumber, isComposite);

            int compareFrom = timeMap.get(FromToEnum.FROM).compareTo(from);
            int compareTo = timeMap.get(FromToEnum.TO).compareTo(to);

            boolean isCache = (timeMap.get(FromToEnum.FROM).isBefore(from) && timeMap.get(FromToEnum.TO).isAfter(from)) ||
                    (timeMap.get(FromToEnum.FROM).isBefore(to) && timeMap.get(FromToEnum.TO).isAfter(to));

            boolean isBetweenCache = (timeMap.get(FromToEnum.FROM).isAfter(from) && timeMap.get(FromToEnum.TO).isBefore(to));

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
    }

    private List<TransactionEntity> getTransactionsWhenCacheBetweenFromAndToDates(TransactionService service, String cardNumber, boolean isComposite, LocalDateTime from, LocalDateTime to, Map<FromToEnum, LocalDateTime> timeMap) {
        List<TransactionEntity> cacheTransactions;

        if (isComposite) {
            cacheTransactions = transactionRepository.findAllByCreatedAtIsBetween(timeMap.get(FromToEnum.FROM),
                    timeMap.get(FromToEnum.TO));
        } else {
            cacheTransactions = transactionRepository.findAllByCreatedAtIsBetween(
                    cardNumber, timeMap.get(FromToEnum.FROM), timeMap.get(FromToEnum.TO));
        }

        List<TransactionEntity> fromTimeMap = service.getAllTransactionsByDates(cardNumber,
                from, timeMap.get(FromToEnum.FROM));

        List<TransactionEntity> toTimeMap = service.getAllTransactionsByDates(cardNumber,
                timeMap.get(FromToEnum.TO), to);

        if (fromTimeMap != null && !fromTimeMap.isEmpty()) {
            transactionRepository.saveAll(fromTimeMap);
        }

        if (toTimeMap != null && !toTimeMap.isEmpty()) {
            transactionRepository.saveAll(toTimeMap);
        }

        return Stream.of(cacheTransactions, fromTimeMap, toTimeMap).filter(Objects::nonNull)
                .flatMap(Collection::stream).sorted(Comparator.comparing(TransactionEntity::getId))
                .toList();
    }

    private List<TransactionEntity> getTransactionWhenCacheRangeToGreaterThanRequestTo(TransactionService service, String cardNumber, boolean isComposite, LocalDateTime from, LocalDateTime to, Map<FromToEnum, LocalDateTime> timeMap) {
        List<TransactionEntity> cacheList;

        if (isComposite) {
            cacheList = transactionRepository.findAllByCreatedAtIsBetween(from, to);
        } else {
            cacheList = transactionRepository.findAllByCreatedAtIsBetween(cardNumber,
                    from, timeMap.get(FromToEnum.TO));
        }

        List<TransactionEntity> repositoryList = service.getAllTransactionsByDates(cardNumber,
                timeMap.get(FromToEnum.TO), to);

        if (repositoryList != null && !repositoryList.isEmpty()) {
            transactionRepository.saveAll(repositoryList);
        }

        return Stream.of(cacheList, repositoryList).filter(Objects::nonNull)
                .flatMap(Collection::stream).sorted(Comparator.comparing(TransactionEntity::getId))
                .toList();
    }

    private List<TransactionEntity> getTransactionsWhenCacheRangeFromGreaterThanRequestFrom(TransactionService service, String cardNumber, boolean isComposite, LocalDateTime from, LocalDateTime to, Map<FromToEnum, LocalDateTime> timeMap) {
        List<TransactionEntity> cacheList;

        if (isComposite) {
            cacheList = transactionRepository.findAllByCreatedAtIsBetween(from, to);
        } else {
            cacheList = transactionRepository.findAllByCreatedAtIsBetween(cardNumber,
                    timeMap.get(FromToEnum.FROM), to);
        }

        List<TransactionEntity> repositoryList = service.getAllTransactionsByDates(cardNumber,
                from, timeMap.get(FromToEnum.FROM));

        if (repositoryList != null && !repositoryList.isEmpty()) {
            transactionRepository.saveAll(repositoryList);
        }

        return Stream.of(cacheList, repositoryList).filter(Objects::nonNull)
                .flatMap(Collection::stream).sorted(Comparator.comparing(TransactionEntity::getId))
                .toList();
    }

    private List<TransactionEntity> getTransactionOnlyExternalServiceWhenCacheRangeGreaterThan(TransactionService service, String cardNumber, boolean isComposite, LocalDateTime from, LocalDateTime to, Map<FromToEnum, LocalDateTime> timeMap, int compareFrom) {
        List<TransactionEntity> list;

        if (compareFrom < 0) {
            list = service.getAllTransactionsByDates(cardNumber, timeMap.get(FromToEnum.TO), to);
        } else {
            list = service.getAllTransactionsByDates(cardNumber, from, timeMap.get(FromToEnum.FROM));
        }

        if (list != null && !list.isEmpty()) {
            transactionRepository.saveAll(list);
        }

        if (isComposite) {
            return transactionRepository.findAllByCreatedAtIsBetween(from, to);
        }

        return transactionRepository.findAllByCreatedAtIsBetween(cardNumber, from, to);
    }

    private List<TransactionEntity> getTransactionsOnlyCache(String cardNumber, boolean isComposite, LocalDateTime from, LocalDateTime to) {
        if (isComposite) {
            return transactionRepository.findAllByCreatedAtIsBetween(from, to).stream()
                    .sorted(Comparator.comparing(TransactionEntity::getId)).toList();
        }
        return transactionRepository.findAllByCreatedAtIsBetween(cardNumber, from, to).stream()
                .sorted(Comparator.comparing(TransactionEntity::getId)).toList();
    }

    private List<TransactionEntity> getTransactionsExternalServiceWhenCacheIsEmpty(TransactionService service, String cardNumber, LocalDateTime from, LocalDateTime to) {
        List<TransactionEntity> list = service.getAllTransactionsByDates(cardNumber, from, to);

        if (list != null && !list.isEmpty()) {
            transactionRepository.saveAll(list);
            return list;
        }

        return new ArrayList<>();
    }

    private Map<FromToEnum, LocalDateTime> getFromToDateRepository(String cardNumber, boolean isComposite) {
        List<TransactionEntity> list;
        if (isComposite) {
            list = transactionRepository.findAll().stream()
                    .sorted(Comparator.comparing(TransactionEntity::getCreatedAt)).toList();
        } else {
            list = transactionRepository.findAll().stream()
                    .filter(t -> t.getToCard().equals(cardNumber) || t.getFromCard().equals(cardNumber))
                    .sorted(Comparator.comparing(TransactionEntity::getCreatedAt))
                    .toList();
        }
        return new HashMap<>() {{
            put(FromToEnum.FROM, list.get(0).getCreatedAt());
            put(FromToEnum.TO, list.get(list.size() - 1).getCreatedAt());
        }};
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
