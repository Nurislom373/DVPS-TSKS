package org.khasanof.uicaching;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.khasanof.uicaching.config.CacheHelper;
import org.khasanof.uicaching.domain.TransactionEntity;
import org.khasanof.uicaching.enums.FromToEnum;
import org.khasanof.uicaching.repository.TransactionRepository;
import org.khasanof.uicaching.service.TransactionService;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Author: Nurislom
 * <br/>
 * Date: 3/4/2023
 * <br/>
 * Time: 5:54 PM
 * <br/>
 * Package: org.khasanof.uicaching
 */
@SpringBootTest
public class TSGetAllTransactionByDateTest {

    @Autowired
    private TransactionService service;

    @Autowired
    private TransactionRepository repository;

    @Autowired
    private CacheHelper cacheHelper;

    /*
       Cache - [] : Empty

       Call 1 - [2023-02-27, 2023-03-03] : {
            First : Get External Service from {Call-1-FromDate} to {Call-1-ToDate}

            If External Service Return List is not null - Add cache
       }

       Cache - [2023-02-27, 2023-03-02]

       Call 2/3 - [2023-02-28, 2023-03-02] : {
            First : Get Cache from {Call-2/3-FromDate} to {Call-2/3-ToDate}
       }
     */
    @Test
    void test_ForTheCaseWhereThereIsACache() {
        TransactionRepository repository = Mockito.mock(TransactionRepository.class);
        CacheHelper cacheHelper = Mockito.mock(CacheHelper.class);
        TransactionService transactionService = new TransactionService(repository, cacheHelper);

        LocalDateTime from = LocalDateTime.of(2023, 2, 27, 0, 0);
        LocalDateTime to = LocalDateTime.of(2023, 3, 3, 0, 0);

        LocalDateTime fromTwo = LocalDateTime.of(2023, 2, 28, 0, 0);
        LocalDateTime toTwo = LocalDateTime.of(2023, 3, 2, 0, 0);

        // call 1
        Mockito.when(cacheHelper.isEmpty()).thenReturn(true);
        Mockito.when(repository.findAllByCreatedAtIsBetween(from, to)).thenReturn(getTransactions(from, to));

        List<TransactionEntity> listOne = transactionService.getAllTransactionByDate(from, to);

        // call 2
        Mockito.when(cacheHelper.isEmpty()).thenReturn(false);

        HashMap<FromToEnum, LocalDateTime> timeMap = new HashMap<>() {{
            put(FromToEnum.FROM, LocalDateTime.of(2023, 2, 27, 14, 7));
            put(FromToEnum.TO, LocalDateTime.of(2023, 3, 2, 17, 58));
        }};

        Mockito.when(cacheHelper.getFromToDate()).thenReturn(timeMap);

        Mockito.when(cacheHelper.getAllTransactions(ArgumentMatchers.any(), ArgumentMatchers.any()))
                .thenReturn(getTransactions(fromTwo, toTwo));

        List<TransactionEntity> listTwo = transactionService.getAllTransactionByDate(fromTwo, toTwo);

        // call 3
        Mockito.when(cacheHelper.getAllTransactions(ArgumentMatchers.any(), ArgumentMatchers.any()))
                .thenReturn(getTransactions(fromTwo, toTwo));

        List<TransactionEntity> listThree = transactionService.getAllTransactionByDate(fromTwo, toTwo);


        // Asserts
        // created_at between '2023-02-27 00:00:00' and '2023-03-03 00:00:00'
        Assertions.assertEquals(listOne.size(), 25);

        // created_at between '2023-02-28 00:00:00' and '2023-03-02 00:00:00'
        Assertions.assertEquals(listTwo.size(), listThree.size());


        // How many times these 4 methods are called during our test run.
        Mockito.verify(cacheHelper, Mockito.times(3)).isEmpty();

        Mockito.verify(cacheHelper, Mockito.times(2)).getFromToDate();

        Mockito.verify(cacheHelper, Mockito.times(2))
                .getAllTransactions(ArgumentMatchers.any(), ArgumentMatchers.any());

        Mockito.verify(repository, Mockito.times(1))
                .findAllByCreatedAtIsBetween(ArgumentMatchers.any(), ArgumentMatchers.any());
    }

