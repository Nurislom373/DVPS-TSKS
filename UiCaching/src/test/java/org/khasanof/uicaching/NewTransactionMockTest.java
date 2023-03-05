package org.khasanof.uicaching;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.khasanof.uicaching.config.CacheHelper;
import org.khasanof.uicaching.domain.TransactionEntity;
import org.khasanof.uicaching.enums.FromToEnum;
import org.khasanof.uicaching.enums.Status;
import org.khasanof.uicaching.repository.TransactionRepository;
import org.khasanof.uicaching.service.TransactionService;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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
public class NewTransactionMockTest {

    @Autowired
    private TransactionService service;

    @Autowired
    private TransactionRepository repository;

    @Autowired
    private CacheHelper cacheHelper;

    @Test
    void test_getAllTransactionByDate_severalCases() {
        TransactionRepository repository = Mockito.mock(TransactionRepository.class);
        CacheHelper cacheHelper = Mockito.mock(CacheHelper.class);
        TransactionService transactionService = new TransactionService(repository, cacheHelper);

        LocalDateTime from = LocalDateTime.of(2023, 2, 27, 0, 0);
        LocalDateTime to = LocalDateTime.of(2023, 3, 3, 0, 0);

        LocalDateTime fromTwo = LocalDateTime.of(2023, 2, 28, 0, 0);
        LocalDateTime toTwo = LocalDateTime.of(2023, 3, 2, 0, 0);

        LocalDateTime fromThree = LocalDateTime.of(2023, 2, 25, 0, 0);
        LocalDateTime toThree = LocalDateTime.of(2023, 3, 2, 15, 40);

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
                .thenReturn(getTransactions(timeMap.get(FromToEnum.FROM), toThree));

        Mockito.when(repository.findAllByCreatedAtIsBetween(ArgumentMatchers.any(), ArgumentMatchers.any()))
                .thenReturn(getTransactions(fromThree, timeMap.get(FromToEnum.FROM)));

        List<TransactionEntity> listThree = transactionService.getAllTransactionByDate(fromThree, toThree);


        // created_at between '2023-02-27 00:00:00' and '2023-03-03 00:00:00'
        Assertions.assertEquals(listOne.size(), 25);

        // created_at between '2023-02-28 00:00:00' and '2023-03-02 00:00:00'
        Assertions.assertEquals(listTwo.size(), 8);

        // created_at between '2023-02-25 00:00:00' and '2023-03-02 15:40:00'
        Assertions.assertEquals(listThree.size(), 44);


        // We can see how many times these 4 methods are called during our test run.
        Mockito.verify(cacheHelper, Mockito.times(3)).isEmpty();

        Mockito.verify(cacheHelper, Mockito.times(2)).getFromToDate();

        Mockito.verify(cacheHelper, Mockito.times(2))
                .getAllTransactions(ArgumentMatchers.any(), ArgumentMatchers.any());

        Mockito.verify(repository, Mockito.times(2))
                .findAllByCreatedAtIsBetween(ArgumentMatchers.any(), ArgumentMatchers.any());
    }

    @Test
    void newTest() {

        LocalDateTime from1 = LocalDateTime.of(2023, 2, 24, 0, 0);
        LocalDateTime to1 = LocalDateTime.of(2023, 2, 27, 0, 0);

        LocalDateTime from2 = LocalDateTime.of(2023, 2, 25, 20, 0);
        LocalDateTime to2 = LocalDateTime.of(2023, 3, 3, 0, 0);

        LocalDateTime from3 = LocalDateTime.of(2023, 2, 26, 0, 0);
        LocalDateTime to3 = LocalDateTime.of(2023, 3, 2, 15, 40);

        List<TransactionEntity> list1 = service.getAllTransactionByDate(from1, to1);
        List<TransactionEntity> list2 = service.getAllTransactionByDate(from2, to2);
        List<TransactionEntity> list3 = service.getAllTransactionByDate(from3, to3);

        System.out.println("list1.size() = " + list1.size());
        System.out.println("list2.size() = " + list2.size());
        System.out.println("list3.size() = " + list3.size());

        soutList(list1);
        System.out.println(" ------------------- ");
        soutList(list2);
        System.out.println(" ------------------- ");
        soutList(list3);
    }

