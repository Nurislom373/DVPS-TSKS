package org.khasanof.uicaching;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.khasanof.uicaching.config.CacheHelper;
import org.khasanof.uicaching.domain.TransactionEntity;
import org.khasanof.uicaching.dto.TransactionCreateDTO;
import org.khasanof.uicaching.dto.TransactionUpdateDTO;
import org.khasanof.uicaching.enums.FromToEnum;
import org.khasanof.uicaching.enums.Status;
import org.khasanof.uicaching.repository.TransactionRepository;
import org.khasanof.uicaching.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

@SpringBootTest
class UiCachingApplicationTests {

    @Autowired
    private TransactionService service;

    @Autowired
    private TransactionRepository repository;

    @Autowired
    private CacheHelper cacheHelper;

    @Test
    void service_IsNotNull() {
        org.assertj.core.api.Assertions.assertThat(service).isNotNull();
    }

    @Test
    void getCacheFromToDate() {
        LocalDateTime from = LocalDateTime.of(2023, 2, 22, 0, 0);
        LocalDateTime to = LocalDateTime.of(2023, 2, 26, 0, 0);

        LocalDateTime fromTwo = LocalDateTime.of(2023, 2, 20, 0, 0);
        LocalDateTime toTwo = LocalDateTime.of(2023, 2, 28, 0, 0);
        List<TransactionEntity> listOne = service.getAllTransactionByDate(from, to);
        System.out.println("listOne = " + listOne);
        List<TransactionEntity> listTwo = service.getAllTransactionByDate(fromTwo, toTwo);
        System.out.println("listTwo = " + listTwo);
    }

    @Test
    void getTransactionById_Test() {
        IntStream.rangeClosed(0, 20).forEach(i -> {
            TransactionEntity transactionEntity = service.getTransactionById(1L);
            System.out.println("transactionEntity = " + transactionEntity);
        });
    }

    @Test
    void getTransactionByIdMethod_RuntimeException_IdNullAndUnsigned_Test() {
        Assertions.assertThrows(RuntimeException.class, () -> service.getTransactionById(null));
        Assertions.assertThrows(RuntimeException.class, () -> service.getTransactionById(-1L));
    }

    @Test
    void getTransactionByIdMethod_RuntimeException_NotFound_Test() {
        Assertions.assertThrows(RuntimeException.class, () -> service.getTransactionById(400L));
        Assertions.assertThrows(RuntimeException.class, () -> service.getTransactionById(500L));
    }

    @Test
    void saveMethod_Test() {
        TransactionEntity entity = service.save(new TransactionCreateDTO(new BigDecimal("73825745625432.0"),
                Status.SUCCESS));

        org.assertj.core.api.Assertions.assertThat(cacheHelper.cacheContainKey(entity.getId())).isTrue();

        TransactionEntity transactionEntity = service.getTransactionById(entity.getId());

        Assertions.assertEquals(entity.getId(), transactionEntity.getId());
        Assertions.assertEquals(entity.getAmount(), transactionEntity.getAmount());
        Assertions.assertEquals(entity.getStatus(), transactionEntity.getStatus());
        Assertions.assertEquals(entity.getCreatedAt(), transactionEntity.getCreatedAt());
    }

    @Test
    void saveMethod_RuntimeException_DtoIsNull_Test() {
        Assertions.assertThrows(RuntimeException.class, () -> service.save(null));
    }

    @Test
    void updateMethod_Test() {
        TransactionEntity update = service.update(new TransactionUpdateDTO(100L, new BigDecimal("657546743435.00"),
                Status.SUCCESS));

        TransactionEntity transactionEntity = service.getTransactionById(update.getId());

        Assertions.assertEquals(update.getId(), transactionEntity.getId());
        Assertions.assertEquals(update.getAmount(), transactionEntity.getAmount());
        Assertions.assertEquals(update.getStatus(), transactionEntity.getStatus());
        Assertions.assertEquals(update.getCreatedAt(), transactionEntity.getCreatedAt());
    }