    /*
       Cache - [] : Cache Is Empty Test

       Call 1 - [2023-02-24, 2023-02-27] : {
            First : Get External Service from {Call-1-FromDate} to {Call-1-ToDate}
       }

       Call 2 - [2023-02-25, 2023-03-03] : {
            First : Get External Service from {Call-2-FromDate} to {Call-2-ToDate}

            If External Service Return List is not null - Add cache
       }

       Call 3 - [2023-02-26, 2023-03-02] : {
            First : Get External Service from {Call-2-FromDate} to {Call-2-ToDate}

            If External Service Return List is not null - Add cache
       }
     */
    @Test
    void test_WhenCacheHelperCalled_IsEmptyMethod_ReturnTrue() {
        TransactionRepository repository = Mockito.mock(TransactionRepository.class);
        CacheHelper cacheHelper = Mockito.mock(CacheHelper.class);
        TransactionService transactionService = new TransactionService(repository, cacheHelper);

        LocalDateTime from1 = LocalDateTime.of(2023, 2, 24, 0, 0);
        LocalDateTime to1 = LocalDateTime.of(2023, 2, 27, 0, 0);

        LocalDateTime from2 = LocalDateTime.of(2023, 2, 25, 20, 0);
        LocalDateTime to2 = LocalDateTime.of(2023, 3, 3, 0, 0);

        LocalDateTime from3 = LocalDateTime.of(2023, 2, 26, 0, 0);
        LocalDateTime to3 = LocalDateTime.of(2023, 3, 2, 15, 40);

        // call 1
        Mockito.when(cacheHelper.isEmpty()).thenReturn(true);
        Mockito.when(repository.findAllByCreatedAtIsBetween(from1, to1)).thenReturn(getTransactions(from1, to1));

        List<TransactionEntity> list1 = transactionService.getAllTransactionByDate(from1, to1);

        // call 2
        Mockito.when(repository.findAllByCreatedAtIsBetween(from2, to2)).thenReturn(null);

        // External Service Return Null
        List<TransactionEntity> list2 = transactionService.getAllTransactionByDate(from2, to2);

        // call 3
        Mockito.when(repository.findAllByCreatedAtIsBetween(from3, to3)).thenReturn(new ArrayList<>());

        // External Service Return Empty List
        List<TransactionEntity> list3 = transactionService.getAllTransactionByDate(from3, to3);

        // Asserts
        Assertions.assertEquals(list1.size(), 41);
        Assertions.assertEquals(list2.size(), 0);
        Assertions.assertEquals(list3.size(), 0);

        // How many times these 3 methods are called during our test run.
        Mockito.verify(cacheHelper, Mockito.times(3)).isEmpty();
        Mockito.verify(cacheHelper, Mockito.times(1))
                .addAllTransactionCache(ArgumentMatchers.any());
        Mockito.verify(repository, Mockito.times(3))
                .findAllByCreatedAtIsBetween(ArgumentMatchers.any(), ArgumentMatchers.any());
    }

