package org.khasanof.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.khasanof.web.rest.TestUtil.sameNumber;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.khasanof.IntegrationTest;
import org.khasanof.domain.transaction.Transaction;
import org.khasanof.enums.Status;
import org.khasanof.repository.EntityManager;
import org.khasanof.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 * Integration tests for the {@link TransactionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class TransactionResourceIT {

    private static final BigDecimal DEFAULT_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_AMOUNT = new BigDecimal(2);

    private static final Status DEFAULT_STATUS = Status.FAILED;
    private static final Status UPDATED_STATUS = Status.SUCCESS;

    private static final String DEFAULT_FROM_CARD = "AAAAAAAAAA";
    private static final String UPDATED_FROM_CARD = "BBBBBBBBBB";

    private static final String DEFAULT_TO_CARD = "AAAAAAAAAA";
    private static final String UPDATED_TO_CARD = "BBBBBBBBBB";

    private static final LocalDateTime DEFAULT_CREATED_AT = LocalDateTime.now();
    private static final LocalDateTime UPDATED_CREATED_AT = LocalDateTime.now().truncatedTo(ChronoUnit.MILLIS);

    private static final LocalDateTime DEFAULT_UPDATED_AT = LocalDateTime.now();
    private static final LocalDateTime UPDATED_UPDATED_AT = LocalDateTime.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/transactions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Transaction transaction;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Transaction createEntity(EntityManager em) {
        Transaction transaction = new Transaction()
            .amount(DEFAULT_AMOUNT)
            .status(DEFAULT_STATUS)
            .fromCard(DEFAULT_FROM_CARD)
            .toCard(DEFAULT_TO_CARD)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedAt(DEFAULT_UPDATED_AT);
        return transaction;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Transaction createUpdatedEntity(EntityManager em) {
        Transaction transaction = new Transaction()
            .amount(UPDATED_AMOUNT)
            .status(UPDATED_STATUS)
            .fromCard(UPDATED_FROM_CARD)
            .toCard(UPDATED_TO_CARD)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        return transaction;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Transaction.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
    }

    @AfterEach
    public void cleanup() {
        deleteEntities(em);
    }

    @BeforeEach
    public void initTest() {
        deleteEntities(em);
        transaction = createEntity(em);
    }

    @Test
    void createTransaction() throws Exception {
        int databaseSizeBeforeCreate = transactionRepository.findAll().collectList().block().size();
        // Create the Transaction
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(transaction))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Transaction in the database
        List<Transaction> transactionList = transactionRepository.findAll().collectList().block();
        assertThat(transactionList).hasSize(databaseSizeBeforeCreate + 1);
        Transaction testTransaction = transactionList.get(transactionList.size() - 1);
        assertThat(testTransaction.getAmount()).isEqualByComparingTo(DEFAULT_AMOUNT);
        assertThat(testTransaction.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testTransaction.getFromCard()).isEqualTo(DEFAULT_FROM_CARD);
        assertThat(testTransaction.getToCard()).isEqualTo(DEFAULT_TO_CARD);
        assertThat(testTransaction.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testTransaction.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
    }

    @Test
    void createTransactionWithExistingId() throws Exception {
        // Create the Transaction with an existing ID
        transaction.setId(1L);

        int databaseSizeBeforeCreate = transactionRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(transaction))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Transaction in the database
        List<Transaction> transactionList = transactionRepository.findAll().collectList().block();
        assertThat(transactionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkAmountIsRequired() throws Exception {
        int databaseSizeBeforeTest = transactionRepository.findAll().collectList().block().size();
        // set the field null
        transaction.setAmount(null);

        // Create the Transaction, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(transaction))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Transaction> transactionList = transactionRepository.findAll().collectList().block();
        assertThat(transactionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllTransactionsAsStream() {
        // Initialize the database
        transactionRepository.save(transaction).block();

        List<Transaction> transactionList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(Transaction.class)
            .getResponseBody()
            .filter(transaction::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(transactionList).isNotNull();
        assertThat(transactionList).hasSize(1);
        Transaction testTransaction = transactionList.get(0);
        assertThat(testTransaction.getAmount()).isEqualByComparingTo(DEFAULT_AMOUNT);
        assertThat(testTransaction.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testTransaction.getFromCard()).isEqualTo(DEFAULT_FROM_CARD);
        assertThat(testTransaction.getToCard()).isEqualTo(DEFAULT_TO_CARD);
        assertThat(testTransaction.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testTransaction.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
    }

    @Test
    void getAllTransactions() {
        // Initialize the database
        transactionRepository.save(transaction).block();

        // Get all the transactionList
        webTestClient
            .get()
            .uri(ENTITY_API_URL + "?sort=id,desc")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id")
            .value(hasItem(transaction.getId().intValue()))
            .jsonPath("$.[*].amount")
            .value(hasItem(sameNumber(DEFAULT_AMOUNT)))
            .jsonPath("$.[*].status")
            .value(hasItem(DEFAULT_STATUS.toString()))
            .jsonPath("$.[*].fromCard")
            .value(hasItem(DEFAULT_FROM_CARD))
            .jsonPath("$.[*].toCard")
            .value(hasItem(DEFAULT_TO_CARD))
            .jsonPath("$.[*].createdAt")
            .value(hasItem(DEFAULT_CREATED_AT.toString()))
            .jsonPath("$.[*].updatedAt")
            .value(hasItem(DEFAULT_UPDATED_AT.toString()));
    }

    @Test
    void getTransaction() {
        // Initialize the database
        transactionRepository.save(transaction).block();

        // Get the transaction
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, transaction.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(transaction.getId().intValue()))
            .jsonPath("$.amount")
            .value(is(sameNumber(DEFAULT_AMOUNT)))
            .jsonPath("$.status")
            .value(is(DEFAULT_STATUS.toString()))
            .jsonPath("$.fromCard")
            .value(is(DEFAULT_FROM_CARD))
            .jsonPath("$.toCard")
            .value(is(DEFAULT_TO_CARD))
            .jsonPath("$.createdAt")
            .value(is(DEFAULT_CREATED_AT.toString()))
            .jsonPath("$.updatedAt")
            .value(is(DEFAULT_UPDATED_AT.toString()));
    }

    @Test
    void getNonExistingTransaction() {
        // Get the transaction
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingTransaction() throws Exception {
        // Initialize the database
        transactionRepository.save(transaction).block();

        int databaseSizeBeforeUpdate = transactionRepository.findAll().collectList().block().size();

        // Update the transaction
        Transaction updatedTransaction = transactionRepository.findById(transaction.getId()).block();
        updatedTransaction
            .amount(UPDATED_AMOUNT)
            .status(UPDATED_STATUS)
            .fromCard(UPDATED_FROM_CARD)
            .toCard(UPDATED_TO_CARD)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedTransaction.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedTransaction))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Transaction in the database
        List<Transaction> transactionList = transactionRepository.findAll().collectList().block();
        assertThat(transactionList).hasSize(databaseSizeBeforeUpdate);
        Transaction testTransaction = transactionList.get(transactionList.size() - 1);
        assertThat(testTransaction.getAmount()).isEqualByComparingTo(UPDATED_AMOUNT);
        assertThat(testTransaction.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testTransaction.getFromCard()).isEqualTo(UPDATED_FROM_CARD);
        assertThat(testTransaction.getToCard()).isEqualTo(UPDATED_TO_CARD);
        assertThat(testTransaction.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testTransaction.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    void putNonExistingTransaction() throws Exception {
        int databaseSizeBeforeUpdate = transactionRepository.findAll().collectList().block().size();
        transaction.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, transaction.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(transaction))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Transaction in the database
        List<Transaction> transactionList = transactionRepository.findAll().collectList().block();
        assertThat(transactionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchTransaction() throws Exception {
        int databaseSizeBeforeUpdate = transactionRepository.findAll().collectList().block().size();
        transaction.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(transaction))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Transaction in the database
        List<Transaction> transactionList = transactionRepository.findAll().collectList().block();
        assertThat(transactionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamTransaction() throws Exception {
        int databaseSizeBeforeUpdate = transactionRepository.findAll().collectList().block().size();
        transaction.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(transaction))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Transaction in the database
        List<Transaction> transactionList = transactionRepository.findAll().collectList().block();
        assertThat(transactionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateTransactionWithPatch() throws Exception {
        // Initialize the database
        transactionRepository.save(transaction).block();

        int databaseSizeBeforeUpdate = transactionRepository.findAll().collectList().block().size();

        // Update the transaction using partial update
        Transaction partialUpdatedTransaction = new Transaction();
        partialUpdatedTransaction.setId(transaction.getId());

        partialUpdatedTransaction.status(UPDATED_STATUS);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedTransaction.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedTransaction))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Transaction in the database
        List<Transaction> transactionList = transactionRepository.findAll().collectList().block();
        assertThat(transactionList).hasSize(databaseSizeBeforeUpdate);
        Transaction testTransaction = transactionList.get(transactionList.size() - 1);
        assertThat(testTransaction.getAmount()).isEqualByComparingTo(DEFAULT_AMOUNT);
        assertThat(testTransaction.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testTransaction.getFromCard()).isEqualTo(DEFAULT_FROM_CARD);
        assertThat(testTransaction.getToCard()).isEqualTo(DEFAULT_TO_CARD);
        assertThat(testTransaction.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testTransaction.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
    }

    @Test
    void fullUpdateTransactionWithPatch() throws Exception {
        // Initialize the database
        transactionRepository.save(transaction).block();

        int databaseSizeBeforeUpdate = transactionRepository.findAll().collectList().block().size();

        // Update the transaction using partial update
        Transaction partialUpdatedTransaction = new Transaction();
        partialUpdatedTransaction.setId(transaction.getId());

        partialUpdatedTransaction
            .amount(UPDATED_AMOUNT)
            .status(UPDATED_STATUS)
            .fromCard(UPDATED_FROM_CARD)
            .toCard(UPDATED_TO_CARD)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedTransaction.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedTransaction))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Transaction in the database
        List<Transaction> transactionList = transactionRepository.findAll().collectList().block();
        assertThat(transactionList).hasSize(databaseSizeBeforeUpdate);
        Transaction testTransaction = transactionList.get(transactionList.size() - 1);
        assertThat(testTransaction.getAmount()).isEqualByComparingTo(UPDATED_AMOUNT);
        assertThat(testTransaction.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testTransaction.getFromCard()).isEqualTo(UPDATED_FROM_CARD);
        assertThat(testTransaction.getToCard()).isEqualTo(UPDATED_TO_CARD);
        assertThat(testTransaction.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testTransaction.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    void patchNonExistingTransaction() throws Exception {
        int databaseSizeBeforeUpdate = transactionRepository.findAll().collectList().block().size();
        transaction.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, transaction.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(transaction))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Transaction in the database
        List<Transaction> transactionList = transactionRepository.findAll().collectList().block();
        assertThat(transactionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchTransaction() throws Exception {
        int databaseSizeBeforeUpdate = transactionRepository.findAll().collectList().block().size();
        transaction.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(transaction))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Transaction in the database
        List<Transaction> transactionList = transactionRepository.findAll().collectList().block();
        assertThat(transactionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamTransaction() throws Exception {
        int databaseSizeBeforeUpdate = transactionRepository.findAll().collectList().block().size();
        transaction.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(transaction))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Transaction in the database
        List<Transaction> transactionList = transactionRepository.findAll().collectList().block();
        assertThat(transactionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteTransaction() {
        // Initialize the database
        transactionRepository.save(transaction).block();

        int databaseSizeBeforeDelete = transactionRepository.findAll().collectList().block().size();

        // Delete the transaction
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, transaction.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Transaction> transactionList = transactionRepository.findAll().collectList().block();
        assertThat(transactionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
