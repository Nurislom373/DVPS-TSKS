package org.khasanof.uicaching;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.khasanof.uicaching.config.CacheHelper;
import org.khasanof.uicaching.domain.TransactionEntity;
import org.khasanof.uicaching.enums.Status;
import org.khasanof.uicaching.repository.TransactionRepository;
import org.khasanof.uicaching.service.TransactionService;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Author: Nurislom
 * <br/>
 * Date: 3/2/2023
 * <br/>
 * Time: 6:04 PM
 * <br/>
 * Package: org.khasanof.uicaching
 */
@SpringBootTest
@ExtendWith(SpringExtension.class)
public class TransactionServiceMockTest {

    @Autowired
    private TransactionRepository repository;

    @Autowired
    private CacheHelper cacheHelper;

    // Repository How Many Called Mock Test
    @Test
    void repositoryHowManyCalled_MockTest() {
        TransactionRepository mock = Mockito.mock(TransactionRepository.class);
        TransactionService transactionService = new TransactionService(mock,
                cacheHelper);

        LocalDateTime from = LocalDateTime.of(2023, 3, 1, 0, 0);
        LocalDateTime to = LocalDateTime.of(2023, 3, 3, 0, 0);

        List<TransactionEntity> list = List.of(
                new TransactionEntity(1L, new BigDecimal("589436584.4937"), Status.SUCCESS,
                        LocalDateTime.now().minusDays(1), LocalDateTime.now()),

                new TransactionEntity(2L, new BigDecimal("589436584.4937"), Status.SUCCESS,
                        LocalDateTime.now().minusDays(1), LocalDateTime.now())
        );

        Mockito.when(mock.findAllByCreatedAtIsBetween(ArgumentMatchers.any(),
                ArgumentMatchers.any())).thenReturn(list);

        List<TransactionEntity> all = mock.findAllByCreatedAtIsBetween(from, to);
        System.out.println("all = " + all);

        List<Long> idsList = all.stream().map(TransactionEntity::getId).toList();

        Assertions.assertThat(cacheHelper.cacheContainsAllKey(idsList)).isFalse();

        List<TransactionEntity> list1 = transactionService.getAllTransactionByDate(from, to);
        List<TransactionEntity> list2 = transactionService.getAllTransactionByDate(from, to);
        List<TransactionEntity> list3 = transactionService.getAllTransactionByDate(from, to);

        org.junit.jupiter.api.Assertions.assertEquals(all.size(), list1.size());
        org.junit.jupiter.api.Assertions.assertEquals(all.size(), list2.size());
        org.junit.jupiter.api.Assertions.assertEquals(all.size(), list3.size());

        Mockito.verify(mock, Mockito.times(2))
                .findAllByCreatedAtIsBetween(ArgumentMatchers.any(), ArgumentMatchers.any());
    }

    // Cache How Many Called Mock Test
    @Test
    void cacheHowManyCalled_MockTest() {
        CacheHelper cacheHelper = Mockito.mock(CacheHelper.class);
        TransactionRepository transactionRepository = Mockito.mock(TransactionRepository.class);
        TransactionService transactionService = new TransactionService(transactionRepository, cacheHelper);

        LocalDateTime from = LocalDateTime.of(2023, 3, 1, 0, 0);
        LocalDateTime to = LocalDateTime.of(2023, 3, 3, 0, 0);

        List<TransactionEntity> list = List.of(
                new TransactionEntity(1L, new BigDecimal("589436584.4937"), Status.SUCCESS,
                        LocalDateTime.now().minusDays(1), LocalDateTime.now()),

                new TransactionEntity(2L, new BigDecimal("589436584.4937"), Status.SUCCESS,
                        LocalDateTime.now().minusDays(1), LocalDateTime.now())
        );

        Mockito.when(cacheHelper.getAllTransactions(from, to)).thenReturn(list);
        Mockito.when(transactionRepository.findAllByIdIsNotInAndCreatedAtBetween(ArgumentMatchers.anyCollection(),
                ArgumentMatchers.any(), ArgumentMatchers.any())).thenReturn(new ArrayList<>());

        List<TransactionEntity> list1 = transactionService.getAllTransactionByDate(from, to); // call 1
        List<TransactionEntity> list2 = transactionService.getAllTransactionByDate(from, to); // call 2
        List<TransactionEntity> list3 = transactionService.getAllTransactionByDate(from, to); // call 3

        org.junit.jupiter.api.Assertions.assertEquals(list.size(), list1.size());
        org.junit.jupiter.api.Assertions.assertEquals(list.size(), list2.size());
        org.junit.jupiter.api.Assertions.assertEquals(list.size(), list3.size());

        Mockito.verify(cacheHelper, Mockito.times(3))
                .getAllTransactions(ArgumentMatchers.any(), ArgumentMatchers.any());
    }

