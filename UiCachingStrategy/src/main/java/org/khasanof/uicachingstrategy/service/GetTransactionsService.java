package org.khasanof.uicachingstrategy.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.khasanof.uicachingstrategy.domain.TransactionEntity;
import org.khasanof.uicachingstrategy.enums.FromToEnum;
import org.khasanof.uicachingstrategy.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
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
@Service
@RequiredArgsConstructor
@Slf4j
public class GetTransactionsService {

    private final ContextTransactionServices contextTransactionService;
    private final TransactionRepository transactionRepository;

    public List<TransactionEntity> getAllTransactionByDate(String cardNumber, LocalDateTime from, LocalDateTime to) {
        checkParameters(cardNumber, from, to);

        if (transactionRepository.getCardCacheCount(cardNumber) == 0) {

            return getTransactionsExternalServiceWhenCacheIsEmpty(cardNumber, from, to);

        } else {
            Map<FromToEnum, LocalDateTime> timeMap = getFromToDateRepository(cardNumber);

            int compareFrom = timeMap.get(FromToEnum.FROM).compareTo(from);
            int compareTo = timeMap.get(FromToEnum.TO).compareTo(to);

            boolean isCache = (timeMap.get(FromToEnum.FROM).isBefore(from) && timeMap.get(FromToEnum.TO).isAfter(from)) ||
                    (timeMap.get(FromToEnum.FROM).isBefore(to) && timeMap.get(FromToEnum.TO).isAfter(to));

            boolean isBetweenCache = (timeMap.get(FromToEnum.FROM).isAfter(from) && timeMap.get(FromToEnum.TO).isBefore(to));

            if (compareFrom <= 0 && compareTo >= 0) {

                return getTransactionsOnlyCache(cardNumber, from, to);

            } else if ((!isCache && !isBetweenCache)) {

                return getTransactionOnlyExternalServiceWhenCacheRangeGreaterThan(cardNumber, from, to, timeMap, compareFrom);

            } else if (compareFrom <= 0) {

                return getTransactionWhenCacheRangeToGreaterThanRequestTo(cardNumber, from, to, timeMap);

            } else if (compareTo >= 0) {

                return getTransactionsWhenCacheRangeFromGreaterThanRequestFrom(cardNumber, from, to, timeMap);

            } else {

                return getTransactionsWhenCacheBetweenFromAndToDates(cardNumber, from, to, timeMap);
            }
        }
    }

