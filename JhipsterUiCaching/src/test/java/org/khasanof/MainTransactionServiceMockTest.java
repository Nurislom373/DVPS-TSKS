package org.khasanof;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.khasanof.data.TransactionMockData;
import org.khasanof.domain.transaction.Transaction;
import org.khasanof.dto.transaction.TransactionCardGetDTO;
import org.khasanof.enums.Status;
import org.khasanof.repository.TransactionRepository;
import org.khasanof.service.v2.SpringMainTransactionsService;
import org.khasanof.service.v2.context.AnnotationContextTransactionService;
import org.khasanof.service.v2.context.SpringFieldContextTransactionService;
import org.khasanof.service.v2.transactionServices.masterCard.MasterCardTransactionService;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
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
 * Date: 3/16/2023
 * <br/>
 * Time: 11:02 PM
 * <br/>
 * Package: org.khasanof
 */
@SpringBootTest
@ActiveProfiles("dev")
public class MainTransactionServiceMockTest {

    @Autowired
    private TransactionMockData data;

    @Autowired
    private SpringMainTransactionsService mainTransactionsService;

    @Test
    void test_getTransaction() {
        LocalDateTime from1 = LocalDateTime.of(2023, 3, 2, 0, 0);
        LocalDateTime to1 = LocalDateTime.of(2023, 3, 4, 0, 0);
        Mono<List<Transaction>> mono = mainTransactionsService.getAllTransactionsByCardAndDates(
            new TransactionCardGetDTO("5425764309411081", from1, to1));

        StepVerifier.create(mono)
            .assertNext(list -> {
                Assertions.assertThat(list).isNotNull();
            })
            .expectComplete()
            .verify();
    }

