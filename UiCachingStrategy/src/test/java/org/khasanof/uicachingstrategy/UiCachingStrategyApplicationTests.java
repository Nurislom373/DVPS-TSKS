package org.khasanof.uicachingstrategy;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.khasanof.uicachingstrategy.domain.TransactionEntity;
import org.khasanof.uicachingstrategy.enums.FromToEnum;
import org.khasanof.uicachingstrategy.enums.Status;
import org.khasanof.uicachingstrategy.repository.TransactionRepository;
import org.khasanof.uicachingstrategy.service.ContextTransactionServices;
import org.khasanof.uicachingstrategy.service.GetTransactionsService;
import org.khasanof.uicachingstrategy.service.humo.HumoTransactionService;
import org.khasanof.uicachingstrategy.service.uzcard.UzCardTransactionService;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@SpringBootTest
class UiCachingStrategyApplicationTests {

    @Autowired
    private TransactionRepository repository;

    @Autowired
    private ContextTransactionServices contextTransactionServices;

    @Autowired
    private UzCardTransactionService uzCardTransactionService;

    @Autowired
    private HumoTransactionService humoTransactionService;



    @Test
    void test_onlyGetCache() {
        GetTransactionsService service = new GetTransactionsService(contextTransactionServices, repository);
        LocalDateTime from1 = LocalDateTime.of(2023, 2, 25, 0, 0);
        LocalDateTime to1 = LocalDateTime.of(2023, 2, 27, 0, 0);
        List<TransactionEntity> list = service.getAllTransactionByDate("9860996540334145", from1, to1);
        Assertions.assertEquals(list.size(), 4);
    }

    /*
       Cache - [] : Empty

       Call 1 - [2023-02-25, 2023-02-27] : {
            First : Get External Service from {Call-1-FromDate} to {Call-1-ToDate}
            If the list returned from the external service is null or empty, an empty list is returned.
       }

       Call 2 - [2023-02-25, 2023-02-27] : {
            First : Get External Service from {Call-2-FromDate} to {Call-1-ToDate}
            If the list returned from the external service is null or empty, an empty list is returned.
       }

       Call 3 - [2023-02-25, 2023-02-27] : {
            First : Get External Service from {Call-3-FromDate} to {Call-3-ToDate}
            If the list returned from the external service is null or empty, an empty list is returned.
       }
     */
    @Test
    void test_CacheIsEmpty() {
        HumoTransactionService humoTransactionService = Mockito.mock(HumoTransactionService.class);
        TransactionRepository transactionRepository = Mockito.mock(TransactionRepository.class);
        ContextTransactionServices contextTransactionServices1 = new ContextTransactionServices(
                new UzCardTransactionService(), humoTransactionService);
        GetTransactionsService service = new GetTransactionsService(contextTransactionServices1, transactionRepository);

        LocalDateTime from1 = LocalDateTime.of(2023, 2, 25, 0, 0);
        LocalDateTime to1 = LocalDateTime.of(2023, 2, 27, 0, 0);
        String cardNumber = "9860996540334145";

        // call 1
        Mockito.when(transactionRepository.getCardCacheCount(ArgumentMatchers.any()))
                .thenReturn(0);

        Mockito.when(humoTransactionService.getAllTransactionsByDates(ArgumentMatchers.any(),
                ArgumentMatchers.any(), ArgumentMatchers.any())).thenReturn(
                repository.findAllByCreatedAtIsBetween("9860996540334145", from1, to1));

        List<TransactionEntity> list1 = service.getAllTransactionByDate(cardNumber, from1, to1);

        // call 2
        Mockito.when(humoTransactionService.getAllTransactionsByDates(ArgumentMatchers.any(),
                ArgumentMatchers.any(), ArgumentMatchers.any())).thenReturn(null);

        List<TransactionEntity> list2 = service.getAllTransactionByDate("9860996540334145", from1, to1);

        // call 3
        Mockito.when(humoTransactionService.getAllTransactionsByDates(ArgumentMatchers.any(),
                ArgumentMatchers.any(), ArgumentMatchers.any())).thenReturn(new ArrayList<>());

        List<TransactionEntity> list3 = service.getAllTransactionByDate("9860996540334145", from1, to1);

        // Asserts
        /*
            from_card = '9860996540334145' or to_card = '9860996540334145' and created_at between '2023-02-25 00:00:00' and '2023-02-27 00:00:00'
         */
        Assertions.assertEquals(list1.size(), 4);
        Assertions.assertEquals(list2.size(), 0);
        Assertions.assertEquals(list3.size(), 0);

        // How many times these 3 methods are called during our test run.
        Mockito.verify(transactionRepository, Mockito.times(3))
                .getCardCacheCount(ArgumentMatchers.any());

        Mockito.verify(transactionRepository, Mockito.times(1))
                .saveAll(ArgumentMatchers.any());

        Mockito.verify(humoTransactionService, Mockito.times(3))
                .getAllTransactionsByDates(ArgumentMatchers.any(), ArgumentMatchers.any(), ArgumentMatchers.any());
    }


