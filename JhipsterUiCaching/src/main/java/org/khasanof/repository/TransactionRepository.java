package org.khasanof.repository;

import org.khasanof.domain.transaction.Transaction;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

/**
 * Spring Data R2DBC repository for the Transaction entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TransactionRepository extends ReactiveCrudRepository<Transaction, Long>, TransactionRepositoryInternal {

    @Query("select * from transaction t where created_at between :createdAt1 and :createdAt2 and " +
        "(from_card = :cardNumber or to_card = :cardNumber)")
    Flux<Transaction> findAllByQuery(String cardNumber, LocalDateTime createdAt1, LocalDateTime createdAt2);

    @Query("select * from transaction t where created_at between :createdAt1 and :createdAt2 and " +
        "(from_card like :cardNumber or to_card like :cardNumber)")
    Flux<Transaction> findAllByStartQuery(String cardNumber, LocalDateTime createdAt1, LocalDateTime createdAt2);

    Flux<Transaction> findAllByCreatedAtIsBetween(LocalDateTime createdAt, LocalDateTime createdAt2);

    @Query("select count(*) from transaction t where t.from_card = $1 or t.to_card = $1")
    Mono<Long> getCardCacheCount(String cardNumber);

    @Query("select count(*) from transaction t where t.from_card like $1 or t.to_card like $1")
    Mono<Long> getCardCacheCountStart(String cardStart);
    @Override
    <S extends Transaction> Mono<S> save(S entity);

    @Override
    Flux<Transaction> findAll();

    @Override
    Mono<Transaction> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface TransactionRepositoryInternal {
    <S extends Transaction> Mono<S> save(S entity);

    Flux<Transaction> findAllBy(Pageable pageable);

    Flux<Transaction> findAll();

    Mono<Transaction> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<Transaction> findAllBy(Pageable pageable, Criteria criteria);

}