    @Test
    void newSecondTest() {

        LocalDateTime from1 = LocalDateTime.of(2023, 2, 24, 0, 0);
        LocalDateTime to1 = LocalDateTime.of(2023, 2, 27, 0, 0);

        LocalDateTime from2 = LocalDateTime.of(2023, 2, 21, 0, 0);
        LocalDateTime to2 = LocalDateTime.of(2023, 2, 25, 0, 0);

        LocalDateTime from3 = LocalDateTime.of(2023, 2, 22, 0, 0);
        LocalDateTime to3 = LocalDateTime.of(2023, 3, 2, 15, 40);

        List<TransactionEntity> list1 = service.getAllTransactionByDate(from1, to1);
        List<TransactionEntity> list2 = service.getAllTransactionByDate(from2, to2);
        List<TransactionEntity> list3 = service.getAllTransactionByDate(from3, to3);

        System.out.println("list1.size() = " + list1.size());
        System.out.println("list2.size() = " + list2.size());
        System.out.println("list3.size() = " + list3.size());

        soutList(list1);
        System.out.println(" ------------------- ");
        soutList(list2);
        System.out.println(" ------------------- ");
        soutList(list3);
    }

    @Test
    void newThirdTest() {

        LocalDateTime from1 = LocalDateTime.of(2023, 2, 24, 0, 0);
        LocalDateTime to1 = LocalDateTime.of(2023, 2, 27, 0, 0);

        LocalDateTime from2 = LocalDateTime.of(2023, 2, 21, 0, 0);
        LocalDateTime to2 = LocalDateTime.of(2023, 2, 28, 0, 0);

        LocalDateTime from3 = LocalDateTime.of(2023, 2, 26, 0, 0);
        LocalDateTime to3 = LocalDateTime.of(2023, 2, 27, 0, 0);

        List<TransactionEntity> list1 = service.getAllTransactionByDate(from1, to1);
        List<TransactionEntity> list2 = service.getAllTransactionByDate(from2, to2);
        List<TransactionEntity> list3 = service.getAllTransactionByDate(from3, to3);

        System.out.println("list1.size() = " + list1.size());
        System.out.println("list2.size() = " + list2.size());
        System.out.println("list3.size() = " + list3.size());

        soutList(list1);
        System.out.println(" ------------------- ");
        soutList(list2);
        System.out.println(" ------------------- ");
        soutList(list3);
    }

    @Test
    void newFourtyTest() {
        LocalDateTime from1 = LocalDateTime.of(2023, 2, 22, 0, 0);
        LocalDateTime to1 = LocalDateTime.of(2023, 2, 25, 0, 0);

        LocalDateTime from2 = LocalDateTime.of(2023, 2, 26, 0, 0);
        LocalDateTime to2 = LocalDateTime.of(2023, 2, 27, 0, 0);

        LocalDateTime from3 = LocalDateTime.of(2023, 2, 20, 0, 0);
        LocalDateTime to3 = LocalDateTime.of(2023, 2, 22, 0, 0);

        LocalDateTime from4 = LocalDateTime.of(2023, 2, 23, 0, 0);
        LocalDateTime to4 = LocalDateTime.of(2023, 2, 28, 0, 0);

        LocalDateTime from5 = LocalDateTime.of(2023, 2, 19, 0, 0);
        LocalDateTime to5 = LocalDateTime.of(2023, 2, 25, 0, 0);

        LocalDateTime from6 = LocalDateTime.of(2023, 2, 23, 0, 0);
        LocalDateTime to6 = LocalDateTime.of(2023, 2, 27, 0, 0);

        LocalDateTime from7 = LocalDateTime.of(2023, 2, 19, 0, 0);
        LocalDateTime to7 = LocalDateTime.of(2023, 2, 28, 0, 0);

        service.getAllTransactionByDate(from1, to1);
        service.getAllTransactionByDate(from2, to2);
        service.getAllTransactionByDate(from3, to3);
        service.getAllTransactionByDate(from4, to4);
        service.getAllTransactionByDate(from5, to5);
        service.getAllTransactionByDate(from6, to6);
        service.getAllTransactionByDate(from7, to7);
    }

    public void soutList(List<?> list) {
        list.forEach(System.out::println);
    }

    public TransactionService getService(boolean mockRepository, boolean mockCacheHelper) {
        if (mockRepository && !mockCacheHelper) {
            return new TransactionService(Mockito.mock(TransactionRepository.class), cacheHelper);
        } else if (mockCacheHelper && !mockRepository) {
            return new TransactionService(repository, Mockito.mock(CacheHelper.class));
        } else {
            return service;
        }
    }

    public List<TransactionEntity> getTransactions(LocalDateTime from, LocalDateTime to) {
        return repository.findAllByCreatedAtIsBetween(from, to);
    }
}