    private List<TransactionEntity> getTransactionsWhenCacheBetweenFromAndToDates(String cardNumber, LocalDateTime from, LocalDateTime to, Map<FromToEnum, LocalDateTime> timeMap) {
        List<TransactionEntity> cacheTransactions = transactionRepository.findAllByCreatedAtIsBetween(
                cardNumber, timeMap.get(FromToEnum.FROM), timeMap.get(FromToEnum.TO));

        List<TransactionEntity> fromTimeMap = contextTransactionService.getService(cardNumber)
                .getAllTransactionsByDates(cardNumber, from, timeMap.get(FromToEnum.FROM));

        List<TransactionEntity> toTimeMap = contextTransactionService.getService(cardNumber)
                .getAllTransactionsByDates(cardNumber, timeMap.get(FromToEnum.TO), to);

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

    private List<TransactionEntity> getTransactionWhenCacheRangeToGreaterThanRequestTo(String cardNumber, LocalDateTime from, LocalDateTime to, Map<FromToEnum, LocalDateTime> timeMap) {
        List<TransactionEntity> cacheList = transactionRepository.findAllByCreatedAtIsBetween(cardNumber,
                from, timeMap.get(FromToEnum.TO));

        List<TransactionEntity> repositoryList = contextTransactionService.getService(cardNumber)
                .getAllTransactionsByDates(cardNumber, timeMap.get(FromToEnum.TO), to);

        if (repositoryList != null && !repositoryList.isEmpty()) {
            transactionRepository.saveAll(repositoryList);
        }

        return Stream.of(cacheList, repositoryList).filter(Objects::nonNull)
                .flatMap(Collection::stream).sorted(Comparator.comparing(TransactionEntity::getId))
                .toList();
    }

    private List<TransactionEntity> getTransactionsWhenCacheRangeFromGreaterThanRequestFrom(String cardNumber, LocalDateTime from, LocalDateTime to, Map<FromToEnum, LocalDateTime> timeMap) {
        List<TransactionEntity> cacheList = transactionRepository.findAllByCreatedAtIsBetween(cardNumber,
                timeMap.get(FromToEnum.FROM), to);

        List<TransactionEntity> repositoryList = contextTransactionService.getService(cardNumber)
                .getAllTransactionsByDates(cardNumber, from, timeMap.get(FromToEnum.FROM));

        if (repositoryList != null && !repositoryList.isEmpty()) {
            transactionRepository.saveAll(repositoryList);
        }

        return Stream.of(cacheList, repositoryList).filter(Objects::nonNull)
                .flatMap(Collection::stream).sorted(Comparator.comparing(TransactionEntity::getId))
                .toList();
    }

    private List<TransactionEntity> getTransactionOnlyExternalServiceWhenCacheRangeGreaterThan(String cardNumber, LocalDateTime from, LocalDateTime to, Map<FromToEnum, LocalDateTime> timeMap, int compareFrom) {
        List<TransactionEntity> list;

        if (compareFrom < 0) {
            list = contextTransactionService.getService(cardNumber)
                    .getAllTransactionsByDates(cardNumber, timeMap.get(FromToEnum.TO), to);
        } else {
            list = contextTransactionService.getService(cardNumber)
                    .getAllTransactionsByDates(cardNumber, from, timeMap.get(FromToEnum.FROM));
        }

        if (list != null && !list.isEmpty()) {
            transactionRepository.saveAll(list);
        }

        return transactionRepository.findAllByCreatedAtIsBetween(cardNumber, from, to);
    }

    private List<TransactionEntity> getTransactionsOnlyCache(String cardNumber, LocalDateTime from, LocalDateTime to) {
        return transactionRepository.findAllByCreatedAtIsBetween(cardNumber, from, to).stream()
                .sorted(Comparator.comparing(TransactionEntity::getId)).toList();
    }

    private List<TransactionEntity> getTransactionsExternalServiceWhenCacheIsEmpty(String cardNumber, LocalDateTime from, LocalDateTime to) {
        List<TransactionEntity> list = contextTransactionService.getService(cardNumber)
                .getAllTransactionsByDates(cardNumber, from, to);

        if (list != null && !list.isEmpty()) {
            transactionRepository.saveAll(list);
            return list;
        }

        return new ArrayList<>();
    }

    private Map<FromToEnum, LocalDateTime> getFromToDateRepository(String cardNumber) {
        List<TransactionEntity> list = transactionRepository.findAll().stream()
                .filter(t -> t.getToCard().equals(cardNumber) || t.getFromCard().equals(cardNumber))
                .sorted(Comparator.comparing(TransactionEntity::getCreatedAt))
                .toList();
        return new HashMap<>() {{
            put(FromToEnum.FROM, list.get(0).getCreatedAt());
            put(FromToEnum.TO, list.get(list.size() - 1).getCreatedAt());
        }};
    }

    private void checkParameters(String cardNumber, LocalDateTime from, LocalDateTime to) {
        if (Objects.isNull(from) || Objects.isNull(to)) {
            throw new RuntimeException("Invalid Date!");
        }
        if (from.isEqual(to)) {
            throw new RuntimeException("From Date is Equal To Date!");
        }
        if (from.isAfter(to)) {
            throw new RuntimeException("From Date is Greater Than To Date!");
        }
        if (cardNumber.length() != 16) {
            throw new RuntimeException("Invalid CardNUmber!");
        }
    }

}