    @Test
    void cacheIsEmptyHowManyCalled_MockTest() {
        CacheHelper cacheHelper = Mockito.mock(CacheHelper.class);
        TransactionService transactionService = new TransactionService(repository, cacheHelper);

        LocalDateTime from = LocalDateTime.of(2023, 2, 26, 0, 0);
        LocalDateTime to = LocalDateTime.of(2023, 2, 28, 0, 0);

        Mockito.when(cacheHelper.getAllTransactions(from, to)).thenReturn(null);

        transactionService.getAllTransactionByDate(from, to); // call 1
        transactionService.getAllTransactionByDate(from, to); // call 2

        Mockito.verify(cacheHelper, Mockito.times(2))
                .addAllTransactionCache(ArgumentMatchers.any());

        Mockito.verify(cacheHelper, Mockito.times(2))
                .getAllTransactions(from, to);
    }

    @Test
    void repository_FindAllByCreatedAtIsBetweenMethod_SeveralCases_MockTest() {
        TransactionRepository repository = Mockito.mock(TransactionRepository.class);
        TransactionService transactionService = new TransactionService(repository, cacheHelper);

        List<TransactionEntity> list = List.of(
                new TransactionEntity(1L, new BigDecimal("589436584.4937"), Status.SUCCESS,
                        LocalDateTime.now().minusDays(1), LocalDateTime.now()),

                new TransactionEntity(2L, new BigDecimal("589436584.4937"), Status.SUCCESS,
                        LocalDateTime.now().minusDays(1), LocalDateTime.now())
        );

        LocalDateTime fromOne = LocalDateTime.of(2023, 2, 17, 0, 0);
        LocalDateTime toOne = LocalDateTime.of(2023, 2, 18, 0, 0);

        LocalDateTime fromTwo = LocalDateTime.of(2023, 3, 1, 0, 0);
        LocalDateTime toTwo = LocalDateTime.of(2023, 3, 3, 0, 0);

        LocalDateTime fromThree = LocalDateTime.of(2024, 3, 1, 0, 0);
        LocalDateTime toThree = LocalDateTime.of(2024, 3, 3, 0, 0);

        Mockito.when(repository.findAllByCreatedAtIsBetween(fromOne, toOne)).thenReturn(null);
        Mockito.when(repository.findAllByCreatedAtIsBetween(fromTwo, toTwo)).thenReturn(list);
        Mockito.when(repository.findAllByCreatedAtIsBetween(fromThree, toThree)).thenReturn(new ArrayList<>());

        List<TransactionEntity> listOne = transactionService.getAllTransactionByDate(fromOne, toOne); // call 1
        List<TransactionEntity> listTwo = transactionService.getAllTransactionByDate(fromTwo, toTwo); // call 2
        List<TransactionEntity> listThree = transactionService.getAllTransactionByDate(fromThree, toThree); // call 3

        org.junit.jupiter.api.Assertions.assertNotNull(listOne);
        org.junit.jupiter.api.Assertions.assertNotNull(listTwo);
        org.junit.jupiter.api.Assertions.assertNotNull(listThree);

        org.junit.jupiter.api.Assertions.assertEquals(listOne.size(), 0);
        org.junit.jupiter.api.Assertions.assertEquals(listTwo.size(), list.size());
        org.junit.jupiter.api.Assertions.assertEquals(listThree.size(), 0);

        Mockito.verify(repository, Mockito.times(3))
                .findAllByCreatedAtIsBetween(ArgumentMatchers.any(), ArgumentMatchers.any());
    }

