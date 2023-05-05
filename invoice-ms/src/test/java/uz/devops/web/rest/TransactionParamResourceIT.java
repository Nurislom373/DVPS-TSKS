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
import uz.devops.IntegrationTest;
import uz.devops.domain.TransactionParam;
import uz.devops.domain.enumeration.Status;
import uz.devops.repository.TransactionParamRepository;
import uz.devops.service.dto.TransactionParamDTO;
import uz.devops.service.mapper.TransactionParamMapper;

/**
 * Integration tests for the {@link TransactionParamResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TransactionParamResourceIT {

    private static final Status DEFAULT_STATUS = Status.NEW;
    private static final Status UPDATED_STATUS = Status.ACTIVE;

    private static final Long DEFAULT_TRANSACTION_ID = 1L;
    private static final Long UPDATED_TRANSACTION_ID = 2L;
    private static final Long SMALLER_TRANSACTION_ID = 1L - 1L;

    private static final String ENTITY_API_URL = "/api/p-2-p-params";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TransactionParamRepository transactionParamRepository;

    @Autowired
    private TransactionParamMapper TransactionParamMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTransactionParamMockMvc;

    private TransactionParam transactionParam;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TransactionParam createEntity(EntityManager em) {
        TransactionParam transactionParam = new TransactionParam().status(DEFAULT_STATUS).transactionId(DEFAULT_TRANSACTION_ID);
        return transactionParam;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TransactionParam createUpdatedEntity(EntityManager em) {
        TransactionParam transactionParam = new TransactionParam().status(UPDATED_STATUS).transactionId(UPDATED_TRANSACTION_ID);
        return transactionParam;
    }

    @BeforeEach
    public void initTest() {
        transactionParam = createEntity(em);
    }

    @Test
    @Transactional
    void createTransactionParam() throws Exception {
        int databaseSizeBeforeCreate = transactionParamRepository.findAll().size();
        // Create the TransactionParam
        TransactionParamDTO transactionParamDTO = TransactionParamMapper.toDto(transactionParam);
        restTransactionParamMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(transactionParamDTO))
            )
            .andExpect(status().isCreated());

        // Validate the TransactionParam in the database
        List<TransactionParam> transactionParamList = transactionParamRepository.findAll();
        assertThat(transactionParamList).hasSize(databaseSizeBeforeCreate + 1);
        TransactionParam testTransactionParam = transactionParamList.get(transactionParamList.size() - 1);
        assertThat(testTransactionParam.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testTransactionParam.getTransactionId()).isEqualTo(DEFAULT_TRANSACTION_ID);
    }

    @Test
    @Transactional
    void createTransactionParamWithExistingId() throws Exception {
        // Create the TransactionParam with an existing ID
        transactionParam.setId(1L);
        TransactionParamDTO transactionParamDTO = TransactionParamMapper.toDto(transactionParam);

        int databaseSizeBeforeCreate = transactionParamRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTransactionParamMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(transactionParamDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TransactionParam in the database
        List<TransactionParam> transactionParamList = transactionParamRepository.findAll();
        assertThat(transactionParamList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTransactionIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = transactionParamRepository.findAll().size();
        // set the field null
        transactionParam.setTransactionId(null);

        // Create the TransactionParam, which fails.
        TransactionParamDTO transactionParamDTO = TransactionParamMapper.toDto(transactionParam);

        restTransactionParamMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(transactionParamDTO))
            )
            .andExpect(status().isBadRequest());

        List<TransactionParam> transactionParamList = transactionParamRepository.findAll();
        assertThat(transactionParamList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTransactionParams() throws Exception {
        // Initialize the database
        transactionParamRepository.saveAndFlush(transactionParam);

        // Get all the TransactionParamList
        restTransactionParamMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(transactionParam.getId().intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].transactionId").value(hasItem(DEFAULT_TRANSACTION_ID.intValue())));
    }

    @Test
    @Transactional
    void getTransactionParam() throws Exception {
        // Initialize the database
        transactionParamRepository.saveAndFlush(transactionParam);

        // Get the TransactionParam
        restTransactionParamMockMvc
            .perform(get(ENTITY_API_URL_ID, transactionParam.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(transactionParam.getId().intValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.transactionId").value(DEFAULT_TRANSACTION_ID.intValue()));
    }

    @Test
    @Transactional
    void getTransactionParamsByIdFiltering() throws Exception {
        // Initialize the database
        transactionParamRepository.saveAndFlush(transactionParam);

        Long id = transactionParam.getId();

        defaultTransactionParamShouldBeFound("id.equals=" + id);
        defaultTransactionParamShouldNotBeFound("id.notEquals=" + id);

        defaultTransactionParamShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultTransactionParamShouldNotBeFound("id.greaterThan=" + id);

        defaultTransactionParamShouldBeFound("id.lessThanOrEqual=" + id);
        defaultTransactionParamShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTransactionParamsByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        transactionParamRepository.saveAndFlush(transactionParam);

        // Get all the TransactionParamList where status equals to DEFAULT_STATUS
        defaultTransactionParamShouldBeFound("status.equals=" + DEFAULT_STATUS);

        // Get all the TransactionParamList where status equals to UPDATED_STATUS
        defaultTransactionParamShouldNotBeFound("status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllTransactionParamsByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        transactionParamRepository.saveAndFlush(transactionParam);

        // Get all the TransactionParamList where status in DEFAULT_STATUS or UPDATED_STATUS
        defaultTransactionParamShouldBeFound("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS);

        // Get all the TransactionParamList where status equals to UPDATED_STATUS
        defaultTransactionParamShouldNotBeFound("status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllTransactionParamsByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        transactionParamRepository.saveAndFlush(transactionParam);

        // Get all the TransactionParamList where status is not null
        defaultTransactionParamShouldBeFound("status.specified=true");

        // Get all the TransactionParamList where status is null
        defaultTransactionParamShouldNotBeFound("status.specified=false");
    }

    @Test
    @Transactional
    void getAllTransactionParamsByTransactionIdIsEqualToSomething() throws Exception {
        // Initialize the database
        transactionParamRepository.saveAndFlush(transactionParam);

        // Get all the TransactionParamList where transactionId equals to DEFAULT_TRANSACTION_ID
        defaultTransactionParamShouldBeFound("transactionId.equals=" + DEFAULT_TRANSACTION_ID);

        // Get all the TransactionParamList where transactionId equals to UPDATED_TRANSACTION_ID
        defaultTransactionParamShouldNotBeFound("transactionId.equals=" + UPDATED_TRANSACTION_ID);
    }

    @Test
    @Transactional
    void getAllTransactionParamsByTransactionIdIsInShouldWork() throws Exception {
        // Initialize the database
        transactionParamRepository.saveAndFlush(transactionParam);

        // Get all the TransactionParamList where transactionId in DEFAULT_TRANSACTION_ID or UPDATED_TRANSACTION_ID
        defaultTransactionParamShouldBeFound("transactionId.in=" + DEFAULT_TRANSACTION_ID + "," + UPDATED_TRANSACTION_ID);

        // Get all the TransactionParamList where transactionId equals to UPDATED_TRANSACTION_ID
        defaultTransactionParamShouldNotBeFound("transactionId.in=" + UPDATED_TRANSACTION_ID);
    }

    @Test
    @Transactional
    void getAllTransactionParamsByTransactionIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        transactionParamRepository.saveAndFlush(transactionParam);

        // Get all the TransactionParamList where transactionId is not null
        defaultTransactionParamShouldBeFound("transactionId.specified=true");

        // Get all the TransactionParamList where transactionId is null
        defaultTransactionParamShouldNotBeFound("transactionId.specified=false");
    }

    @Test
    @Transactional
    void getAllTransactionParamsByTransactionIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        transactionParamRepository.saveAndFlush(transactionParam);

        // Get all the TransactionParamList where transactionId is greater than or equal to DEFAULT_TRANSACTION_ID
        defaultTransactionParamShouldBeFound("transactionId.greaterThanOrEqual=" + DEFAULT_TRANSACTION_ID);

        // Get all the TransactionParamList where transactionId is greater than or equal to UPDATED_TRANSACTION_ID
        defaultTransactionParamShouldNotBeFound("transactionId.greaterThanOrEqual=" + UPDATED_TRANSACTION_ID);
    }

    @Test
    @Transactional
    void getAllTransactionParamsByTransactionIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        transactionParamRepository.saveAndFlush(transactionParam);

        // Get all the TransactionParamList where transactionId is less than or equal to DEFAULT_TRANSACTION_ID
        defaultTransactionParamShouldBeFound("transactionId.lessThanOrEqual=" + DEFAULT_TRANSACTION_ID);

        // Get all the TransactionParamList where transactionId is less than or equal to SMALLER_TRANSACTION_ID
        defaultTransactionParamShouldNotBeFound("transactionId.lessThanOrEqual=" + SMALLER_TRANSACTION_ID);
    }

    @Test
    @Transactional
    void getAllTransactionParamsByTransactionIdIsLessThanSomething() throws Exception {
        // Initialize the database
        transactionParamRepository.saveAndFlush(transactionParam);

        // Get all the TransactionParamList where transactionId is less than DEFAULT_TRANSACTION_ID
        defaultTransactionParamShouldNotBeFound("transactionId.lessThan=" + DEFAULT_TRANSACTION_ID);

        // Get all the TransactionParamList where transactionId is less than UPDATED_TRANSACTION_ID
        defaultTransactionParamShouldBeFound("transactionId.lessThan=" + UPDATED_TRANSACTION_ID);
    }

    @Test
    @Transactional
    void getAllTransactionParamsByTransactionIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        transactionParamRepository.saveAndFlush(transactionParam);

        // Get all the TransactionParamList where transactionId is greater than DEFAULT_TRANSACTION_ID
        defaultTransactionParamShouldNotBeFound("transactionId.greaterThan=" + DEFAULT_TRANSACTION_ID);

        // Get all the TransactionParamList where transactionId is greater than SMALLER_TRANSACTION_ID
        defaultTransactionParamShouldBeFound("transactionId.greaterThan=" + SMALLER_TRANSACTION_ID);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTransactionParamShouldBeFound(String filter) throws Exception {
        restTransactionParamMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(transactionParam.getId().intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].transactionId").value(hasItem(DEFAULT_TRANSACTION_ID.intValue())));

        // Check, that the count call also returns 1
        restTransactionParamMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTransactionParamShouldNotBeFound(String filter) throws Exception {
        restTransactionParamMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTransactionParamMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTransactionParam() throws Exception {
        // Get the TransactionParam
        restTransactionParamMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTransactionParam() throws Exception {
        // Initialize the database
        transactionParamRepository.saveAndFlush(transactionParam);

        int databaseSizeBeforeUpdate = transactionParamRepository.findAll().size();

        // Update the TransactionParam
        TransactionParam updatedTransactionParam = transactionParamRepository.findById(transactionParam.getId()).get();
        // Disconnect from session so that the updates on updatedTransactionParam are not directly saved in db
        em.detach(updatedTransactionParam);
        updatedTransactionParam.status(UPDATED_STATUS).transactionId(UPDATED_TRANSACTION_ID);
        TransactionParamDTO transactionParamDTO = TransactionParamMapper.toDto(updatedTransactionParam);

        restTransactionParamMockMvc
            .perform(
                put(ENTITY_API_URL_ID, transactionParamDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(transactionParamDTO))
            )
            .andExpect(status().isOk());

        // Validate the TransactionParam in the database
        List<TransactionParam> transactionParamList = transactionParamRepository.findAll();
        assertThat(transactionParamList).hasSize(databaseSizeBeforeUpdate);
        TransactionParam testTransactionParam = transactionParamList.get(transactionParamList.size() - 1);
        assertThat(testTransactionParam.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testTransactionParam.getTransactionId()).isEqualTo(UPDATED_TRANSACTION_ID);
    }

    @Test
    @Transactional
    void putNonExistingTransactionParam() throws Exception {
        int databaseSizeBeforeUpdate = transactionParamRepository.findAll().size();
        transactionParam.setId(count.incrementAndGet());

        // Create the TransactionParam
        TransactionParamDTO transactionParamDTO = TransactionParamMapper.toDto(transactionParam);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTransactionParamMockMvc
            .perform(
                put(ENTITY_API_URL_ID, transactionParamDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(transactionParamDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TransactionParam in the database
        List<TransactionParam> transactionParamList = transactionParamRepository.findAll();
        assertThat(transactionParamList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTransactionParam() throws Exception {
        int databaseSizeBeforeUpdate = transactionParamRepository.findAll().size();
        transactionParam.setId(count.incrementAndGet());

        // Create the TransactionParam
        TransactionParamDTO transactionParamDTO = TransactionParamMapper.toDto(transactionParam);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTransactionParamMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(transactionParamDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TransactionParam in the database
        List<TransactionParam> transactionParamList = transactionParamRepository.findAll();
        assertThat(transactionParamList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTransactionParam() throws Exception {
        int databaseSizeBeforeUpdate = transactionParamRepository.findAll().size();
        transactionParam.setId(count.incrementAndGet());

        // Create the TransactionParam
        TransactionParamDTO transactionParamDTO = TransactionParamMapper.toDto(transactionParam);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTransactionParamMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(transactionParamDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TransactionParam in the database
        List<TransactionParam> transactionParamList = transactionParamRepository.findAll();
        assertThat(transactionParamList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTransactionParamWithPatch() throws Exception {
        // Initialize the database
        transactionParamRepository.saveAndFlush(transactionParam);

        int databaseSizeBeforeUpdate = transactionParamRepository.findAll().size();

        // Update the TransactionParam using partial update
        TransactionParam partialUpdatedTransactionParam = new TransactionParam();
        partialUpdatedTransactionParam.setId(transactionParam.getId());

        partialUpdatedTransactionParam.status(UPDATED_STATUS);

        restTransactionParamMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTransactionParam.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTransactionParam))
            )
            .andExpect(status().isOk());

        // Validate the TransactionParam in the database
        List<TransactionParam> transactionParamList = transactionParamRepository.findAll();
        assertThat(transactionParamList).hasSize(databaseSizeBeforeUpdate);
        TransactionParam testTransactionParam = transactionParamList.get(transactionParamList.size() - 1);
        assertThat(testTransactionParam.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testTransactionParam.getTransactionId()).isEqualTo(DEFAULT_TRANSACTION_ID);
    }

    @Test
    @Transactional
    void fullUpdateTransactionParamWithPatch() throws Exception {
        // Initialize the database
        transactionParamRepository.saveAndFlush(transactionParam);

        int databaseSizeBeforeUpdate = transactionParamRepository.findAll().size();

        // Update the TransactionParam using partial update
        TransactionParam partialUpdatedTransactionParam = new TransactionParam();
        partialUpdatedTransactionParam.setId(transactionParam.getId());

        partialUpdatedTransactionParam.status(UPDATED_STATUS).transactionId(UPDATED_TRANSACTION_ID);

        restTransactionParamMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTransactionParam.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTransactionParam))
            )
            .andExpect(status().isOk());

        // Validate the TransactionParam in the database
        List<TransactionParam> transactionParamList = transactionParamRepository.findAll();
        assertThat(transactionParamList).hasSize(databaseSizeBeforeUpdate);
        TransactionParam testTransactionParam = transactionParamList.get(transactionParamList.size() - 1);
        assertThat(testTransactionParam.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testTransactionParam.getTransactionId()).isEqualTo(UPDATED_TRANSACTION_ID);
    }

    @Test
    @Transactional
    void patchNonExistingTransactionParam() throws Exception {
        int databaseSizeBeforeUpdate = transactionParamRepository.findAll().size();
        transactionParam.setId(count.incrementAndGet());

        // Create the TransactionParam
        TransactionParamDTO transactionParamDTO = TransactionParamMapper.toDto(transactionParam);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTransactionParamMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, transactionParamDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(transactionParamDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TransactionParam in the database
        List<TransactionParam> transactionParamList = transactionParamRepository.findAll();
        assertThat(transactionParamList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTransactionParam() throws Exception {
        int databaseSizeBeforeUpdate = transactionParamRepository.findAll().size();
        transactionParam.setId(count.incrementAndGet());

        // Create the TransactionParam
        TransactionParamDTO transactionParamDTO = TransactionParamMapper.toDto(transactionParam);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTransactionParamMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(transactionParamDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TransactionParam in the database
        List<TransactionParam> transactionParamList = transactionParamRepository.findAll();
        assertThat(transactionParamList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTransactionParam() throws Exception {
        int databaseSizeBeforeUpdate = transactionParamRepository.findAll().size();
        transactionParam.setId(count.incrementAndGet());

        // Create the TransactionParam
        TransactionParamDTO transactionParamDTO = TransactionParamMapper.toDto(transactionParam);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTransactionParamMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(transactionParamDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TransactionParam in the database
        List<TransactionParam> transactionParamList = transactionParamRepository.findAll();
        assertThat(transactionParamList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTransactionParam() throws Exception {
        // Initialize the database
        transactionParamRepository.saveAndFlush(transactionParam);

        int databaseSizeBeforeDelete = transactionParamRepository.findAll().size();

        // Delete the TransactionParam
        restTransactionParamMockMvc
            .perform(delete(ENTITY_API_URL_ID, transactionParam.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TransactionParam> transactionParamList = transactionParamRepository.findAll();
        assertThat(transactionParamList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