    /*
       Cache - [2023-02-20, 2023-02-25]

       Call 1 - [2023-02-23, 2023-03-02] : {
            Second : Get Cache from {Call-1-FromDate} to {CacheToDate}
            First : Get External Service from {CacheToDate} to {Call-1-ToDate}

            If the list returned from the external service is null, the cache returns the returned list.
       }

       Call 2 - [2023-02-24, 2023-03-02] : {
            Second : Get Cache from {Call-1-FromDate} to {CacheToDate}
            First : Get External Service from {CacheToDate} to {Call-1-ToDate}

            If the list returned from the external service is null, the cache returns the returned list.
       }
     */
    @Test
    void test_ThereIsACache_ThereIsNotExternalService() {
        TransactionRepository repository = Mockito.mock(TransactionRepository.class);
        CacheHelper cacheHelper = Mockito.mock(CacheHelper.class);
        TransactionService transactionService = new TransactionService(repository, cacheHelper);

        // There is a cache in this area
        LocalDateTime from1 = LocalDateTime.of(2023, 2, 20, 0, 0);
        LocalDateTime to1 = LocalDateTime.of(2023, 2, 25, 0, 0);

        LocalDateTime from2 = LocalDateTime.of(2023, 2, 23, 0, 0);
        LocalDateTime to2 = LocalDateTime.of(2023, 3, 2, 0, 0);

        LocalDateTime from3 = LocalDateTime.of(2023, 2, 24, 0, 0);
        LocalDateTime to3 = LocalDateTime.of(2023, 3, 2, 0, 0);

        // Mock
        Mockito.when(cacheHelper.isEmpty()).thenReturn(false);

        HashMap<FromToEnum, LocalDateTime> timeMap = new HashMap<>() {{
            put(FromToEnum.FROM, LocalDateTime.of(2023, 2, 20, 0, 0));
            put(FromToEnum.TO, LocalDateTime.of(2023, 2, 24, 14, 0));
        }};

        Mockito.when(cacheHelper.getFromToDate()).thenReturn(timeMap);

        Mockito.when(repository.findAllByCreatedAtIsBetween(ArgumentMatchers.any(), ArgumentMatchers.any()))
                .thenReturn(null);

        // call 1
        Mockito.when(cacheHelper.getAllTransactions(ArgumentMatchers.any(), ArgumentMatchers.any()))
                .thenReturn(getTransactions(from2, to1));

        List<TransactionEntity> list1 = transactionService.getAllTransactionByDate(from2, to2);

        // call 2
        Mockito.when(cacheHelper.getAllTransactions(ArgumentMatchers.any(), ArgumentMatchers.any()))
                .thenReturn(getTransactions(from3, to1));

        List<TransactionEntity> list2 = transactionService.getAllTransactionByDate(from3, to3);


        // Asserts
        Assertions.assertEquals(list1.size(), 29);
        Assertions.assertEquals(list2.size(), 16);

        // How many times these 4 methods are called during our test run.
        Mockito.verify(cacheHelper, Mockito.times(2)).isEmpty();

        Mockito.verify(cacheHelper, Mockito.times(0))
                .addAllTransactionCache(ArgumentMatchers.any());

        Mockito.verify(cacheHelper, Mockito.times(2))
                .getAllTransactions(ArgumentMatchers.any(), ArgumentMatchers.any());

        Mockito.verify(repository, Mockito.times(2))
                .findAllByCreatedAtIsBetween(ArgumentMatchers.any(), ArgumentMatchers.any());
    }