    @Test
    void repositoryIsEmptyHowManyCalled_MockTest() {
        TransactionRepository transactionRepository = Mockito.mock(TransactionRepository.class);
        CacheHelper cacheHelper = Mockito.mock(CacheHelper.class);
        TransactionService transactionService = new TransactionService(transactionRepository, cacheHelper);

        LocalDateTime from = LocalDateTime.of(2023, 2, 26, 0, 0);
        LocalDateTime to = LocalDateTime.of(2023, 2, 28, 0, 0);

        Mockito.when(transactionRepository.findAllByIdIsNotInAndCreatedAtBetween(ArgumentMatchers.any(),
                ArgumentMatchers.any(), ArgumentMatchers.any())).thenReturn(null);

        Mockito.when(cacheHelper.getAllTransactions(from, to)).thenReturn(getTransactionList());

        List<TransactionEntity> listOne = transactionService.getAllTransactionByDate(from, to); // call 1
        List<TransactionEntity> listTwo = transactionService.getAllTransactionByDate(from, to); // call 2

        Assertions.assertThat(listOne.size()).isEqualTo(2);
        Assertions.assertThat(listTwo.size()).isEqualTo(2);

        Mockito.verify(transactionRepository, Mockito.times(2))
                .findAllByIdIsNotInAndCreatedAtBetween(ArgumentMatchers.any(), ArgumentMatchers.any(),
                        ArgumentMatchers.any());

        Mockito.verify(cacheHelper, Mockito.times(2))
                .getAllTransactions(from, to);
    }

    @Test
    void cacheAndRepositoryAllMethods_ReturnNull_MockTest() {
        TransactionRepository transactionRepository = Mockito.mock(TransactionRepository.class);
        CacheHelper cacheHelper = Mockito.mock(CacheHelper.class);
        TransactionService transactionService = new TransactionService(transactionRepository, cacheHelper);

        LocalDateTime from = LocalDateTime.of(2023, 2, 26, 0, 0);
        LocalDateTime to = LocalDateTime.of(2023, 2, 28, 0, 0);

        Mockito.when(cacheHelper.getAllTransactions(ArgumentMatchers.any(),
                ArgumentMatchers.any())).thenReturn(null);

        Mockito.when(transactionRepository.findAllByCreatedAtIsBetween(ArgumentMatchers.any(),
                ArgumentMatchers.any())).thenReturn(null);

        List<TransactionEntity> listOne = transactionService.getAllTransactionByDate(from, to);
        List<TransactionEntity> listTwo = transactionService.getAllTransactionByDate(from, to);
        List<TransactionEntity> listThree = transactionService.getAllTransactionByDate(from, to);

        org.junit.jupiter.api.Assertions.assertEquals(listOne.size(), 0);
        org.junit.jupiter.api.Assertions.assertEquals(listTwo.size(), 0);
        org.junit.jupiter.api.Assertions.assertEquals(listThree.size(), 0);

        Mockito.verify(transactionRepository, Mockito.times(3))
                .findAllByCreatedAtIsBetween(ArgumentMatchers.any(), ArgumentMatchers.any());

        Mockito.verify(cacheHelper, Mockito.times(3))
                .getAllTransactions(ArgumentMatchers.any(), ArgumentMatchers.any());
    }

    @Test
    void cache_AddAllTransactionCacheMethod_MockTest() {
        TransactionRepository transactionRepository = Mockito.mock(TransactionRepository.class);
        TransactionService transactionService = new TransactionService(transactionRepository, cacheHelper);

        LocalDateTime from = LocalDateTime.of(2023, 3, 1, 0, 0);
        LocalDateTime to = LocalDateTime.of(2023, 3, 3, 0, 0);

        Mockito.when(transactionRepository.findAllByCreatedAtIsBetween(ArgumentMatchers.any(),
                ArgumentMatchers.any())).thenReturn(getTransactionList());

        List<Long> ids = getTransactionList().stream()
                .map(TransactionEntity::getId).toList();

        Assertions.assertThat(cacheHelper.cacheContainsAllKey(ids)).isFalse();

        List<TransactionEntity> listOne = transactionService.getAllTransactionByDate(from, to); // call 1

        Assertions.assertThat(cacheHelper.cacheContainsAllKey(ids)).isTrue();

        List<TransactionEntity> listTwo = transactionService.getAllTransactionByDate(from, to); // call 2

        org.junit.jupiter.api.Assertions.assertEquals(listOne.size(), 2);
        org.junit.jupiter.api.Assertions.assertEquals(listTwo.size(), 2);

        Mockito.verify(transactionRepository, Mockito.times(1))
                .findAllByCreatedAtIsBetween(ArgumentMatchers.any(), ArgumentMatchers.any());
    }

