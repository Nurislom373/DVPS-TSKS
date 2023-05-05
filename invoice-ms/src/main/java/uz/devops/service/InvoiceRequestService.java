package uz.devops.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import uz.devops.service.dto.InvoiceRequestDTO;

/**
 * Service Interface for managing {@link uz.devops.domain.InvoiceRequest}.
 */
public interface InvoiceRequestService {
    /**
     * Save a invoiceRequest.
     *
     * @param invoiceRequestDTO the entity to save.
     * @return the persisted entity.
     */
    InvoiceRequestDTO save(InvoiceRequestDTO invoiceRequestDTO);

    /**
     * Updates a invoiceRequest.
     *
     * @param invoiceRequestDTO the entity to update.
     * @return the persisted entity.
     */
    InvoiceRequestDTO update(InvoiceRequestDTO invoiceRequestDTO);

    /**
     * Partially updates a invoiceRequest.
     *
     * @param invoiceRequestDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<InvoiceRequestDTO> partialUpdate(InvoiceRequestDTO invoiceRequestDTO);

    /**
     * Get all the invoiceRequests.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<InvoiceRequestDTO> findAll(Pageable pageable);

    /**
     * Get the "id" invoiceRequest.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<InvoiceRequestDTO> findOne(Long id);

    /**
     * Delete the "id" invoiceRequest.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