    /*
       Cache - [2023-02-23, 2023-02-28]

       Call 1 - [2023-02-24, 2023-03-02] : {
            Second : Get Cache from {Call-1-FromDate} to {CacheToDate}
            First : Get External Service from {CacheToDate} to {Call-1-ToDate}
            If the list returned from the external service is null, the cache returns the returned list.
       }

       Call 2 - [2023-02-26, 2023-03-02] : {
            Second : Get Cache from {Call-1-FromDate} to {CacheToDate}
            First : Get External Service from {CacheToDate} to {Call-1-ToDate}
            If the list returned from the external service is null, the cache returns the returned list.
       }
     */
    @Test
    void test_ThereIsACache_ThereIsNotExternalService() {
        UzCardTransactionService transactionService = Mockito.mock(UzCardTransactionService.class);
        TransactionRepository transactionRepository = Mockito.mock(TransactionRepository.class);
        ContextTransactionServices contextTransactionServices1 = new ContextTransactionServices(transactionService,
                new HumoTransactionService());
        GetTransactionsService service = new GetTransactionsService(contextTransactionServices1, transactionRepository);

        String cardNumber = "8600937995190824"; // Card Number

        LocalDateTime from1 = LocalDateTime.of(2023, 2, 24, 0, 0);
        LocalDateTime to1 = LocalDateTime.of(2023, 3, 2, 0, 0);

        LocalDateTime from2 = LocalDateTime.of(2023, 2, 26, 0, 0);
        LocalDateTime to2 = LocalDateTime.of(2023, 3, 2, 0, 0);

        // call 1
        Mockito.when(transactionRepository.findAll())
                .thenReturn(getTEnitityList(cardNumber, 2, 23, 2, 28));

        Mockito.when(transactionRepository.getCardCacheCount(ArgumentMatchers.any()))
                .thenReturn(1);

        Mockito.when(transactionService.getAllTransactionsByDates(ArgumentMatchers.any(),
                ArgumentMatchers.any(), ArgumentMatchers.any())).thenReturn(new ArrayList<>());

        Mockito.when(transactionRepository.findAllByCreatedAtIsBetween(ArgumentMatchers.any(),
                        ArgumentMatchers.any(), ArgumentMatchers.any()))
                .thenReturn(repository.findAllByCreatedAtIsBetween(cardNumber, from1, to1));

        List<TransactionEntity> list1 = service.getAllTransactionByDate(cardNumber, from1, to1);

        // call 2
        Mockito.when(transactionService.getAllTransactionsByDates(ArgumentMatchers.any(),
                ArgumentMatchers.any(), ArgumentMatchers.any())).thenReturn(null);

        Mockito.when(transactionRepository.findAllByCreatedAtIsBetween(ArgumentMatchers.any(),
                        ArgumentMatchers.any(), ArgumentMatchers.any()))
                .thenReturn(repository.findAllByCreatedAtIsBetween(cardNumber, from2, to2));

        List<TransactionEntity> list2 = service.getAllTransactionByDate(cardNumber, from2, to2);

        // Asserts
        Assertions.assertEquals(list1.size(), 20);
        Assertions.assertEquals(list2.size(), 18);

        // How many times these 4 methods are called during our test run.
        Mockito.verify(transactionRepository, Mockito.times(2))
                .getCardCacheCount(ArgumentMatchers.any());

        Mockito.verify(transactionRepository, Mockito.times(0))
                .saveAll(ArgumentMatchers.any());

        Mockito.verify(transactionRepository, Mockito.times(2))
                .findAll();

        Mockito.verify(transactionService, Mockito.times(2))
                .getAllTransactionsByDates(ArgumentMatchers.any(), ArgumentMatchers.any(), ArgumentMatchers.any());
    }

