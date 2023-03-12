package org.khasanof.reactiveuicaching.repository;

import org.khasanof.reactiveuicaching.domain.TransactionEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Author: Nurislom
 * <br/>
 * Date: 2/28/2023
 * <br/>
 * Time: 4:40 PM
 * <br/>
 * Package: org.khasanof.uicaching.repository
 */
@Repository
public interface TransactionRepository extends ReactiveCrudRepository<TransactionEntity, Integer> {

    @Query(value = "select t from TransactionEntity t where t.fromCard = ?1 or t.toCard = ?1 and t.createdAt between ?2 and ?3")
    Flux<TransactionEntity> findAllByCreatedAtIsBetween(String cardNumber, LocalDateTime createdAt, LocalDateTime createdAt2);

    Flux<TransactionEntity> findAllByCreatedAtIsBetween(LocalDateTime createdAt, LocalDateTime createdAt2);

    @Query(value = "select count(t) from TransactionEntity t where t.fromCard = ?1 or t.toCard = ?1")
    Mono<Long> getCardCacheCount(String cardNumber);
}
