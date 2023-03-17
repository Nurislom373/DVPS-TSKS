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
import org.khasanof.reactiveuicaching.service.transactionServices.humo.HumoTransactionService;
import org.khasanof.reactiveuicaching.service.transactionServices.masterCard.MasterCardTransactionService;
import org.khasanof.reactiveuicaching.service.transactionServices.uzcard.UzCardTransactionService;
import org.khasanof.reactiveuicaching.service.transactionServices.visa.VisaTransactionService;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
class ReactiveUiCachingApplicationTests {

    @Autowired
    private TransactionMockData data;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private AnnotationContextTransactionService annotationContextTransactionService;

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

    @Test
    void contextLoads() {
        MainTransactionsService service = new MainTransactionsService(annotationContextTransactionService,
                transactionRepository);
        LocalDateTime from1 = LocalDateTime.of(2023, 2, 25, 0, 0);
        LocalDateTime to1 = LocalDateTime.of(2023, 2, 27, 0, 0);
        String cardNUmber = "9860996540334145";
        Mono<List<TransactionEntity>> mono = service.getAllTransactionsByCardAndDates(cardNUmber, from1, to1);

        List<TransactionEntity> block = mono.block();
        System.out.println("block.size() = " + block.size());

        StepVerifier.create(mono)
                .assertNext(i -> Assertions.assertThat(i).isNotNull())
                .expectComplete()
                .verify();
    }

    @Test
    void testOtherService() {
        MainTransactionsService service = new MainTransactionsService(annotationContextTransactionService,
                transactionRepository);
        LocalDateTime from1 = LocalDateTime.of(2023, 3, 3, 0, 0);
        LocalDateTime to1 = LocalDateTime.of(2023, 3, 5, 0, 0);
        String cardNUmber = "9860587209978556";
        Mono<List<TransactionEntity>> mono = service.getAllTransactionsByCardAndDates(cardNUmber, from1, to1);

        List<TransactionEntity> block = mono.block();
        System.out.println("block.size() = " + block.size());

        StepVerifier.create(mono)
                .assertNext(i -> Assertions.assertThat(i).isNotNull())
                .expectComplete()
                .verify();
    }

    @Test
    void testBetweenCacheRange() {
        MainTransactionsService service = new MainTransactionsService(annotationContextTransactionService,
                transactionRepository);
        LocalDateTime from1 = LocalDateTime.of(2023, 3, 3, 0, 0);
        LocalDateTime to1 = LocalDateTime.of(2023, 3, 7, 0, 0);
        String cardNUmber = "9860587209978556";
        Mono<List<TransactionEntity>> mono = service.getAllTransactionsByCardAndDates(cardNUmber, from1, to1);

        List<TransactionEntity> block = mono.block();
        System.out.println("block.size() = " + block.size());

        // created_at between '2023-03-03 00:00:00' and '2023-03-07 00:00:00' and (from_card = '9860587209978556' or to_card = '9860587209978556')

        StepVerifier.create(mono)
                .assertNext(i -> Assertions.assertThat(i).isNotNull())
                .expectComplete()
                .verify();

        soutList(block);
    }

    @Test
    void testGetTransactionOnlyExternalServiceWhenCacheRangeGreaterThan() {
        MainTransactionsService service = new MainTransactionsService(annotationContextTransactionService,
                transactionRepository);
        LocalDateTime from1 = LocalDateTime.of(2023, 3, 11, 0, 0);
        LocalDateTime to1 = LocalDateTime.of(2023, 3, 14, 0, 0);
        String cardNUmber = "9860587209978556";
        Mono<List<TransactionEntity>> mono = service.getAllTransactionsByCardAndDates(cardNUmber, from1, to1);

        List<TransactionEntity> block = mono.block();
        System.out.println("block.size() = " + block.size());
        soutList(block);

        // created_at between '2023-03-03 00:00:00' and '2023-03-07 00:00:00' and (from_card = '9860587209978556' or to_card = '9860587209978556')
    }

    @Test
    void getTransactionsWhenCacheRangeFromGreaterThanRequestFrom() {
        MainTransactionsService service = new MainTransactionsService(annotationContextTransactionService,
                transactionRepository);
        LocalDateTime from1 = LocalDateTime.of(2023, 3, 8, 0, 0);
        LocalDateTime to1 = LocalDateTime.of(2023, 3, 13, 0, 0);
        String cardNUmber = "9860587209978556";
        Mono<List<TransactionEntity>> mono = service.getAllTransactionsByCardAndDates(cardNUmber, from1, to1);

        List<TransactionEntity> block = mono.block();
        System.out.println("block.size() = " + block.size());
        soutList(block);

        // created_at between '2023-03-03 00:00:00' and '2023-03-07 00:00:00' and (from_card = '9860587209978556' or to_card = '9860587209978556')
    }

