package uz.devops.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;
import uz.devops.domain.TransactionParam;
import uz.devops.repository.TransactionParamRepository;
import uz.devops.service.TransactionParamQueryService;
import uz.devops.service.TransactionParamService;
import uz.devops.service.criteria.TransactionParamCriteria;
import uz.devops.service.dto.TransactionParamDTO;
import uz.devops.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link TransactionParam}.
 */
@RestController
@RequestMapping("/api")
public class TransactionParamResource {

    private final Logger log = LoggerFactory.getLogger(TransactionParamResource.class);

    private static final String ENTITY_NAME = "invoicemsTransactionParam";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TransactionParamService transactionParamService;

    private final TransactionParamRepository transactionParamRepository;

    private final TransactionParamQueryService transactionParamQueryService;

    public TransactionParamResource(
        TransactionParamService transactionParamService,
        TransactionParamRepository transactionParamRepository,
        TransactionParamQueryService transactionParamQueryService
    ) {
        this.transactionParamService = transactionParamService;
        this.transactionParamRepository = transactionParamRepository;
        this.transactionParamQueryService = transactionParamQueryService;
    }

    /**
     * {@code POST  /p-2-p-params} : Create a new TransactionParam.
     *
     * @param transactionParamDTO the TransactionParamDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new TransactionParamDTO, or with status {@code 400 (Bad Request)} if the TransactionParam has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/p-2-p-params")
    public ResponseEntity<TransactionParamDTO> createTransactionParam(@Valid @RequestBody TransactionParamDTO transactionParamDTO) throws URISyntaxException {
        log.debug("REST request to save TransactionParam : {}", transactionParamDTO);
        if (transactionParamDTO.getId() != null) {
            throw new BadRequestAlertException("A new TransactionParam cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TransactionParamDTO result = transactionParamService.save(transactionParamDTO);
        return ResponseEntity
            .created(new URI("/api/p-2-p-params/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /p-2-p-params/:id} : Updates an existing TransactionParam.
     *
     * @param id the id of the TransactionParamDTO to save.
     * @param transactionParamDTO the TransactionParamDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated TransactionParamDTO,
     * or with status {@code 400 (Bad Request)} if the TransactionParamDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the TransactionParamDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/p-2-p-params/{id}")
    public ResponseEntity<TransactionParamDTO> updateTransactionParam(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TransactionParamDTO transactionParamDTO
    ) throws URISyntaxException {
        log.debug("REST request to update TransactionParam : {}, {}", id, transactionParamDTO);
        if (transactionParamDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, transactionParamDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!transactionParamRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TransactionParamDTO result = transactionParamService.update(transactionParamDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, transactionParamDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /p-2-p-params/:id} : Partial updates given fields of an existing TransactionParam, field will ignore if it is null
     *
     * @param id the id of the TransactionParamDTO to save.
     * @param transactionParamDTO the TransactionParamDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated TransactionParamDTO,
     * or with status {@code 400 (Bad Request)} if the TransactionParamDTO is not valid,
     * or with status {@code 404 (Not Found)} if the TransactionParamDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the TransactionParamDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/p-2-p-params/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TransactionParamDTO> partialUpdateTransactionParam(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TransactionParamDTO transactionParamDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update TransactionParam partially : {}, {}", id, transactionParamDTO);
        if (transactionParamDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, transactionParamDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!transactionParamRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TransactionParamDTO> result = transactionParamService.partialUpdate(transactionParamDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, transactionParamDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /p-2-p-params} : get all the TransactionParams.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of TransactionParams in body.
     */
    @GetMapping("/p-2-p-params")
    public ResponseEntity<List<TransactionParamDTO>> getAllTransactionParams(
        TransactionParamCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get TransactionParams by criteria: {}", criteria);
        Page<TransactionParamDTO> page = transactionParamQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /p-2-p-params/count} : count all the TransactionParams.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/p-2-p-params/count")
    public ResponseEntity<Long> countTransactionParams(TransactionParamCriteria criteria) {
        log.debug("REST request to count TransactionParams by criteria: {}", criteria);
        return ResponseEntity.ok().body(transactionParamQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /p-2-p-params/:id} : get the "id" TransactionParam.
     *
     * @param id the id of the TransactionParamDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the TransactionParamDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/p-2-p-params/{id}")
    public ResponseEntity<TransactionParamDTO> getTransactionParam(@PathVariable Long id) {
        log.debug("REST request to get TransactionParam : {}", id);
        Optional<TransactionParamDTO> TransactionParamDTO = transactionParamService.findOne(id);
        return ResponseUtil.wrapOrNotFound(TransactionParamDTO);
    }

    /**
     * {@code DELETE  /p-2-p-params/:id} : delete the "id" TransactionParam.
     *
     * @param id the id of the TransactionParamDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/p-2-p-params/{id}")
    public ResponseEntity<Void> deleteTransactionParam(@PathVariable Long id) {
        log.debug("REST request to delete TransactionParam : {}", id);
        transactionParamService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
