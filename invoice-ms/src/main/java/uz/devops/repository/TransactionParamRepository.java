package uz.devops.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import uz.devops.domain.TransactionParam;

import javax.validation.constraints.NotNull;
import java.util.Optional;

/**
 * Spring Data JPA repository for the TransactionParam entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TransactionParamRepository extends JpaRepository<TransactionParam, Long>, JpaSpecificationExecutor<TransactionParam> {

    Optional<TransactionParam> findByTransactionId(@NotNull Long transactionId);

    Boolean existsByTransactionId(@NotNull Long transactionId);

}
