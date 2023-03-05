package org.khasanof.uicaching.repository;

import org.khasanof.uicaching.domain.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
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

    List<TransactionEntity> findAllByIdIsNotInAndCreatedAtBetween(Collection<Long> id, LocalDateTime createdAt, LocalDateTime createdAt2);

    List<TransactionEntity> findAllByCreatedAtIsBetween(LocalDateTime createdAt, LocalDateTime createdAt2);

}