    @Test
    void updateMethod_RuntimeException_DtoIdNullAndUnsigned_Test() {
        TransactionUpdateDTO dto1 = new TransactionUpdateDTO(null, new BigDecimal("657546743435.00"), Status.FAIL);
        TransactionUpdateDTO dto2 = new TransactionUpdateDTO(-1L, new BigDecimal("657546743435.00"), Status.FAIL);
        TransactionUpdateDTO dto3 = new TransactionUpdateDTO(1000L, new BigDecimal("657546743435.00"), Status.FAIL);
        TransactionUpdateDTO dto4 = new TransactionUpdateDTO(Long.parseLong("386287"),
                new BigDecimal("657546743435.00"), Status.FAIL);

        Assertions.assertThrows(RuntimeException.class, () -> service.update(dto1));
        Assertions.assertThrows(RuntimeException.class, () -> service.update(dto2));
        Assertions.assertThrows(RuntimeException.class, () -> service.update(dto3));
        Assertions.assertThrows(RuntimeException.class, () -> service.update(dto4));
    }

    @Test
    void getAllTransactionByDateMethod_Test() {
        cacheHelper.clearCache();

        LocalDateTime from = LocalDateTime.of(2023, 2, 22, 0, 0);
        LocalDateTime to = LocalDateTime.of(2023, 2, 26, 0, 0);

        List<TransactionEntity> all = repository.findAllByCreatedAtIsBetween(from, to);
        List<Long> idsList = all.stream().map(TransactionEntity::getId).toList();

        org.assertj.core.api.Assertions.assertThat(cacheHelper.cacheContainsAllKey(idsList)).isFalse();

        List<TransactionEntity> list1 = getTransactions(from, to);

        list1.forEach(System.out::println);

        org.assertj.core.api.Assertions.assertThat(cacheHelper.cacheContainsAllKey(idsList)).isTrue();
    }

    @Test
    void getAllTransactionByDateMethod_RuntimeException_DateEqual_Test() {
        LocalDateTime from = LocalDateTime.of(2023, 2, 22, 0, 0);
        LocalDateTime to = LocalDateTime.of(2023, 2, 22, 0, 0);

        Assertions.assertThrows(RuntimeException.class, () -> getTransactions(from, to));
    }

    @Test
    void getAllTransactionByDateMethod_RuntimeException_ToGreaterThanFrom_Test() {
        LocalDateTime from = LocalDateTime.of(2023, 2, 25, 0, 0);
        LocalDateTime to = LocalDateTime.of(2023, 2, 22, 0, 0);

        Assertions.assertThrows(RuntimeException.class, () -> getTransactions(from, to));
    }

    @Test
    void getAllTransactionByDateMethod_RuntimeException_FromAndToNull_Test() {
        Assertions.assertThrows(RuntimeException.class, () -> getTransactions(null, null));
    }

    @Test
    void getAllTransactionByDateMethod_EqualSizeCacheAndRepository_Test() {
        cacheHelper.clearCache();

        LocalDateTime from = LocalDateTime.of(2023, 2, 22, 0, 0);
        LocalDateTime to = LocalDateTime.of(2023, 2, 24, 0, 0);

        List<TransactionEntity> all = repository.findAllByCreatedAtIsBetween(from, to);
        List<Long> idsList = all.stream().map(TransactionEntity::getId).toList();

        org.assertj.core.api.Assertions.assertThat(cacheHelper.cacheContainsAllKey(idsList)).isFalse();

        List<TransactionEntity> listRepository = getTransactions(from, to);

        org.assertj.core.api.Assertions.assertThat(cacheHelper.cacheContainsAllKey(idsList)).isTrue();

        List<TransactionEntity> listCache = getTransactions(from, to);

        Assertions.assertEquals(all.size(), listRepository.size());
        Assertions.assertEquals(all.size(), listCache.size());
    }

    @Test
    void getAllTransactionByDateMethod_FromToParametersNull_Test() {
        LocalDateTime from = LocalDateTime.of(2023, 2, 22, 0, 0);
        LocalDateTime to = LocalDateTime.of(2023, 2, 24, 0, 0);

        Assertions.assertThrows(RuntimeException.class, () -> getTransactions(null, to));

        Assertions.assertThrows(RuntimeException.class, () -> getTransactions(from, null));
    }

