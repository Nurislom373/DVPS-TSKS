package uz.devops.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import uz.devops.domain.TransactionParam;
import uz.devops.service.dto.TransactionParamDTO;

/**
 * Service Interface for managing {@link TransactionParam}.
 */
public interface TransactionParamService {
    /**
     * Save a TransactionParam.
     *
     * @param transactionParamDTO the entity to save.
     * @return the persisted entity.
     */
    TransactionParamDTO save(TransactionParamDTO transactionParamDTO);

    /**
     * Updates a TransactionParam.
     *
     * @param transactionParamDTO the entity to update.
     * @return the persisted entity.
     */
    TransactionParamDTO update(TransactionParamDTO transactionParamDTO);

    /**
     * Partially updates a TransactionParam.
     *
     * @param transactionParamDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<TransactionParamDTO> partialUpdate(TransactionParamDTO transactionParamDTO);

    /**
     * Get all the TransactionParams.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<TransactionParamDTO> findAll(Pageable pageable);

    /**
     * Get the "id" TransactionParam.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TransactionParamDTO> findOne(Long id);

    /**
     * Delete the "id" TransactionParam.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
