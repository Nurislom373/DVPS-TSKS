package org.khasanof.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.khasanof.domain.transaction.Transaction;
import org.khasanof.repository.TransactionRepository;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;

/**
 * Author: Nurislom
 * <br/>
 * Date: 3/17/2023
 * <br/>
 * Time: 5:53 PM
 * <br/>
 * Package: org.khasanof
 */
@SpringBootTest
@ActiveProfiles("dev")
public class TransactionRepositoryTest {

    @Autowired
    private TransactionRepository repository;


    @Test
    void test_findAllByQueryCheckExpectCount() {
        LocalDateTime from1 = LocalDateTime.of(2023, 3, 2, 0, 0);
        LocalDateTime to1 = LocalDateTime.of(2023, 3, 4, 0, 0);
        Flux<Transaction> flux = repository.findAllByQuery("5425764309411081", from1, to1);

        StepVerifier.create(flux)
            .expectNextCount(7)
            .expectComplete()
            .verify();
    }

    @Test
    void test_findAllByQueryCaseIsEmpty() {
        LocalDateTime from1 = LocalDateTime.of(2023, 3, 2, 0, 0);
        LocalDateTime to1 = LocalDateTime.of(2023, 3, 4, 0, 0);
        Flux<Transaction> flux = repository.findAllByQuery("5425764309411082", from1, to1);

        StepVerifier.create(flux)
            .expectNextCount(0)
            .expectComplete()
            .verify();
    }

    @Test
    void test_findAllByStartQueryCheckExpectCount() {
        LocalDateTime from1 = LocalDateTime.of(2023, 3, 2, 0, 0);
        LocalDateTime to1 = LocalDateTime.of(2023, 3, 4, 0, 0);
        Flux<Transaction> flux = repository.findAllByStartQuery("5425%", from1, to1);

        StepVerifier.create(flux)
            .expectNextCount(7)
            .expectComplete()
            .verify();
    }

    @Test
    void test_findAllByStartQueryCaseIsEmpty() {
        LocalDateTime from1 = LocalDateTime.of(2023, 3, 2, 0, 0);
        LocalDateTime to1 = LocalDateTime.of(2023, 3, 4, 0, 0);
        Flux<Transaction> flux = repository.findAllByStartQuery("9860%", from1, to1);

        StepVerifier.create(flux)
            .expectNextCount(0)
            .expectComplete()
            .verify();
    }

    @Test
    void test_getCardCacheCount() {
        Mono<Long> mono = repository.getCardCacheCount("5425764309411081");

        StepVerifier.create(mono)
            .expectNext(7L)
            .expectComplete()
            .verify();
    }

    @Test
    void test_CaseGetCardCacheCountZero() {
        Mono<Long> mono = repository.getCardCacheCount("5425764309411082");

        StepVerifier.create(mono)
            .expectNext(0L)
            .expectComplete()
            .verify();
    }

    @Test
    void test_getCardCacheCountStart() {
        Mono<Long> mono = repository.getCardCacheCountStart("5425%");

        StepVerifier.create(mono)
            .expectNext(7L)
            .expectComplete()
            .verify();
    }

    @Test
    void test_CaseGetCardCacheCountStartZero() {
        Mono<Long> mono = repository.getCardCacheCountStart("9860%");

        StepVerifier.create(mono)
            .expectNext(0L)
            .expectComplete()
            .verify();
    }

    @Test
    void test_findAll() {
        Flux<Transaction> flux = repository.findAll();

        StepVerifier.create(flux)
            .expectNextCount(7)
            .expectComplete()
            .verify();
    }

    @Test
    void test_findAllByQueryMockTest() {
        TransactionRepository transactionRepository = Mockito.mock(TransactionRepository.class);

        Mockito.when(transactionRepository.findAllByQuery(ArgumentMatchers.any(), ArgumentMatchers.any(),
            ArgumentMatchers.any())).thenReturn(Flux.empty());

        Flux<Transaction> flux = transactionRepository.findAllByQuery(ArgumentMatchers.any(),
            ArgumentMatchers.any(), ArgumentMatchers.any());

        StepVerifier.create(flux)
            .expectNextCount(0)
            .expectComplete()
            .verify();
    }
}