    @Test
    void test_getOnlyCache() {
        MainTransactionsService service = new MainTransactionsService(annotationContextTransactionService,
                transactionRepository);
        LocalDateTime from1 = LocalDateTime.of(2023, 3, 6, 0, 0);
        LocalDateTime to1 = LocalDateTime.of(2023, 3, 8, 0, 0);
        String cardNUmber = "*";
        Mono<List<TransactionEntity>> mono = service.getAllTransactionsByCardAndDates(cardNUmber, from1, to1);

        List<TransactionEntity> block = mono.block();
        System.out.println("block.size() = " + block.size());
        soutList(block);
    }

    @Test
    void test_cacheRangeGreaterThan() {
        MainTransactionsService service = new MainTransactionsService(annotationContextTransactionService,
                transactionRepository);
        LocalDateTime from1 = LocalDateTime.of(2023, 3, 1, 0, 0);
        LocalDateTime to1 = LocalDateTime.of(2023, 3, 10, 0, 0);
        String cardNumber = "5425195488749646";

        Mono<List<TransactionEntity>> mono = service.getAllTransactionsByCardAndDates(cardNumber, from1, to1);

        List<TransactionEntity> block = mono.block();
        System.out.println("block.size() = " + block.size());
        soutList(block);
    }

    @Test
    void test_multiCards() {
        MainTransactionsService service = new MainTransactionsService(annotationContextTransactionService,
                transactionRepository);
        LocalDateTime from1 = LocalDateTime.of(2023, 3, 4, 0, 0);
        LocalDateTime to1 = LocalDateTime.of(2023, 3, 6, 0, 0);

        List<String> list = List.of("5425195488749646", "5425366817704639", "5425877627225510");

        Mono<Map<String, List<TransactionEntity>>> dates = service.getAllTransactionsByCardsAndDates(list, from1, to1);

        Map<String, List<TransactionEntity>> listMap = dates.block();
        System.out.println("listMap.get(\"5425195488749646\").size() = " + listMap.get("5425195488749646").size());
        System.out.println("listMap.get(\"5425366817704639\").size() = " + listMap.get("5425366817704639").size());
        System.out.println("listMap.get(\"5425877627225510\").size() = " + listMap.get("5425877627225510").size());
    }

    @Test
    void test_With9860Cards() {
        MainTransactionsService service = new MainTransactionsService(annotationContextTransactionService,
                transactionRepository);
        LocalDateTime from1 = LocalDateTime.of(2023, 3, 2, 0, 0);
        LocalDateTime to1 = LocalDateTime.of(2023, 3, 7, 0, 0);

        LocalDateTime from2 = LocalDateTime.of(2023, 3, 4, 0, 0);
        LocalDateTime to2 = LocalDateTime.of(2023, 3, 8, 0, 0);

        String card = "9860044148040762";

        Mono<List<TransactionEntity>> one = service.getAllTransactionsByCardAndDates(card, from1, to1);
        Mono<List<TransactionEntity>> two = service.getAllTransactionsByCardAndDates(card, from2, to2);

        List<TransactionEntity> list1 = one.block();
        List<TransactionEntity> list2 = two.block();

        System.out.println("list1.size() = " + list1.size());
        System.out.println("list2.size() = " + list2.size());
    }

    @Test
    void test_With9860CardsCacheRangeGreater() {
        MainTransactionsService service = new MainTransactionsService(annotationContextTransactionService,
                transactionRepository);

        LocalDateTime from1 = LocalDateTime.of(2023, 2, 27, 0, 0);
        LocalDateTime to1 = LocalDateTime.of(2023, 3, 11, 0, 0);

        String card = "9860044148040762";

        Mono<List<TransactionEntity>> one = service.getAllTransactionsByCardAndDates(card, from1, to1);

        List<TransactionEntity> list1 = one.block();

        System.out.println("list1.size() = " + list1.size());
    }

    @Test
    void test_With9860CardsCacheRangeGreaterThan() {
        MainTransactionsService service = new MainTransactionsService(annotationContextTransactionService,
                transactionRepository);

        LocalDateTime from1 = LocalDateTime.of(2023, 3, 12, 0, 0);
        LocalDateTime to1 = LocalDateTime.of(2023, 3, 15, 0, 0);

        String card = "9860044148040762";

        Mono<List<TransactionEntity>> one = service.getAllTransactionsByCardAndDates(card, from1, to1);

        List<TransactionEntity> list1 = one.block();

        System.out.println("list1.size() = " + list1.size());
    }

    private void soutList(List<TransactionEntity> list) {
        list.forEach(System.out::println);
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
