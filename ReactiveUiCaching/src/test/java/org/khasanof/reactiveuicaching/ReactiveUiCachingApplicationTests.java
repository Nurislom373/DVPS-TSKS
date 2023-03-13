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
    void testRepository() {
        String cardNumber = "9860996540334145";
        LocalDateTime from1 = LocalDateTime.of(2023, 2, 25, 0, 0);
        LocalDateTime to1 = LocalDateTime.of(2023, 2, 27, 0, 0);
        Flux<TransactionEntity> all = transactionRepository.findAllByCreatedAtIsBetween(cardNumber, from1, to1);
        StepVerifier.create(all)
                .expectNextCount(14)
                .expectComplete()
                .verify();
    }

}
