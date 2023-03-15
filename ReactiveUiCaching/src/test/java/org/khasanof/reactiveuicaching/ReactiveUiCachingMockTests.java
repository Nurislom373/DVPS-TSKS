package org.khasanof.reactiveuicaching;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.khasanof.reactiveuicaching.data.TransactionMockData;
import org.khasanof.reactiveuicaching.domain.TransactionEntity;
import org.khasanof.reactiveuicaching.enums.Status;
import org.khasanof.reactiveuicaching.repository.TransactionRepository;
import org.khasanof.reactiveuicaching.service.MainTransactionsService;
import org.khasanof.reactiveuicaching.service.TransactionService;
import org.khasanof.reactiveuicaching.service.context.AnnotationContextTransactionService;
import org.khasanof.reactiveuicaching.service.transactionServices.masterCard.MasterCardTransactionService;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author: Nurislom
 * <br/>
 * Date: 3/15/2023
 * <br/>
 * Time: 3:43 PM
 * <br/>
 * Package: org.khasanof.reactiveuicaching
 */
@SpringBootTest
public class ReactiveUiCachingMockTests {

    @Autowired
    private AnnotationContextTransactionService annotationContextTransactionService;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private TransactionMockData data;


    /*
        Cache - [] : Empty

        Call 1 - [2023-03-02, 2023-03-04] : {
            First : Get External Service from {Call-1-FromDate} to {Call-1-ToDate}
        }
     */
    @Test
    void test_shouldCacheIsNull() {
        var annotationContextTransactionService1 = Mockito.mock(AnnotationContextTransactionService.class);
        var transactionRepository1 = Mockito.mock(TransactionRepository.class);
        var masterCardTransactionService = Mockito.mock(MasterCardTransactionService.class);

        MainTransactionsService service = new MainTransactionsService(annotationContextTransactionService1,
                transactionRepository1);
        LocalDateTime from1 = LocalDateTime.of(2023, 3, 2, 0, 0);
        LocalDateTime to1 = LocalDateTime.of(2023, 3, 4, 0, 0);
        String cardNumber = "5425764309411081";

        Mockito.when(annotationContextTransactionService1.getService(ArgumentMatchers.any()))
                .thenReturn(masterCardTransactionService);

        Mockito.when(transactionRepository1.count()).thenReturn(Mono.just(0L));

        Mockito.when(masterCardTransactionService.getAllTransactionsByDates(ArgumentMatchers.any(), ArgumentMatchers.any(),
                ArgumentMatchers.any())).thenReturn(Flux.fromIterable(getTransactions(cardNumber, from1, to1)));

        Mockito.when(transactionRepository1.saveAll(ArgumentMatchers.anyCollection()))
                .thenReturn(Flux.fromIterable(getTransactions(cardNumber, from1, to1)));

        Mono<List<TransactionEntity>> mono = service.getAllTransactionsByCardAndDates(cardNumber, from1, to1);

        StepVerifier.create(mono)
                .assertNext(list -> {
                    Assertions.assertThat(list).isNotNull();
                    org.junit.jupiter.api.Assertions.assertEquals(list.size(), 2);
                })
                .expectComplete()
                .verify();

        Mockito.verify(transactionRepository1, Mockito.times(1))
                .count();

        Mockito.verify(transactionRepository1, Mockito.times(1))
                .saveAll(ArgumentMatchers.anyCollection());

        Mockito.verify(annotationContextTransactionService1, Mockito.times(1))
                .getService(ArgumentMatchers.any());

        Mockito.verify(masterCardTransactionService, Mockito.times(1))
                .getAllTransactionsByDates(ArgumentMatchers.any(), ArgumentMatchers.any(), ArgumentMatchers.any());
    }