    /*
       Cache - [2023-02-24, 2023-02-27]

       Call 1 - [2023-02-21, 2023-02-25] : {
            First : Get Cache from {CacheFromDate} to {Call-1-ToDate}
            Second : Get External Service from {Call-1-FromDate} to {CacheToDate}
       }

       Call 2 - [2023-02-22, 2023-03-02] : {
            First : Get Cache from {Call-2-FromDate} to {CacheToDate}
            Second : Get External Service from {CacheToDate} to {Call-2-ToDate}
       }
     */
    @Test
    void test_FromAndToDatesOneOfTheTwo_BetweenCache_Cases() {
        TransactionRepository repository = Mockito.mock(TransactionRepository.class);
        CacheHelper cacheHelper = Mockito.mock(CacheHelper.class);
        TransactionService transactionService = new TransactionService(repository, cacheHelper);

        // There is a cache in this area
        LocalDateTime from1 = LocalDateTime.of(2023, 2, 24, 0, 0);
        LocalDateTime to1 = LocalDateTime.of(2023, 2, 27, 0, 0);

        LocalDateTime from2 = LocalDateTime.of(2023, 2, 21, 0, 0);
        LocalDateTime to2 = LocalDateTime.of(2023, 2, 25, 0, 0);

        LocalDateTime from3 = LocalDateTime.of(2023, 2, 22, 0, 0);
        LocalDateTime to3 = LocalDateTime.of(2023, 3, 2, 15, 40);

        // call 1
        Mockito.when(cacheHelper.isEmpty()).thenReturn(false);

        HashMap<FromToEnum, LocalDateTime> timeMap = new HashMap<>() {{
            put(FromToEnum.FROM, LocalDateTime.of(2023, 2, 24, 0, 0));
            put(FromToEnum.TO, LocalDateTime.of(2023, 2, 26, 14, 8));
        }};

        Mockito.when(cacheHelper.getFromToDate()).thenReturn(timeMap);

        Mockito.when(cacheHelper.getAllTransactions(ArgumentMatchers.any(), ArgumentMatchers.any()))
                .thenReturn(getTransactions(timeMap.get(FromToEnum.FROM), to2));

        Mockito.when(repository.findAllByCreatedAtIsBetween(ArgumentMatchers.any(), ArgumentMatchers.any()))
                .thenReturn(getTransactions(from2, timeMap.get(FromToEnum.FROM)));

        List<TransactionEntity> list1 = transactionService.getAllTransactionByDate(from2, to2);

        // call 2
        HashMap<FromToEnum, LocalDateTime> timeMap2 = new HashMap<>() {{
            put(FromToEnum.FROM, LocalDateTime.of(2023, 2, 21, 0, 0));
            put(FromToEnum.TO, LocalDateTime.of(2023, 2, 26, 14, 8));
        }};

        Mockito.when(cacheHelper.getFromToDate()).thenReturn(timeMap2);

        Mockito.when(cacheHelper.getAllTransactions(ArgumentMatchers.any(), ArgumentMatchers.any()))
                .thenReturn(getTransactions(from3, timeMap2.get(FromToEnum.TO)));

        Mockito.when(repository.findAllByCreatedAtIsBetween(ArgumentMatchers.any(), ArgumentMatchers.any()))
                .thenReturn(getTransactions(timeMap2.get(FromToEnum.TO), to3));

        List<TransactionEntity> list2 = transactionService.getAllTransactionByDate(from3, to3);

        // Asserts
        Assertions.assertEquals(list1.size(), 44);
        Assertions.assertEquals(list2.size(), 83);

        // How many times these 4 methods are called during our test run.
        Mockito.verify(cacheHelper, Mockito.times(2)).isEmpty();
        Mockito.verify(cacheHelper, Mockito.times(2))
                .addAllTransactionCache(ArgumentMatchers.any());
        Mockito.verify(cacheHelper, Mockito.times(2))
                .getAllTransactions(ArgumentMatchers.any(), ArgumentMatchers.any());
        Mockito.verify(repository, Mockito.times(2))
                .findAllByCreatedAtIsBetween(ArgumentMatchers.any(), ArgumentMatchers.any());
    }