    @Test
    void getAllTransactionByDateMethod_IsEmpty_Test() {
        List<TransactionEntity> listRepository = getTransactions(
                LocalDateTime.of(2023, 2, 15, 0, 0),
                LocalDateTime.of(2023, 2, 19, 0, 0));

        System.out.println("listRepository = " + listRepository);

        List<TransactionEntity> listCache = getTransactions(
                LocalDateTime.of(2023, 2, 15, 0, 0),
                LocalDateTime.of(2023, 2, 19, 0, 0));

        System.out.println("listCache = " + listCache);

        org.assertj.core.api.Assertions.assertThat(listRepository.isEmpty()).isTrue();
        org.assertj.core.api.Assertions.assertThat(listCache.isEmpty()).isTrue();
    }

    @Test
    void getAllTransactionByDateMethod_NoYear_Test() {
        List<TransactionEntity> list1 = getTransactions(
                LocalDateTime.of(2033, 2, 22, 0, 0),
                LocalDateTime.of(2033, 2, 26, 0, 0)
        );

        List<TransactionEntity> list2 = getTransactions(
                LocalDateTime.of(2063, 2, 22, 0, 0),
                LocalDateTime.of(2063, 2, 26, 0, 0)
        );

        org.assertj.core.api.Assertions.assertThat(list1).isEmpty();
        org.assertj.core.api.Assertions.assertThat(list2).isEmpty();
    }

    @Test
    void getAllTransactionByDateMethod_HourAndMinutes_Test() {
        List<TransactionEntity> list = getTransactions(
                LocalDateTime.of(2023, 2, 28, 6, 30),
                LocalDateTime.of(2023, 3, 2, 23, 30)
        );

        org.assertj.core.api.Assertions.assertThat(list).isNotNull();
    }

    @Test
    void getAllTransactionByDateMethod_Exception_DateTimeException_Test() {
        Assertions.assertThrows(DateTimeException.class, () -> getTransactions(
                LocalDateTime.of(2023, 2, 30, 6, 30),
                LocalDateTime.of(2023, 3, 2, 23, 30)
        ));

        Assertions.assertThrows(DateTimeException.class, () -> getTransactions(
                LocalDateTime.of(-2023, 2, 30, 6, 30),
                LocalDateTime.of(2023, 3, 2, 23, 30)
        ));

        Assertions.assertThrows(DateTimeException.class, () -> getTransactions(
                LocalDateTime.of(2023, 2, 30, 6, 30),
                LocalDateTime.of(2023, 3, 2, 23, 62)
        ));

        Assertions.assertThrows(DateTimeException.class, () -> getTransactions(
                LocalDateTime.of(2023, 2, 34, 6, 30),
                LocalDateTime.of(2023, 3, 42, 23, 12)
        ));

        Assertions.assertThrows(DateTimeException.class, () -> getTransactions(
                LocalDateTime.of(2023, 20, 14, 6, 30),
                LocalDateTime.of(2023, 13, 22, 23, 12)
        ));

        Assertions.assertThrows(DateTimeException.class, () -> getTransactions(
                LocalDateTime.of(2023, 20, 14, 60, 30),
                LocalDateTime.of(2023, 13, 22, 33, 12)
        ));
    }

    @Test
    void getAllTransactionByDateMethod_CacheIsAbsent_Test() {
        cacheHelper.clearCache();

        LocalDateTime from = LocalDateTime.of(2023, 2, 20, 0, 0);
        LocalDateTime to = LocalDateTime.of(2023, 2, 24, 0, 0);

        List<TransactionEntity> all = repository.findAllByCreatedAtIsBetween(from, to);

        List<Long> idsList = all.stream().map(TransactionEntity::getId)
                .toList();

        org.assertj.core.api.Assertions.assertThat(cacheHelper.cacheContainsAllKey(idsList)).isFalse();

        getTransactions(from, to);

        org.assertj.core.api.Assertions.assertThat(cacheHelper.cacheContainsAllKey(idsList)).isTrue();
    }

    private List<TransactionEntity> getTransactions(LocalDateTime from, LocalDateTime to) {
        return service.getAllTransactionByDate(from, to);
    }
}
