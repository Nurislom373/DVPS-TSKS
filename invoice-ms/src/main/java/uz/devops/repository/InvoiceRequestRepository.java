package uz.devops.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import uz.devops.domain.InvoiceRequest;

/**
 * Spring Data JPA repository for the InvoiceRequest entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InvoiceRequestRepository extends JpaRepository<InvoiceRequest, Long>, JpaSpecificationExecutor<InvoiceRequest> {}
