package org.khasanof.uicachingstrategy.repository;

import org.khasanof.uicachingstrategy.domain.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Collection;
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
public interface TransactionRepository extends JpaRepository<TransactionEntity, Integer> {

    @Query(value = "select t from TransactionEntity t where t.fromCard = ?1 or t.toCard = ?1 and t.createdAt between ?2 and ?3")
    List<TransactionEntity> findAllByCreatedAtIsBetween(String cardNumber, LocalDateTime createdAt, LocalDateTime createdAt2);

    List<TransactionEntity> findAllByCreatedAtIsBetween(LocalDateTime createdAt, LocalDateTime createdAt2);

    @Query(value = "select count(t) from TransactionEntity t where t.fromCard = ?1 or t.toCard = ?1")
    int getCardCacheCount(String cardNumber);
}
