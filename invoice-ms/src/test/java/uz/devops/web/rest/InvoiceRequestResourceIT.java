package uz.devops.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;
import uz.devops.IntegrationTest;
import uz.devops.domain.InvoiceRequest;
import uz.devops.domain.enumeration.Status;
import uz.devops.repository.InvoiceRequestRepository;
import uz.devops.service.criteria.InvoiceRequestCriteria;
import uz.devops.service.dto.InvoiceRequestDTO;
import uz.devops.service.mapper.InvoiceRequestMapper;

/**
 * Integration tests for the {@link InvoiceRequestResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class InvoiceRequestResourceIT {

    private static final Status DEFAULT_STATUS = Status.NEW;
    private static final Status UPDATED_STATUS = Status.ACTIVE;

    private static final String DEFAULT_REQUEST_JSON = "AAAAAAAAAA";
    private static final String UPDATED_REQUEST_JSON = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/invoice-requests";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private InvoiceRequestRepository invoiceRequestRepository;

    @Autowired
    private InvoiceRequestMapper invoiceRequestMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restInvoiceRequestMockMvc;

    private InvoiceRequest invoiceRequest;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InvoiceRequest createEntity(EntityManager em) {
        InvoiceRequest invoiceRequest = new InvoiceRequest().status(DEFAULT_STATUS).requestJson(DEFAULT_REQUEST_JSON);
        return invoiceRequest;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InvoiceRequest createUpdatedEntity(EntityManager em) {
        InvoiceRequest invoiceRequest = new InvoiceRequest().status(UPDATED_STATUS).requestJson(UPDATED_REQUEST_JSON);
        return invoiceRequest;
    }

    @BeforeEach
    public void initTest() {
        invoiceRequest = createEntity(em);
    }

    @Test
    @Transactional
    void createInvoiceRequest() throws Exception {
        int databaseSizeBeforeCreate = invoiceRequestRepository.findAll().size();
        // Create the InvoiceRequest
        InvoiceRequestDTO invoiceRequestDTO = invoiceRequestMapper.toDto(invoiceRequest);
        restInvoiceRequestMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(invoiceRequestDTO))
            )
            .andExpect(status().isCreated());

        // Validate the InvoiceRequest in the database
        List<InvoiceRequest> invoiceRequestList = invoiceRequestRepository.findAll();
        assertThat(invoiceRequestList).hasSize(databaseSizeBeforeCreate + 1);
        InvoiceRequest testInvoiceRequest = invoiceRequestList.get(invoiceRequestList.size() - 1);
        assertThat(testInvoiceRequest.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testInvoiceRequest.getRequestJson()).isEqualTo(DEFAULT_REQUEST_JSON);
    }

    @Test
    @Transactional
    void createInvoiceRequestWithExistingId() throws Exception {
        // Create the InvoiceRequest with an existing ID
        invoiceRequest.setId(1L);
        InvoiceRequestDTO invoiceRequestDTO = invoiceRequestMapper.toDto(invoiceRequest);

        int databaseSizeBeforeCreate = invoiceRequestRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restInvoiceRequestMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(invoiceRequestDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the InvoiceRequest in the database
        List<InvoiceRequest> invoiceRequestList = invoiceRequestRepository.findAll();
        assertThat(invoiceRequestList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllInvoiceRequests() throws Exception {
        // Initialize the database
        invoiceRequestRepository.saveAndFlush(invoiceRequest);

        // Get all the invoiceRequestList
        restInvoiceRequestMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(invoiceRequest.getId().intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].requestJson").value(hasItem(DEFAULT_REQUEST_JSON.toString())));
    }

    @Test
    @Transactional
    void getInvoiceRequest() throws Exception {
        // Initialize the database
        invoiceRequestRepository.saveAndFlush(invoiceRequest);

        // Get the invoiceRequest
        restInvoiceRequestMockMvc
            .perform(get(ENTITY_API_URL_ID, invoiceRequest.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(invoiceRequest.getId().intValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.requestJson").value(DEFAULT_REQUEST_JSON.toString()));
    }

    @Test
    @Transactional
    void getInvoiceRequestsByIdFiltering() throws Exception {
        // Initialize the database
        invoiceRequestRepository.saveAndFlush(invoiceRequest);

        Long id = invoiceRequest.getId();

        defaultInvoiceRequestShouldBeFound("id.equals=" + id);
        defaultInvoiceRequestShouldNotBeFound("id.notEquals=" + id);

        defaultInvoiceRequestShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultInvoiceRequestShouldNotBeFound("id.greaterThan=" + id);

        defaultInvoiceRequestShouldBeFound("id.lessThanOrEqual=" + id);
        defaultInvoiceRequestShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllInvoiceRequestsByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRequestRepository.saveAndFlush(invoiceRequest);

        // Get all the invoiceRequestList where status equals to DEFAULT_STATUS
        defaultInvoiceRequestShouldBeFound("status.equals=" + DEFAULT_STATUS);

        // Get all the invoiceRequestList where status equals to UPDATED_STATUS
        defaultInvoiceRequestShouldNotBeFound("status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllInvoiceRequestsByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        invoiceRequestRepository.saveAndFlush(invoiceRequest);

        // Get all the invoiceRequestList where status in DEFAULT_STATUS or UPDATED_STATUS
        defaultInvoiceRequestShouldBeFound("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS);

        // Get all the invoiceRequestList where status equals to UPDATED_STATUS
        defaultInvoiceRequestShouldNotBeFound("status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllInvoiceRequestsByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        invoiceRequestRepository.saveAndFlush(invoiceRequest);

        // Get all the invoiceRequestList where status is not null
        defaultInvoiceRequestShouldBeFound("status.specified=true");

        // Get all the invoiceRequestList where status is null
        defaultInvoiceRequestShouldNotBeFound("status.specified=false");
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultInvoiceRequestShouldBeFound(String filter) throws Exception {
        restInvoiceRequestMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(invoiceRequest.getId().intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].requestJson").value(hasItem(DEFAULT_REQUEST_JSON.toString())));

        // Check, that the count call also returns 1
        restInvoiceRequestMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultInvoiceRequestShouldNotBeFound(String filter) throws Exception {
        restInvoiceRequestMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restInvoiceRequestMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingInvoiceRequest() throws Exception {
        // Get the invoiceRequest
        restInvoiceRequestMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingInvoiceRequest() throws Exception {
        // Initialize the database
        invoiceRequestRepository.saveAndFlush(invoiceRequest);

        int databaseSizeBeforeUpdate = invoiceRequestRepository.findAll().size();

        // Update the invoiceRequest
        InvoiceRequest updatedInvoiceRequest = invoiceRequestRepository.findById(invoiceRequest.getId()).get();
        // Disconnect from session so that the updates on updatedInvoiceRequest are not directly saved in db
        em.detach(updatedInvoiceRequest);
        updatedInvoiceRequest.status(UPDATED_STATUS).requestJson(UPDATED_REQUEST_JSON);
        InvoiceRequestDTO invoiceRequestDTO = invoiceRequestMapper.toDto(updatedInvoiceRequest);

        restInvoiceRequestMockMvc
            .perform(
                put(ENTITY_API_URL_ID, invoiceRequestDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(invoiceRequestDTO))
            )
            .andExpect(status().isOk());

        // Validate the InvoiceRequest in the database
        List<InvoiceRequest> invoiceRequestList = invoiceRequestRepository.findAll();
        assertThat(invoiceRequestList).hasSize(databaseSizeBeforeUpdate);
        InvoiceRequest testInvoiceRequest = invoiceRequestList.get(invoiceRequestList.size() - 1);
        assertThat(testInvoiceRequest.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testInvoiceRequest.getRequestJson()).isEqualTo(UPDATED_REQUEST_JSON);
    }

    @Test
    @Transactional
    void putNonExistingInvoiceRequest() throws Exception {
        int databaseSizeBeforeUpdate = invoiceRequestRepository.findAll().size();
        invoiceRequest.setId(count.incrementAndGet());

        // Create the InvoiceRequest
        InvoiceRequestDTO invoiceRequestDTO = invoiceRequestMapper.toDto(invoiceRequest);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInvoiceRequestMockMvc
            .perform(
                put(ENTITY_API_URL_ID, invoiceRequestDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(invoiceRequestDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the InvoiceRequest in the database
        List<InvoiceRequest> invoiceRequestList = invoiceRequestRepository.findAll();
        assertThat(invoiceRequestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchInvoiceRequest() throws Exception {
        int databaseSizeBeforeUpdate = invoiceRequestRepository.findAll().size();
        invoiceRequest.setId(count.incrementAndGet());

        // Create the InvoiceRequest
        InvoiceRequestDTO invoiceRequestDTO = invoiceRequestMapper.toDto(invoiceRequest);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInvoiceRequestMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(invoiceRequestDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the InvoiceRequest in the database
        List<InvoiceRequest> invoiceRequestList = invoiceRequestRepository.findAll();
        assertThat(invoiceRequestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamInvoiceRequest() throws Exception {
        int databaseSizeBeforeUpdate = invoiceRequestRepository.findAll().size();
        invoiceRequest.setId(count.incrementAndGet());

        // Create the InvoiceRequest
        InvoiceRequestDTO invoiceRequestDTO = invoiceRequestMapper.toDto(invoiceRequest);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInvoiceRequestMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(invoiceRequestDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the InvoiceRequest in the database
        List<InvoiceRequest> invoiceRequestList = invoiceRequestRepository.findAll();
        assertThat(invoiceRequestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateInvoiceRequestWithPatch() throws Exception {
        // Initialize the database
        invoiceRequestRepository.saveAndFlush(invoiceRequest);

        int databaseSizeBeforeUpdate = invoiceRequestRepository.findAll().size();

        // Update the invoiceRequest using partial update
        InvoiceRequest partialUpdatedInvoiceRequest = new InvoiceRequest();
        partialUpdatedInvoiceRequest.setId(invoiceRequest.getId());

        partialUpdatedInvoiceRequest.status(UPDATED_STATUS);

        restInvoiceRequestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInvoiceRequest.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedInvoiceRequest))
            )
            .andExpect(status().isOk());

        // Validate the InvoiceRequest in the database
        List<InvoiceRequest> invoiceRequestList = invoiceRequestRepository.findAll();
        assertThat(invoiceRequestList).hasSize(databaseSizeBeforeUpdate);
        InvoiceRequest testInvoiceRequest = invoiceRequestList.get(invoiceRequestList.size() - 1);
        assertThat(testInvoiceRequest.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testInvoiceRequest.getRequestJson()).isEqualTo(DEFAULT_REQUEST_JSON);
    }

    @Test
    @Transactional
    void fullUpdateInvoiceRequestWithPatch() throws Exception {
        // Initialize the database
        invoiceRequestRepository.saveAndFlush(invoiceRequest);

        int databaseSizeBeforeUpdate = invoiceRequestRepository.findAll().size();

        // Update the invoiceRequest using partial update
        InvoiceRequest partialUpdatedInvoiceRequest = new InvoiceRequest();
        partialUpdatedInvoiceRequest.setId(invoiceRequest.getId());

        partialUpdatedInvoiceRequest.status(UPDATED_STATUS).requestJson(UPDATED_REQUEST_JSON);

        restInvoiceRequestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInvoiceRequest.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedInvoiceRequest))
            )
            .andExpect(status().isOk());

        // Validate the InvoiceRequest in the database
        List<InvoiceRequest> invoiceRequestList = invoiceRequestRepository.findAll();
        assertThat(invoiceRequestList).hasSize(databaseSizeBeforeUpdate);
        InvoiceRequest testInvoiceRequest = invoiceRequestList.get(invoiceRequestList.size() - 1);
        assertThat(testInvoiceRequest.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testInvoiceRequest.getRequestJson()).isEqualTo(UPDATED_REQUEST_JSON);
    }

    @Test
    @Transactional
    void patchNonExistingInvoiceRequest() throws Exception {
        int databaseSizeBeforeUpdate = invoiceRequestRepository.findAll().size();
        invoiceRequest.setId(count.incrementAndGet());

        // Create the InvoiceRequest
        InvoiceRequestDTO invoiceRequestDTO = invoiceRequestMapper.toDto(invoiceRequest);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInvoiceRequestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, invoiceRequestDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(invoiceRequestDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the InvoiceRequest in the database
        List<InvoiceRequest> invoiceRequestList = invoiceRequestRepository.findAll();
        assertThat(invoiceRequestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchInvoiceRequest() throws Exception {
        int databaseSizeBeforeUpdate = invoiceRequestRepository.findAll().size();
        invoiceRequest.setId(count.incrementAndGet());

        // Create the InvoiceRequest
        InvoiceRequestDTO invoiceRequestDTO = invoiceRequestMapper.toDto(invoiceRequest);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInvoiceRequestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(invoiceRequestDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the InvoiceRequest in the database
        List<InvoiceRequest> invoiceRequestList = invoiceRequestRepository.findAll();
        assertThat(invoiceRequestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamInvoiceRequest() throws Exception {
        int databaseSizeBeforeUpdate = invoiceRequestRepository.findAll().size();
        invoiceRequest.setId(count.incrementAndGet());

        // Create the InvoiceRequest
        InvoiceRequestDTO invoiceRequestDTO = invoiceRequestMapper.toDto(invoiceRequest);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInvoiceRequestMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(invoiceRequestDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the InvoiceRequest in the database
        List<InvoiceRequest> invoiceRequestList = invoiceRequestRepository.findAll();
        assertThat(invoiceRequestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteInvoiceRequest() throws Exception {
        // Initialize the database
        invoiceRequestRepository.saveAndFlush(invoiceRequest);

        int databaseSizeBeforeDelete = invoiceRequestRepository.findAll().size();

        // Delete the invoiceRequest
        restInvoiceRequestMockMvc
            .perform(delete(ENTITY_API_URL_ID, invoiceRequest.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<InvoiceRequest> invoiceRequestList = invoiceRequestRepository.findAll();
        assertThat(invoiceRequestList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