    /*
        Cache - [2023-03-01, 2023-03-07]

        Call 1 - [2023-03-03, 2023-03-05] : {
            First : Get Cache from {Call-1-FromDate} to {Call-1-ToDate}
        }

        Call 2 - [2023-03-04, 2023-03-06] : {
            First : Get Cache from {Call-2-FromDate} to {Call-2-ToDate}
        }
     */
    @Test
    void test_CaseWhereThereIsACache() {
        var annotationContextTransactionService1 = Mockito.mock(AnnotationContextTransactionService.class);
        var transactionRepository1 = Mockito.mock(TransactionRepository.class);
        var masterCardTransactionService = Mockito.mock(MasterCardTransactionService.class);

        MainTransactionsService service = new MainTransactionsService(annotationContextTransactionService1,
                transactionRepository1);

        // Cache Range
        LocalDateTime from1 = LocalDateTime.of(2023, 3, 1, 0, 0);
        LocalDateTime to1 = LocalDateTime.of(2023, 3, 7, 0, 0);

        LocalDateTime from2 = LocalDateTime.of(2023, 3, 3, 0, 0);
        LocalDateTime to2 = LocalDateTime.of(2023, 3, 5, 0, 0);

        LocalDateTime from3 = LocalDateTime.of(2023, 3, 4, 0, 0);
        LocalDateTime to3 = LocalDateTime.of(2023, 3, 6, 0, 0);

        String cardNumber = "5425764309411081";

        // Mock
        Mockito.when(annotationContextTransactionService1.getService(ArgumentMatchers.any()))
                .thenReturn(masterCardTransactionService);

        Mockito.when(transactionRepository1.count()).thenReturn(Mono.just(1L));

        Mockito.when(transactionRepository1.getCardCacheCount(ArgumentMatchers.any()))
                        .thenReturn(Mono.just(2L));

        Mockito.when(transactionRepository1.findAll())
                        .thenReturn(Flux.fromIterable(getTransactions(cardNumber, from1, to1)));

        Mockito.when(transactionRepository1.findAllByQuery(ArgumentMatchers.any(), ArgumentMatchers.any(),
                ArgumentMatchers.any())).thenReturn(Flux.fromIterable(getTransactions(cardNumber, from1, to1)));

        // call 1
        Mono<List<TransactionEntity>> mono1 = service.getAllTransactionsByCardAndDates(cardNumber, from2, to2);

        // call 2
        Mono<List<TransactionEntity>> mono2 = service.getAllTransactionsByCardAndDates(cardNumber, from3, to3);

        StepVerifier.create(mono1)
                .assertNext(list -> {
                    Assertions.assertThat(list).isNotNull();
                    org.junit.jupiter.api.Assertions.assertEquals(list.size(), 2);
                })
                .expectComplete()
                .verify();

        StepVerifier.create(mono2)
                .assertNext(list -> {
                    Assertions.assertThat(list).isNotNull();
                    org.junit.jupiter.api.Assertions.assertEquals(list.size(), 2);
                })
                .expectComplete()
                .verify();

        Mockito.verify(transactionRepository1, Mockito.times(2))
                .count();

        Mockito.verify(transactionRepository1, Mockito.times(2))
                .findAll();

        Mockito.verify(annotationContextTransactionService1, Mockito.times(2))
                .getService(ArgumentMatchers.any());
    }

