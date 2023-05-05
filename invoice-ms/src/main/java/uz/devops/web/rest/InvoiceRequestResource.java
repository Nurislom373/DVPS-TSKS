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
import uz.devops.repository.InvoiceRequestRepository;
import uz.devops.service.InvoiceRequestQueryService;
import uz.devops.service.InvoiceRequestService;
import uz.devops.service.criteria.InvoiceRequestCriteria;
import uz.devops.service.dto.InvoiceRequestDTO;
import uz.devops.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link uz.devops.domain.InvoiceRequest}.
 */
@RestController
@RequestMapping("/api")
public class InvoiceRequestResource {

    private final Logger log = LoggerFactory.getLogger(InvoiceRequestResource.class);

    private static final String ENTITY_NAME = "invoicemsInvoiceRequest";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final InvoiceRequestService invoiceRequestService;

    private final InvoiceRequestRepository invoiceRequestRepository;

    private final InvoiceRequestQueryService invoiceRequestQueryService;

    public InvoiceRequestResource(
        InvoiceRequestService invoiceRequestService,
        InvoiceRequestRepository invoiceRequestRepository,
        InvoiceRequestQueryService invoiceRequestQueryService
    ) {
        this.invoiceRequestService = invoiceRequestService;
        this.invoiceRequestRepository = invoiceRequestRepository;
        this.invoiceRequestQueryService = invoiceRequestQueryService;
    }

    /**
     * {@code POST  /invoice-requests} : Create a new invoiceRequest.
     *
     * @param invoiceRequestDTO the invoiceRequestDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new invoiceRequestDTO, or with status {@code 400 (Bad Request)} if the invoiceRequest has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/invoice-requests")
    public ResponseEntity<InvoiceRequestDTO> createInvoiceRequest(@Valid @RequestBody InvoiceRequestDTO invoiceRequestDTO)
        throws URISyntaxException {
        log.debug("REST request to save InvoiceRequest : {}", invoiceRequestDTO);
        if (invoiceRequestDTO.getId() != null) {
            throw new BadRequestAlertException("A new invoiceRequest cannot already have an ID", ENTITY_NAME, "idexists");
        }
        InvoiceRequestDTO result = invoiceRequestService.save(invoiceRequestDTO);
        return ResponseEntity
            .created(new URI("/api/invoice-requests/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /invoice-requests/:id} : Updates an existing invoiceRequest.
     *
     * @param id the id of the invoiceRequestDTO to save.
     * @param invoiceRequestDTO the invoiceRequestDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated invoiceRequestDTO,
     * or with status {@code 400 (Bad Request)} if the invoiceRequestDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the invoiceRequestDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/invoice-requests/{id}")
    public ResponseEntity<InvoiceRequestDTO> updateInvoiceRequest(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody InvoiceRequestDTO invoiceRequestDTO
    ) throws URISyntaxException {
        log.debug("REST request to update InvoiceRequest : {}, {}", id, invoiceRequestDTO);
        if (invoiceRequestDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, invoiceRequestDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!invoiceRequestRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        InvoiceRequestDTO result = invoiceRequestService.update(invoiceRequestDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, invoiceRequestDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /invoice-requests/:id} : Partial updates given fields of an existing invoiceRequest, field will ignore if it is null
     *
     * @param id the id of the invoiceRequestDTO to save.
     * @param invoiceRequestDTO the invoiceRequestDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated invoiceRequestDTO,
     * or with status {@code 400 (Bad Request)} if the invoiceRequestDTO is not valid,
     * or with status {@code 404 (Not Found)} if the invoiceRequestDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the invoiceRequestDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/invoice-requests/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<InvoiceRequestDTO> partialUpdateInvoiceRequest(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody InvoiceRequestDTO invoiceRequestDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update InvoiceRequest partially : {}, {}", id, invoiceRequestDTO);
        if (invoiceRequestDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, invoiceRequestDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!invoiceRequestRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<InvoiceRequestDTO> result = invoiceRequestService.partialUpdate(invoiceRequestDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, invoiceRequestDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /invoice-requests} : get all the invoiceRequests.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of invoiceRequests in body.
     */
    @GetMapping("/invoice-requests")
    public ResponseEntity<List<InvoiceRequestDTO>> getAllInvoiceRequests(
        InvoiceRequestCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get InvoiceRequests by criteria: {}", criteria);
        Page<InvoiceRequestDTO> page = invoiceRequestQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /invoice-requests/count} : count all the invoiceRequests.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/invoice-requests/count")
    public ResponseEntity<Long> countInvoiceRequests(InvoiceRequestCriteria criteria) {
        log.debug("REST request to count InvoiceRequests by criteria: {}", criteria);
        return ResponseEntity.ok().body(invoiceRequestQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /invoice-requests/:id} : get the "id" invoiceRequest.
     *
     * @param id the id of the invoiceRequestDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the invoiceRequestDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/invoice-requests/{id}")
    public ResponseEntity<InvoiceRequestDTO> getInvoiceRequest(@PathVariable Long id) {
        log.debug("REST request to get InvoiceRequest : {}", id);
        Optional<InvoiceRequestDTO> invoiceRequestDTO = invoiceRequestService.findOne(id);
        return ResponseUtil.wrapOrNotFound(invoiceRequestDTO);
    }

    /**
     * {@code DELETE  /invoice-requests/:id} : delete the "id" invoiceRequest.
     *
     * @param id the id of the invoiceRequestDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/invoice-requests/{id}")
    public ResponseEntity<Void> deleteInvoiceRequest(@PathVariable Long id) {
        log.debug("REST request to delete InvoiceRequest : {}", id);
        invoiceRequestService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