    @Test
    void test_CasesInTheCacheRange() {
        UzCardTransactionService transactionService = Mockito.mock(UzCardTransactionService.class);
        TransactionRepository transactionRepository = Mockito.mock(TransactionRepository.class);
        ContextTransactionServices contextTransactionServices1 = new ContextTransactionServices(transactionService,
                new HumoTransactionService());
        GetTransactionsService service = new GetTransactionsService(contextTransactionServices1, transactionRepository);

        String cardNumber = "8600937995190824"; // Card Number

        LocalDateTime from1 = LocalDateTime.of(2023, 2, 22, 0, 0);
        LocalDateTime to1 = LocalDateTime.of(2023, 2, 26, 0, 0);

        LocalDateTime from2 = LocalDateTime.of(2023, 2, 26, 0, 0);
        LocalDateTime to2 = LocalDateTime.of(2023, 3, 2, 0, 0);

        // call 1
        Mockito.when(transactionRepository.getCardCacheCount(ArgumentMatchers.any()))
                .thenReturn(1);

        List<TransactionEntity> timeList1 = getTEnitityList(cardNumber, 2, 24, 2, 28);

        Mockito.when(transactionRepository.findAll())
                .thenReturn(timeList1);

        Mockito.when(transactionService.getAllTransactionsByDates(ArgumentMatchers.any(),
                ArgumentMatchers.any(), ArgumentMatchers.any())).thenReturn(getTransactionsFromTo(cardNumber,
                from1, timeList1.get(0).getCreatedAt()));

        Mockito.when(transactionRepository.findAllByCreatedAtIsBetween(ArgumentMatchers.any(),
                        ArgumentMatchers.any(), ArgumentMatchers.any()))
                .thenReturn(repository.findAllByCreatedAtIsBetween(cardNumber, timeList1.get(0).getCreatedAt(), to1));

        List<TransactionEntity> list1 = service.getAllTransactionByDate(cardNumber, from1, to1);

        // call 2
        List<TransactionEntity> timeList2 = getTEnitityList(cardNumber, 2, 22, 2, 28);

        Mockito.when(transactionRepository.findAll())
                .thenReturn(timeList2);

        Mockito.when(transactionService.getAllTransactionsByDates(ArgumentMatchers.any(),
                ArgumentMatchers.any(), ArgumentMatchers.any()))
                .thenReturn(getTransactionsFromTo(cardNumber, from2, timeList2.get(1).getCreatedAt()));

        Mockito.when(transactionRepository.findAllByCreatedAtIsBetween(ArgumentMatchers.any(),
                        ArgumentMatchers.any(), ArgumentMatchers.any()))
                .thenReturn(repository.findAllByCreatedAtIsBetween(cardNumber, timeList2.get(1).getCreatedAt(), to2));

        List<TransactionEntity> list2 = service.getAllTransactionByDate(cardNumber, from2, to2);

        // Asserts
        Assertions.assertEquals(list1.size(), 20);
        Assertions.assertEquals(list2.size(), 27);

        // How many times these 5 methods are called during our test run.
        Mockito.verify(transactionRepository, Mockito.times(2))
                .getCardCacheCount(ArgumentMatchers.any());

        Mockito.verify(transactionRepository, Mockito.times(2))
                .saveAll(ArgumentMatchers.any());

        Mockito.verify(transactionRepository, Mockito.times(2))
                .findAll();

        Mockito.verify(transactionRepository, Mockito.times(2))
                .findAllByCreatedAtIsBetween(ArgumentMatchers.any(), ArgumentMatchers.any(), ArgumentMatchers.any());

        Mockito.verify(transactionService, Mockito.times(2))
                .getAllTransactionsByDates(ArgumentMatchers.any(), ArgumentMatchers.any(), ArgumentMatchers.any());
    }

    private List<TransactionEntity> getTEnitityList(String cardNumber, int month1, int day1, int month2, int day2) {
        return List.of(
                new TransactionEntity(1L, new BigDecimal("56789302.647382"),
                        Status.SUCCESS, cardNumber, "9860880782348651",
                        LocalDateTime.of(2023, month1, day1, 0, 0), LocalDateTime.now()),

                new TransactionEntity(1L, new BigDecimal("56789302.647382"),
                        Status.SUCCESS, cardNumber, "9860880782348651",
                        LocalDateTime.of(2023, month2, day2, 0, 0), LocalDateTime.now())
        );
    }

    private List<TransactionEntity> getTransactionsFromTo(String cardNumber, LocalDateTime from, LocalDateTime to) {
        if (cardNumber.startsWith("8600")) {
            return uzCardTransactionService.getAllTransactionsByDates(cardNumber, from, to);
        } else {
            return humoTransactionService.getAllTransactionsByDates(cardNumber, from, to);
        }
    }
}
