package uz.devops.service;

import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;
import uz.devops.domain.*; // for static metamodels
import uz.devops.domain.InvoiceRequest;
import uz.devops.repository.InvoiceRequestRepository;
import uz.devops.service.criteria.InvoiceRequestCriteria;
import uz.devops.service.dto.InvoiceRequestDTO;
import uz.devops.service.mapper.InvoiceRequestMapper;

/**
 * Service for executing complex queries for {@link InvoiceRequest} entities in the database.
 * The main input is a {@link InvoiceRequestCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link InvoiceRequestDTO} or a {@link Page} of {@link InvoiceRequestDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class InvoiceRequestQueryService extends QueryService<InvoiceRequest> {

    private final Logger log = LoggerFactory.getLogger(InvoiceRequestQueryService.class);

    private final InvoiceRequestRepository invoiceRequestRepository;

    private final InvoiceRequestMapper invoiceRequestMapper;

    public InvoiceRequestQueryService(InvoiceRequestRepository invoiceRequestRepository, InvoiceRequestMapper invoiceRequestMapper) {
        this.invoiceRequestRepository = invoiceRequestRepository;
        this.invoiceRequestMapper = invoiceRequestMapper;
    }

    /**
     * Return a {@link List} of {@link InvoiceRequestDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<InvoiceRequestDTO> findByCriteria(InvoiceRequestCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<InvoiceRequest> specification = createSpecification(criteria);
        return invoiceRequestMapper.toDto(invoiceRequestRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link InvoiceRequestDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<InvoiceRequestDTO> findByCriteria(InvoiceRequestCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<InvoiceRequest> specification = createSpecification(criteria);
        return invoiceRequestRepository.findAll(specification, page).map(invoiceRequestMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(InvoiceRequestCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<InvoiceRequest> specification = createSpecification(criteria);
        return invoiceRequestRepository.count(specification);
    }

    /**
     * Function to convert {@link InvoiceRequestCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<InvoiceRequest> createSpecification(InvoiceRequestCriteria criteria) {
        Specification<InvoiceRequest> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), InvoiceRequest_.id));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getStatus(), InvoiceRequest_.status));
            }
        }
        return specification;
    }
}