    /*
        Cache - [] : Empty

        Call 1 - [2023-03-02, 2023-03-04] : {
            First : Get External Service from {Call-1-FromDate} to {Call-1-ToDate}
        }
     */
    @Test
    void test_shouldCacheIsNull() {
        var annotationContextTransactionService1 = Mockito.mock(SpringFieldContextTransactionService.class);
        var transactionRepository1 = Mockito.mock(TransactionRepository.class);
        var masterCardTransactionService = Mockito.mock(MasterCardTransactionService.class);

        SpringMainTransactionsService service = new SpringMainTransactionsService(annotationContextTransactionService1,
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

        Mono<List<Transaction>> mono = service.getAllTransactionsByCardAndDates(
            new TransactionCardGetDTO(cardNumber, from1, to1));

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

        SpringMainTransactionsService service = new SpringMainTransactionsService(annotationContextTransactionService1,
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
        Mono<List<Transaction>> mono1 = service.getAllTransactionsByCardAndDates(new TransactionCardGetDTO(
            cardNumber, from2, to2));

        // call 2
        Mono<List<Transaction>> mono2 = service.getAllTransactionsByCardAndDates(new TransactionCardGetDTO(
            cardNumber, from3, to3));

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

        SpringMainTransactionsService service = new SpringMainTransactionsService(annotationContextTransactionService1,
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

        List<Transaction> list1 = getTransactionsWithService(cardNumber, 10);
        System.out.println("list1 = " + list1);

        Mockito.when(masterCardTransactionService.getAllTransactionsByDates(ArgumentMatchers.any(),
                ArgumentMatchers.any(), ArgumentMatchers.any()))
            .thenReturn(Flux.fromIterable(list1));

        Mockito.when(transactionRepository1.saveAll(ArgumentMatchers.anyCollection()))
            .thenReturn(Flux.fromIterable(list1));

        Mono<List<Transaction>> listMono1 = service.getAllTransactionsByCardAndDates(
            new TransactionCardGetDTO(cardNumber, from2, to2));

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

        List<Transaction> list2 = getTransactionsWithService(cardNumber, 15);
        System.out.println("list2 = " + list2);

        Mockito.when(masterCardTransactionService.getAllTransactionsByDates(ArgumentMatchers.any(),
                ArgumentMatchers.any(), ArgumentMatchers.any()))
            .thenReturn(Flux.fromIterable(list2));

        Mockito.when(transactionRepository1.saveAll(ArgumentMatchers.anyCollection()))
            .thenReturn(Flux.fromIterable(list2));

        Mono<List<Transaction>> listMono2 = service.getAllTransactionsByCardAndDates(
            new TransactionCardGetDTO(cardNumber, from3, to3));

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

    /*
        Cache - [2023-03-02, 2023-03-04]

        Call 1 - [2023-03-01, 2023-03-05] : {
            First : Get Cache from {CacheFromDate} to {Call-1-ToDate}
            Second : Get External Service from {Call-1-FromDate} to {CacheFromDate}
        }

        Call 2 - [2023-02-28, 2023-03-09] : {
            First : Get Cache from {Call-2-FromDate} to {CacheToDate}
            Second : Get External Service from {CacheToDate} to {Call-2-ToDate}
        }
     */
    @Test
    void test_getTransactionsWhenCacheBetweenFromAndToDates() {
        var annotationContextTransactionService1 = Mockito.mock(AnnotationContextTransactionService.class);
        var transactionRepository1 = Mockito.mock(TransactionRepository.class);
        var masterCardTransactionService = Mockito.mock(MasterCardTransactionService.class);

        SpringMainTransactionsService service = new SpringMainTransactionsService(annotationContextTransactionService1,
            transactionRepository1);

        // Cache Range
        LocalDateTime from1 = LocalDateTime.of(2023, 3, 2, 0, 0);
        LocalDateTime to1 = LocalDateTime.of(2023, 3, 4, 0, 0);

        LocalDateTime from2 = LocalDateTime.of(2023, 3, 1, 0, 0);
        LocalDateTime to2 = LocalDateTime.of(2023, 3, 5, 0, 0);

        LocalDateTime from3 = LocalDateTime.of(2023, 2, 28, 0, 0);
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

        List<Transaction> list1 = getTransactionsWithService(cardNumber, 10);
        System.out.println("list1 = " + list1);

        Mockito.when(masterCardTransactionService.getAllTransactionsByDates(ArgumentMatchers.any(),
                ArgumentMatchers.any(), ArgumentMatchers.any()))
            .thenReturn(Flux.fromIterable(list1));

        Mockito.when(transactionRepository1.saveAll(ArgumentMatchers.anyCollection()))
            .thenReturn(Flux.fromIterable(list1));

        Mono<List<Transaction>> listMono1 = service.getAllTransactionsByCardAndDates(
            new TransactionCardGetDTO(cardNumber, from2, to2));

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

        List<Transaction> list2 = getTransactionsWithService(cardNumber, 15);
        System.out.println("list2 = " + list2);

        Mockito.when(masterCardTransactionService.getAllTransactionsByDates(ArgumentMatchers.any(),
                ArgumentMatchers.any(), ArgumentMatchers.any()))
            .thenReturn(Flux.fromIterable(list2));

        Mockito.when(transactionRepository1.saveAll(ArgumentMatchers.anyCollection()))
            .thenReturn(Flux.fromIterable(list2));

        Mono<List<Transaction>> listMono2 = service.getAllTransactionsByCardAndDates(
            new TransactionCardGetDTO(cardNumber, from3, to3));

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

        Mockito.verify(masterCardTransactionService, Mockito.times(4))
            .getAllTransactionsByDates(ArgumentMatchers.any(), ArgumentMatchers.any(), ArgumentMatchers.any());

        Mockito.verify(annotationContextTransactionService1, Mockito.times(2))
            .getService(ArgumentMatchers.any());
    }

    private List<Transaction> getTransactions(String cardNumber, LocalDateTime from, LocalDateTime to) {
        return List.of(
            new Transaction(1L, new BigDecimal("6783243.748352"), Status.SUCCESS,
                "5425195488749646", cardNumber, from, LocalDateTime.now()),
            new Transaction(2L, new BigDecimal("758943.748352"), Status.FAILED,
                "5425195488749646", cardNumber, to, LocalDateTime.now())
        );
    }

    // Fixed
    private List<Transaction> getTransactionsWithService(String card, int count) {
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
