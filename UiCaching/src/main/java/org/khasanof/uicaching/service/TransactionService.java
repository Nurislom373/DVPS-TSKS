package org.khasanof.uicaching.service;

import org.khasanof.uicaching.config.CacheHelper;
import org.khasanof.uicaching.domain.TransactionEntity;
import org.khasanof.uicaching.dto.TransactionCreateDTO;
import org.khasanof.uicaching.dto.TransactionUpdateDTO;
import org.khasanof.uicaching.enums.FromToEnum;
import org.khasanof.uicaching.repository.TransactionRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Stream;

/**
 * Author: Nurislom
 * <br/>
 * Date: 2/28/2023
 * <br/>
 * Time: 4:40 PM
 * <br/>
 * Package: org.khasanof.uicaching.service
 */
@Service
public class TransactionService {

    private final TransactionRepository repository;

    private final CacheHelper cacheHelper;

    public TransactionService(TransactionRepository repository, CacheHelper cacheHelper) {
        this.repository = repository;
        this.cacheHelper = cacheHelper;
    }

    public TransactionEntity save(TransactionCreateDTO dto) {
        checkDto(dto);
        TransactionEntity entity = new TransactionEntity();
        BeanUtils.copyProperties(dto, entity);
        TransactionEntity transaction = repository.save(entity);
        cacheHelper.addCache(transaction);
        return transaction;
    }

    public TransactionEntity update(TransactionUpdateDTO dto) {
        checkDto(dto);
        checkId(dto.getId());
        TransactionEntity entity = repository.findById(Math.toIntExact(dto.getId()))
                .orElseThrow(() -> new RuntimeException("Transaction Not Found"));
        BeanUtils.copyProperties(dto, entity, "id");
        TransactionEntity transaction = repository.save(entity);
        cacheHelper.updateCache(transaction);
        return transaction;
    }

    public TransactionEntity getTransactionById(Long id) {
        checkId(id);
        TransactionEntity cacheEntity = cacheHelper.findById(id);
        if (Objects.isNull(cacheEntity)) {
            TransactionEntity entity = repository.findById(Math.toIntExact(id))
                    .orElseThrow(() -> new RuntimeException("Transaction Not Found"));
            cacheHelper.addCache(entity);
            return entity;
        }
        return cacheEntity;
    }

