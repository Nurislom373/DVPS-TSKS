package uz.devops.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;
import uz.devops.domain.TransactionParam;
import uz.devops.domain.TransactionParam_;
import uz.devops.repository.TransactionParamRepository;
import uz.devops.service.criteria.TransactionParamCriteria;
import uz.devops.service.dto.TransactionParamDTO;
import uz.devops.service.mapper.TransactionParamMapper;

/**
 * Service for executing complex queries for {@link TransactionParam} entities in the database.
 * The main input is a {@link TransactionParamCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link TransactionParamDTO} or a {@link Page} of {@link TransactionParamDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TransactionParamQueryService extends QueryService<TransactionParam> {

    private final Logger log = LoggerFactory.getLogger(TransactionParamQueryService.class);

    private final TransactionParamRepository transactionParamRepository;

    private final TransactionParamMapper TransactionParamMapper;

    public TransactionParamQueryService(TransactionParamRepository transactionParamRepository, TransactionParamMapper TransactionParamMapper) {
        this.transactionParamRepository = transactionParamRepository;
        this.TransactionParamMapper = TransactionParamMapper;
    }

    /**
     * Return a {@link List} of {@link TransactionParamDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<TransactionParamDTO> findByCriteria(TransactionParamCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<TransactionParam> specification = createSpecification(criteria);
        return TransactionParamMapper.toDto(transactionParamRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link TransactionParamDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TransactionParamDTO> findByCriteria(TransactionParamCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<TransactionParam> specification = createSpecification(criteria);
        return transactionParamRepository.findAll(specification, page).map(TransactionParamMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TransactionParamCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<TransactionParam> specification = createSpecification(criteria);
        return transactionParamRepository.count(specification);
    }

    /**
     * Function to convert {@link TransactionParamCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<TransactionParam> createSpecification(TransactionParamCriteria criteria) {
        Specification<TransactionParam> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), TransactionParam_.id));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getStatus(), TransactionParam_.status));
            }
            if (criteria.getTransactionId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTransactionId(), TransactionParam_.transactionId));
            }
        }
        return specification;
    }
}