    /*
       Cache - [2023-02-24, 2023-02-27]

       Call 1 - [2023-02-21, 2023-02-28] : {
            First : Get Cache from {Call-1-FromDate} to {CacheToDate}
            Second : Get External Service from {CacheToDate} to {Call-1-ToDate}

            If External service return list is not null - Add cache
       }
       Call 2 - [2023-02-26, 2023-02-27] : Get Cache And Return.
     */
    @Test
    void test_FromAndToDates_GreaterThanCacheFromAndToDates_Cases() {
        TransactionRepository repository = Mockito.mock(TransactionRepository.class);
        CacheHelper cacheHelper = Mockito.mock(CacheHelper.class);
        TransactionService transactionService = new TransactionService(repository, cacheHelper);

        // There is a cache in this area
        LocalDateTime from1 = LocalDateTime.of(2023, 2, 24, 0, 0);
        LocalDateTime to1 = LocalDateTime.of(2023, 2, 27, 0, 0);

        LocalDateTime from2 = LocalDateTime.of(2023, 2, 21, 0, 0);
        LocalDateTime to2 = LocalDateTime.of(2023, 2, 28, 0, 0);

        LocalDateTime from3 = LocalDateTime.of(2023, 2, 26, 0, 0);
        LocalDateTime to3 = LocalDateTime.of(2023, 2, 27, 0, 0);

        // call 1
        HashMap<FromToEnum, LocalDateTime> timeMap1 = new HashMap<>() {{
            put(FromToEnum.FROM, LocalDateTime.of(2023, 2, 24, 0, 0));
            put(FromToEnum.TO, LocalDateTime.of(2023, 2, 26, 14, 8));
        }};

        Mockito.when(cacheHelper.isEmpty()).thenReturn(false);
        Mockito.when(cacheHelper.getFromToDate()).thenReturn(timeMap1);
        Mockito.when(cacheHelper.getAllTransactions(ArgumentMatchers.any(), ArgumentMatchers.any()))
                .thenReturn(getTransactions(timeMap1.get(FromToEnum.FROM), timeMap1.get(FromToEnum.TO)));

        Mockito.when(repository.findAllByCreatedAtIsBetween(from2,
                timeMap1.get(FromToEnum.FROM))).thenReturn(getTransactions(from2, timeMap1.get(FromToEnum.FROM)));

        Mockito.when(repository.findAllByCreatedAtIsBetween(timeMap1.get(FromToEnum.TO),
                to2)).thenReturn(getTransactions(timeMap1.get(FromToEnum.TO), to2));

        List<TransactionEntity> list1 = transactionService.getAllTransactionByDate(from2, to2);

        // call 2
        HashMap<FromToEnum, LocalDateTime> timeMap2 = new HashMap<>() {{
            put(FromToEnum.FROM, LocalDateTime.of(2023, 2, 21, 14, 0));
            put(FromToEnum.TO, LocalDateTime.of(2023, 2, 27, 14, 8));
        }};

        Mockito.when(cacheHelper.getFromToDate()).thenReturn(timeMap2);
        Mockito.when(cacheHelper.getAllTransactions(ArgumentMatchers.any(), ArgumentMatchers.any()))
                .thenReturn(getTransactions(from3, to3));

        List<TransactionEntity> list2 = transactionService.getAllTransactionByDate(from3, to3);

        // Asserts
        Assertions.assertEquals(list1.size(), 77);
        Assertions.assertEquals(list2.size(), 11);

        // How many times these 4 methods are called during our test run.
        Mockito.verify(cacheHelper, Mockito.times(2)).isEmpty();
        Mockito.verify(cacheHelper, Mockito.times(2))
                .addAllTransactionCache(ArgumentMatchers.any());
        Mockito.verify(cacheHelper, Mockito.times(2))
                .getAllTransactions(ArgumentMatchers.any(), ArgumentMatchers.any());
        Mockito.verify(repository, Mockito.times(2))
                .findAllByCreatedAtIsBetween(ArgumentMatchers.any(), ArgumentMatchers.any());
    }

