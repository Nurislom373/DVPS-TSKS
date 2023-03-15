package org.khasanof.reactiveuicaching;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.khasanof.reactiveuicaching.domain.TransactionEntity;
import org.khasanof.reactiveuicaching.repository.TransactionRepository;
import org.khasanof.reactiveuicaching.service.MainTransactionsService;
import org.khasanof.reactiveuicaching.service.context.AnnotationContextTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@SpringBootTest
class ReactiveUiCachingApplicationTests {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private AnnotationContextTransactionService annotationContextTransactionService;

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
        LocalDateTime from1 = LocalDateTime.of(2023, 3, 2, 0, 0);
        LocalDateTime to1 = LocalDateTime.of(2023, 3, 4, 0, 0);
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
        LocalDateTime from1 = LocalDateTime.of(2023, 3, 3, 0, 0);
        LocalDateTime to1 = LocalDateTime.of(2023, 3, 7, 0, 0);

        List<String> list = List.of("5425195488749646", "5425366817704639", "5425877627225510");

        Mono<Map<String, List<TransactionEntity>>> dates = service.getAllTransactionsByCardsAndDates(list, from1, to1);

        Map<String, List<TransactionEntity>> listMap = dates.block();
        System.out.println("listMap = " + listMap);
        System.out.println("listMap.get(\"5425195488749646\").size() = " + listMap.get("5425195488749646").size());
        System.out.println("listMap.get(\"5425366817704639\").size() = " + listMap.get("5425366817704639").size());
        System.out.println("listMap.get(\"5425877627225510\").size() = " + listMap.get("5425877627225510").size());
    }

    @Test
    void testRepository() {
        String cardNumber = "";
        LocalDateTime from1 = LocalDateTime.of(2023, 3, 3, 0, 0);
        LocalDateTime to1 = LocalDateTime.of(2023, 3, 7, 0, 0);
        Flux<TransactionEntity> all = transactionRepository.findAllByQuery(cardNumber, from1, to1);
        StepVerifier.create(all)
                .expectNextCount(11)
                .expectComplete()
                .verify();
    }

    private void soutList(List<TransactionEntity> list) {
        list.forEach(System.out::println);
    }

}