    public List<TransactionEntity> getAllTransactionByDate(LocalDateTime from, LocalDateTime to) {
        checkFromToDates(from, to);

        if (cacheHelper.isEmpty()) {

            List<TransactionEntity> list = repository.findAllByCreatedAtIsBetween(from, to);

            if (list != null && !list.isEmpty()) {
                cacheHelper.addAllTransactionCache(list);
                return list;
            }

            return new ArrayList<>();
        } else {
            Map<FromToEnum, LocalDateTime> timeMap = cacheHelper.getFromToDate();

            int compareFrom = timeMap.get(FromToEnum.FROM).compareTo(from);
            int compareTo = timeMap.get(FromToEnum.TO).compareTo(to);

            boolean check = compareFrom < compareTo;

            boolean isCache = (timeMap.get(FromToEnum.FROM).isBefore(from) && timeMap.get(FromToEnum.TO).isAfter(from)) ||
                    (timeMap.get(FromToEnum.FROM).isBefore(to) && timeMap.get(FromToEnum.TO).isAfter(to));

            boolean isBetweenCache = (timeMap.get(FromToEnum.FROM).isAfter(from) && timeMap.get(FromToEnum.TO).isBefore(to));

            if (compareFrom <= 0 && compareTo >= 0) {

                return cacheHelper.getAllTransactions(from, to).stream()
                        .sorted(Comparator.comparing(TransactionEntity::getId)).toList();

            } else if ((!isCache && !isBetweenCache)) {

                List<TransactionEntity> list;

                if (compareFrom < 0) {
                    list = repository.findAllByCreatedAtIsBetween(timeMap.get(FromToEnum.TO), to);
                } else {
                    list = repository.findAllByCreatedAtIsBetween(from, timeMap.get(FromToEnum.FROM));
                }

                if (list != null && !list.isEmpty()) {
                    cacheHelper.addAllTransactionCache(list);
                }

                return cacheHelper.getAllTransactions(from, to);

            } else if (compareFrom <= 0) {

                List<TransactionEntity> cacheList = cacheHelper.getAllTransactions(from, timeMap.get(FromToEnum.TO));

                List<TransactionEntity> repositoryList = repository.findAllByCreatedAtIsBetween(timeMap
                        .get(FromToEnum.TO), to);

                if (repositoryList != null && !repositoryList.isEmpty()) {
                    cacheHelper.addAllTransactionCache(repositoryList);
                }

                return Stream.of(cacheList, repositoryList).filter(Objects::nonNull)
                        .flatMap(Collection::stream).sorted(Comparator.comparing(TransactionEntity::getId))
                        .toList();

            } else if (compareTo >= 0) {

                List<TransactionEntity> cacheList = cacheHelper.getAllTransactions(
                        timeMap.get(FromToEnum.FROM), to);

                List<TransactionEntity> repositoryList = repository.findAllByCreatedAtIsBetween(from,
                        timeMap.get(FromToEnum.FROM));

                if (repositoryList != null && !repositoryList.isEmpty()) {
                    cacheHelper.addAllTransactionCache(repositoryList);
                }

                return Stream.of(cacheList, repositoryList).filter(Objects::nonNull)
                        .flatMap(Collection::stream).sorted(Comparator.comparing(TransactionEntity::getId))
                        .toList();

            } else {

                List<TransactionEntity> cacheTransactions = cacheHelper.getAllTransactions(timeMap.get(FromToEnum.FROM),
                        timeMap.get(FromToEnum.TO));

                List<TransactionEntity> fromTimeMap = repository.findAllByCreatedAtIsBetween(from,
                        timeMap.get(FromToEnum.FROM));

                List<TransactionEntity> toTimeMap = repository.findAllByCreatedAtIsBetween(timeMap.get(FromToEnum.TO),
                        to);

                if (fromTimeMap != null && !fromTimeMap.isEmpty()) {
                    cacheHelper.addAllTransactionCache(fromTimeMap);
                }

                if (toTimeMap != null && !toTimeMap.isEmpty()) {
                    cacheHelper.addAllTransactionCache(toTimeMap);
                }

                return Stream.of(cacheTransactions, fromTimeMap, toTimeMap).filter(Objects::nonNull)
                        .flatMap(Collection::stream).sorted(Comparator.comparing(TransactionEntity::getId))
                        .toList();
            }
        }
    }

    @Deprecated
    private List<TransactionEntity> getAllTransactionsV1(LocalDateTime from, LocalDateTime to) {
        List<TransactionEntity> transactionListWithCache = cacheHelper.getAllTransactions(from, to);

        if (Objects.isNull(transactionListWithCache) || transactionListWithCache.isEmpty()) {
            List<TransactionEntity> list = repository.findAllByCreatedAtIsBetween(from, to);

            if (Objects.isNull(list)) {
                return new ArrayList<>();
            }

            if (!list.isEmpty()) {
                cacheHelper.addAllTransactionCache(list);
            }
            return list;
        }

        List<Long> longs = transactionListWithCache.stream()
                .map(TransactionEntity::getId)
                .toList();

        List<TransactionEntity> transactionsListWithRepository = repository.findAllByIdIsNotInAndCreatedAtBetween(longs,
                from, to);

        if (Objects.isNull(transactionsListWithRepository) || transactionsListWithRepository.isEmpty()) {
            return transactionListWithCache;
        }

        cacheHelper.addAllTransactionCache(transactionsListWithRepository);

        return Stream.concat(transactionListWithCache.stream(), transactionsListWithRepository.stream())
                .sorted(Comparator.comparing(TransactionEntity::getId)).toList();
    }

    private void checkFromToDates(LocalDateTime from, LocalDateTime to) {
        if (Objects.isNull(from) || Objects.isNull(to)) {
            throw new RuntimeException("Invalid Date!");
        }
        if (from.isEqual(to)) {
            throw new RuntimeException("From Date is Equal To Date!");
        }
        if (from.isAfter(to)) {
            throw new RuntimeException("From Date is Greater Than To Date!");
        }
    }

    private void checkDto(Object dto) {
        if (Objects.isNull(dto)) {
            throw new RuntimeException("Invalid DTO!");
        }
    }

    private void checkId(Long id) {
        if (id == null || id < 1) {
            throw new RuntimeException("Invalid ID!");
        }
    }
}