    /*
       Cache - [] : empty

       Call 1 - [2023-02-23, 2023-02-25] : Get External Service and Add Cache
       Call 2 - [2023-02-19, 2023-02-21] : Get External Service and Add Cache

       Cache - [2023-02-20T14:07:00, 2023-02-24T14:08:00]

       Call 3 - [2023-02-20T:00:00:00, 2023-02-24T:00:00:00] : {
            First : Get Cache from {CacheFromDate}  to {Call3ToDate}
            Second : Get External Service from {Call3FromDate} to {CacheFromDate}
       }
     */
    @Test
    void test_CacheIsEmpty_And_AddCacheTransaction_And_GetTransactionCache() {
        TransactionRepository repository = Mockito.mock(TransactionRepository.class);
        CacheHelper cacheHelper = Mockito.mock(CacheHelper.class);
        TransactionService transactionService = new TransactionService(repository, cacheHelper);

        LocalDateTime from1 = LocalDateTime.of(2023, 2, 23, 0, 0);
        LocalDateTime to1 = LocalDateTime.of(2023, 2, 25, 0, 0);

        LocalDateTime from2 = LocalDateTime.of(2023, 2, 19, 0, 0);
        LocalDateTime to2 = LocalDateTime.of(2023, 2, 21, 0, 0);

        LocalDateTime from3 = LocalDateTime.of(2023, 2, 20, 0, 0);
        LocalDateTime to3 = LocalDateTime.of(2023, 2, 24, 0, 0);

        // call 1
        Mockito.when(cacheHelper.isEmpty()).thenReturn(true);
        Mockito.when(repository.findAllByCreatedAtIsBetween(ArgumentMatchers.any(), ArgumentMatchers.any()))
                .thenReturn(getTransactions(from1, to1));

        List<TransactionEntity> list1 = transactionService.getAllTransactionByDate(from1, to1);

        // call 2
        Mockito.when(cacheHelper.isEmpty()).thenReturn(false);

        HashMap<FromToEnum, LocalDateTime> timeMap1 = new HashMap<>() {{
            put(FromToEnum.FROM, LocalDateTime.of(2023, 2, 23, 14, 7));
            put(FromToEnum.TO, LocalDateTime.of(2023, 2, 24, 14, 8));
        }};

        Mockito.when(cacheHelper.getFromToDate()).thenReturn(timeMap1);

        Mockito.when(repository.findAllByCreatedAtIsBetween(ArgumentMatchers.any(), ArgumentMatchers.any()))
                .thenReturn(getTransactions(from2, timeMap1.get(FromToEnum.FROM)));

        Mockito.when(cacheHelper.getAllTransactions(ArgumentMatchers.any(), ArgumentMatchers.any()))
                .thenReturn(getTransactions(from2, to2));

        List<TransactionEntity> list2 = transactionService.getAllTransactionByDate(from2, to2);

        // call 3
        HashMap<FromToEnum, LocalDateTime> timeMap2 = new HashMap<>() {{
            put(FromToEnum.FROM, LocalDateTime.of(2023, 2, 20, 14, 7));
            put(FromToEnum.TO, LocalDateTime.of(2023, 2, 24, 14, 8));
        }};

        Mockito.when(cacheHelper.getFromToDate()).thenReturn(timeMap2);

        Mockito.when(cacheHelper.getAllTransactions(ArgumentMatchers.any(), ArgumentMatchers.any()))
                .thenReturn(getTransactions(timeMap2.get(FromToEnum.FROM), to3));

        Mockito.when(repository.findAllByCreatedAtIsBetween(ArgumentMatchers.any(), ArgumentMatchers.any()))
                .thenReturn(getTransactions(from3, timeMap2.get(FromToEnum.FROM)));

        List<TransactionEntity> list3 = transactionService.getAllTransactionByDate(from3, to3);

        // Asserts
        Assertions.assertEquals(list1.size(), 29);
        Assertions.assertEquals(list2.size(), 15);
        Assertions.assertEquals(list3.size(), 43);

        // How many times these 4 methods are called during our test run.
        Mockito.verify(cacheHelper, Mockito.times(3)).isEmpty();
        Mockito.verify(cacheHelper, Mockito.times(2))
                .addAllTransactionCache(ArgumentMatchers.any());
        Mockito.verify(cacheHelper, Mockito.times(2))
                .getAllTransactions(ArgumentMatchers.any(), ArgumentMatchers.any());
        Mockito.verify(repository, Mockito.times(3))
                .findAllByCreatedAtIsBetween(ArgumentMatchers.any(), ArgumentMatchers.any());
    }

