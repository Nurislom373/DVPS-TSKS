package org.khasanof.uicachingstrategy;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.khasanof.uicachingstrategy.domain.TransactionEntity;
import org.khasanof.uicachingstrategy.repository.TransactionRepository;
import org.khasanof.uicachingstrategy.service.ContextTransactionServices;
import org.khasanof.uicachingstrategy.service.GetTransactionsService;
import org.khasanof.uicachingstrategy.service.humo.HumoTransactionService;
import org.khasanof.uicachingstrategy.service.uzcard.UzCardTransactionService;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class UiCachingStrategyApplicationTests {

    @Autowired
    private TransactionRepository repository;

    @Autowired
    private ContextTransactionServices contextTransactionServices;

    @Test
    void test_onlyGetCache() {
        GetTransactionsService service = new GetTransactionsService(contextTransactionServices, repository);
        LocalDateTime from1 = LocalDateTime.of(2023, 2, 25, 0, 0);
        LocalDateTime to1 = LocalDateTime.of(2023, 2, 27, 0, 0);
        List<TransactionEntity> list = service.getAllTransactionByDate("9860996540334145", from1, to1);
        Assertions.assertEquals(list.size(), 4);
    }

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

        //
        Mockito.verify(transactionRepository, Mockito.times(3))
                .getCardCacheCount(ArgumentMatchers.any());

        Mockito.verify(transactionRepository, Mockito.times(1))
                .saveAll(ArgumentMatchers.any());

        Mockito.verify(humoTransactionService, Mockito.times(3))
                .getAllTransactionsByDates(ArgumentMatchers.any(), ArgumentMatchers.any(), ArgumentMatchers.any());
    }

}