    @Test
    void getTransactionByIdMethod_ReturnNull_Test() {
        TransactionRepository transactionRepository = Mockito.mock(TransactionRepository.class);
        CacheHelper cacheHelper = Mockito.mock(CacheHelper.class);
        TransactionService transactionService = new TransactionService(transactionRepository, cacheHelper);

        Mockito.when(transactionRepository.findById(ArgumentMatchers.any())).thenReturn(Optional.empty());
        Mockito.when(cacheHelper.findById(ArgumentMatchers.any())).thenReturn(null);

        org.junit.jupiter.api.Assertions.assertThrows(RuntimeException.class,
                () -> transactionService.getTransactionById(1L));

        Mockito.verify(transactionRepository, Mockito.times(1)).findById(ArgumentMatchers.any());
        Mockito.verify(cacheHelper, Mockito.times(1)).findById(ArgumentMatchers.any());
    }

    @Test
    void getTransactionByIdMethod_CacheFindByIdReturnNull_Test() {
        TransactionRepository transactionRepository = Mockito.mock(TransactionRepository.class);
        CacheHelper cacheHelper = Mockito.mock(CacheHelper.class);
        TransactionService transactionService = new TransactionService(transactionRepository, cacheHelper);

        TransactionEntity transaction = getTransaction();

        Mockito.when(transactionRepository.findById(ArgumentMatchers.any())).thenReturn(Optional.of(transaction));
        Mockito.when(cacheHelper.findById(ArgumentMatchers.any())).thenReturn(null);

        TransactionEntity entity = transactionService.getTransactionById(1L);

        org.junit.jupiter.api.Assertions.assertEquals(entity.getId(), transaction.getId());
        org.junit.jupiter.api.Assertions.assertEquals(entity.getStatus(), transaction.getStatus());
        org.junit.jupiter.api.Assertions.assertEquals(entity.getAmount(), transaction.getAmount());
        org.junit.jupiter.api.Assertions.assertEquals(entity.getCreatedAt(), transaction.getCreatedAt());

        Mockito.verify(transactionRepository, Mockito.times(1)).findById(ArgumentMatchers.any());
        Mockito.verify(cacheHelper, Mockito.times(1)).findById(ArgumentMatchers.any());
    }

    @Test
    void getTransactionByIdMethod_RepositoryFindByIdReturnNull_Test() {
        CacheHelper cacheHelper = Mockito.mock(CacheHelper.class);
        TransactionService transactionService = new TransactionService(repository, cacheHelper);

        Mockito.when(cacheHelper.findById(ArgumentMatchers.any())).thenReturn(null);

        TransactionEntity transaction = repository.findById(1).get();

        TransactionEntity entity = transactionService.getTransactionById(1L);

        org.junit.jupiter.api.Assertions.assertEquals(entity.getId(), transaction.getId());
        org.junit.jupiter.api.Assertions.assertEquals(entity.getStatus(), transaction.getStatus());
        org.junit.jupiter.api.Assertions.assertEquals(entity.getAmount(), transaction.getAmount());
        org.junit.jupiter.api.Assertions.assertEquals(entity.getCreatedAt(), transaction.getCreatedAt());

        Mockito.verify(cacheHelper, Mockito.times(1)).findById(ArgumentMatchers.any());
    }

    private List<TransactionEntity> getTransactionList() {
        return List.of(
                new TransactionEntity(1L, new BigDecimal("589436584.4937"), Status.SUCCESS,
                        LocalDateTime.now().minusDays(1), LocalDateTime.now()),

                new TransactionEntity(2L, new BigDecimal("589436584.4937"), Status.SUCCESS,
                        LocalDateTime.now().minusDays(1), LocalDateTime.now())
        );
    }
    
    private TransactionEntity getTransaction() {
        return new TransactionEntity(1L, new BigDecimal("589436584.4937"), Status.SUCCESS,
                LocalDateTime.now().minusDays(1), LocalDateTime.now());
    }

}