    /*
        Cache - [2023-03-04, 2023-03-07]

        Call 1 - [2023-03-02, 2023-03-06] : {
            First : Get Cache from {CacheFromDate} to {Call-1-ToDate}
            Second : Get External Service from {Call-1-FromDate} to {CacheFromDate}
        }

        Call 2 - [2023-03-06, 2023-03-09] : {
            First : Get Cache from {Call-2-FromDate} to {CacheToDate}
            Second : Get External Service from {CacheToDate} to {Call-2-ToDate}
        }
     */
    @Test
    void test_CaseRangeFromAndToGreaterThan() {
        var annotationContextTransactionService1 = Mockito.mock(AnnotationContextTransactionService.class);
        var transactionRepository1 = Mockito.mock(TransactionRepository.class);
        var masterCardTransactionService = Mockito.mock(MasterCardTransactionService.class);

        MainTransactionsService service = new MainTransactionsService(annotationContextTransactionService1,
                transactionRepository1);

        // Cache Range
        LocalDateTime from1 = LocalDateTime.of(2023, 3, 4, 0, 0);
        LocalDateTime to1 = LocalDateTime.of(2023, 3, 7, 0, 0);

        LocalDateTime from2 = LocalDateTime.of(2023, 3, 2, 0, 0);
        LocalDateTime to2 = LocalDateTime.of(2023, 3, 6, 0, 0);

        LocalDateTime from3 = LocalDateTime.of(2023, 3, 6, 0, 0);
        LocalDateTime to3 = LocalDateTime.of(2023, 3, 9, 0, 0);

        String cardNumber = "5425764309411081";

        // call 1
        Mockito.when(annotationContextTransactionService1.getService(ArgumentMatchers.any()))
                .thenReturn(masterCardTransactionService);

        Mockito.when(transactionRepository1.count()).thenReturn(Mono.just(1L));

        Mockito.when(transactionRepository1.getCardCacheCount(ArgumentMatchers.any()))
                .thenReturn(Mono.just(2L));

        Mockito.when(transactionRepository1.findAll())
                .thenReturn(Flux.fromIterable(getTransactions(cardNumber, from1, to1)));

        Mockito.when(transactionRepository1.findAllByQuery(ArgumentMatchers.any(), ArgumentMatchers.any(),
                ArgumentMatchers.any())).thenReturn(Flux.fromIterable(getTransactions(cardNumber, from1, to1)));

        List<TransactionEntity> list1 = getTransactionsWithService(cardNumber, 10);
        System.out.println("list1 = " + list1);

        Mockito.when(masterCardTransactionService.getAllTransactionsByDates(ArgumentMatchers.any(),
                ArgumentMatchers.any(), ArgumentMatchers.any()))
                .thenReturn(Flux.fromIterable(list1));

        Mockito.when(transactionRepository1.saveAll(ArgumentMatchers.anyCollection()))
                .thenReturn(Flux.fromIterable(list1));

        Mono<List<TransactionEntity>> listMono1 = service.getAllTransactionsByCardAndDates(cardNumber, from2, to2);

        // StepVerifiers
        StepVerifier.create(listMono1)
                .assertNext(list -> {
                    Assertions.assertThat(list).isNotNull();
                    org.junit.jupiter.api.Assertions.assertEquals(list.size(), 12);
                })
                .expectComplete()
                .verify();


        // call 2
        Mockito.when(transactionRepository1.findAll())
                .thenReturn(Flux.fromIterable(getTransactions(cardNumber, from2, to1)));

        Mockito.when(transactionRepository1.findAllByQuery(ArgumentMatchers.any(), ArgumentMatchers.any(),
                ArgumentMatchers.any())).thenReturn(Flux.fromIterable(getTransactions(cardNumber, from2, to1)));

        List<TransactionEntity> list2 = getTransactionsWithService(cardNumber, 15);
        System.out.println("list2 = " + list2);

        Mockito.when(masterCardTransactionService.getAllTransactionsByDates(ArgumentMatchers.any(),
                        ArgumentMatchers.any(), ArgumentMatchers.any()))
                .thenReturn(Flux.fromIterable(list2));

        Mockito.when(transactionRepository1.saveAll(ArgumentMatchers.anyCollection()))
                .thenReturn(Flux.fromIterable(list2));

        Mono<List<TransactionEntity>> listMono2 = service.getAllTransactionsByCardAndDates(cardNumber, from3, to3);

        // StepVerifier
        StepVerifier.create(listMono2)
                .assertNext(list -> {
                    Assertions.assertThat(list).isNotNull();
                    org.junit.jupiter.api.Assertions.assertEquals(list.size(), 17);
                })
                .expectComplete()
                .verify();


        // Method Invoke Verify
        // How many times these 7 methods are called during our test run.
        Mockito.verify(transactionRepository1, Mockito.times(2))
                .count();

        Mockito.verify(transactionRepository1, Mockito.times(2))
                .getCardCacheCount(ArgumentMatchers.any());

        Mockito.verify(transactionRepository1, Mockito.times(2))
                .findAll();

        Mockito.verify(transactionRepository1, Mockito.times(2))
                .findAllByQuery(ArgumentMatchers.any(), ArgumentMatchers.any(), ArgumentMatchers.any());

        Mockito.verify(transactionRepository1, Mockito.times(2))
                .saveAll(ArgumentMatchers.anyCollection());

        Mockito.verify(masterCardTransactionService, Mockito.times(2))
                .getAllTransactionsByDates(ArgumentMatchers.any(), ArgumentMatchers.any(), ArgumentMatchers.any());

        Mockito.verify(annotationContextTransactionService1, Mockito.times(2))
                .getService(ArgumentMatchers.any());
    }

    private List<TransactionEntity> getTransactions(String cardNumber, LocalDateTime from, LocalDateTime to) {
        return List.of(
                new TransactionEntity(1L, new BigDecimal("6783243.748352"), Status.SUCCESS,
                        "5425195488749646", cardNumber, from, LocalDateTime.now()),
                new TransactionEntity(2L, new BigDecimal("758943.748352"), Status.FAIL,
                        "5425195488749646", cardNumber, to, LocalDateTime.now())
        );
    }

    // Fixed
    private List<TransactionEntity> getTransactionsWithService(String card, int count) {
        Map<String, String> map = new HashMap<>() {{
           put("5425", "/data/mock_transactions_mastercard.json");
           put("9860", "/data/mock_transactions_humo.json");
           put("8600", "/data/mock_transactions_uzcard.json");
           put("4263", "/data/mock_transactions_visa.json");
        }};

        String cardSub = card.substring(0, 4);
        System.out.println("cardSub = " + cardSub);
        return data.getMockList(map.get(cardSub), cardSub)
                .stream().limit(count).toList();
    }

}