    /*
       Cache - [2023-02-23, 2023-02-25]

       Call 1 - [2023-02-28, 2023-03-02] : {
            Get External Service and Add cache
       }

       Call 2 - [2023-02-19, 2023-02-21] : {
            Get External Service and Add cache
       }

       Call 3 - [2023-02-22T:00:00:00, 2023-02-28T:00:00:00] : {
            First : Get Cache from {Call-3-FromDate}  to {Call-3-ToDate}
       }
     */
    @Test
    void test_ForFromAndDates_CaseWhereTheCacheIsNotAll() {
        TransactionRepository repository = Mockito.mock(TransactionRepository.class);
        CacheHelper cacheHelper = Mockito.mock(CacheHelper.class);
        TransactionService transactionService = new TransactionService(repository, cacheHelper);

        LocalDateTime from1 = LocalDateTime.of(2023, 2, 28, 0, 0);
        LocalDateTime to1 = LocalDateTime.of(2023, 3, 2, 0, 0);

        LocalDateTime from2 = LocalDateTime.of(2023, 2, 19, 0, 0);
        LocalDateTime to2 = LocalDateTime.of(2023, 2, 21, 0, 0);

        LocalDateTime from3 = LocalDateTime.of(2023, 2, 22, 0, 0);
        LocalDateTime to3 = LocalDateTime.of(2023, 2, 28, 0, 0);

        // call 1
        Mockito.when(cacheHelper.isEmpty()).thenReturn(false);

        HashMap<FromToEnum, LocalDateTime> timeMap1 = new HashMap<>() {{
            put(FromToEnum.FROM, LocalDateTime.of(2023, 2, 23, 0, 0));
            put(FromToEnum.TO, LocalDateTime.of(2023, 2, 25, 0, 0));
        }};
        Mockito.when(cacheHelper.getFromToDate()).thenReturn(timeMap1);

        Mockito.when(repository.findAllByCreatedAtIsBetween(ArgumentMatchers.any(), ArgumentMatchers.any()))
                .thenReturn(getTransactions(timeMap1.get(FromToEnum.TO), to1));

        Mockito.when(cacheHelper.getAllTransactions(ArgumentMatchers.any(), ArgumentMatchers.any()))
                .thenReturn(getTransactions(from1, to1));

        List<TransactionEntity> list1 = transactionService.getAllTransactionByDate(from1, to1);

        // call 2
        HashMap<FromToEnum, LocalDateTime> timeMap2 = new HashMap<>() {{
            put(FromToEnum.FROM, LocalDateTime.of(2023, 2, 23, 14, 7));
            put(FromToEnum.TO, LocalDateTime.of(2023, 2, 28, 14, 8));
        }};

        Mockito.when(cacheHelper.getFromToDate()).thenReturn(timeMap2);

        Mockito.when(repository.findAllByCreatedAtIsBetween(ArgumentMatchers.any(), ArgumentMatchers.any()))
                .thenReturn(getTransactions(from2, timeMap2.get(FromToEnum.FROM)));

        Mockito.when(cacheHelper.getAllTransactions(ArgumentMatchers.any(), ArgumentMatchers.any()))
                .thenReturn(getTransactions(from2, to2));

        List<TransactionEntity> list2 = transactionService.getAllTransactionByDate(from2, to2);

        // call 3
        HashMap<FromToEnum, LocalDateTime> timeMap3 = new HashMap<>() {{
            put(FromToEnum.FROM, LocalDateTime.of(2023, 2, 20, 14, 7));
            put(FromToEnum.TO, LocalDateTime.of(2023, 2, 28, 14, 8));
        }};

        Mockito.when(cacheHelper.getFromToDate()).thenReturn(timeMap3);

        Mockito.when(cacheHelper.getAllTransactions(ArgumentMatchers.any(), ArgumentMatchers.any()))
                .thenReturn(getTransactions(from3, to3));

        List<TransactionEntity> list3 = transactionService.getAllTransactionByDate(from3, to3);

        // Asserts
        Assertions.assertEquals(list1.size(), 8);
        Assertions.assertEquals(list2.size(), 15);
        Assertions.assertEquals(list3.size(), 72);

        // How many times these 4 methods are called during our test run.
        Mockito.verify(cacheHelper, Mockito.times(3)).isEmpty();
        Mockito.verify(cacheHelper, Mockito.times(2))
                .addAllTransactionCache(ArgumentMatchers.any());
        Mockito.verify(cacheHelper, Mockito.times(3))
                .getAllTransactions(ArgumentMatchers.any(), ArgumentMatchers.any());
        Mockito.verify(repository, Mockito.times(2))
                .findAllByCreatedAtIsBetween(ArgumentMatchers.any(), ArgumentMatchers.any());
    }

    @Test
    void test_CaseWhereCacheIsItself() {
        LocalDateTime from1 = LocalDateTime.of(2023, 2, 23, 0, 0);
        LocalDateTime to1 = LocalDateTime.of(2023, 2, 25, 0, 0);

        LocalDateTime from2 = LocalDateTime.of(2023, 2, 27, 0, 0);
        LocalDateTime to2 = LocalDateTime.of(2023, 2, 28, 0, 0);

        LocalDateTime from3 = LocalDateTime.of(2023, 2, 24, 0, 0);
        LocalDateTime to3 = LocalDateTime.of(2023, 2, 27, 0, 0);

        service.getAllTransactionByDate(from1, to1);
        service.getAllTransactionByDate(from2, to2);
        List<TransactionEntity> list = service.getAllTransactionByDate(from3, to3);

        Assertions.assertEquals(list.size(), 41);
    }

    public List<TransactionEntity> getTransactions(LocalDateTime from, LocalDateTime to) {
        return repository.findAllByCreatedAtIsBetween(from, to);
    }
}
