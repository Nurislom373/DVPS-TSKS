package org.khasanof.web.rest;

import org.khasanof.domain.transaction.Transaction;
import org.khasanof.dto.transaction.TransactionCardGetDTO;
import org.khasanof.dto.transaction.TransactionMultiCardGetDTO;
import org.khasanof.service.MainTransactionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * REST controller for managing {@link Transaction}.
 */
@RestController
@RequestMapping("/api")
public class TransactionResource {

    private final Logger log = LoggerFactory.getLogger(TransactionResource.class);

    private final MainTransactionService transactionService;

    public TransactionResource(MainTransactionService transactionService) {
        this.transactionService = transactionService;
    }

    /**
     * {@code GET  /transactions} : get all the transactions with date interval.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of transactions in body.
     */
    @PostMapping("/transactionsWithCard")
    public Mono<ResponseEntity<List<Transaction>>> getAllTransactionsWithCard(@Valid @RequestBody TransactionCardGetDTO dto) {
        log.debug("REST request to get all Transactions");
        return transactionService.getAllTransactionsByCardAndDates(dto)
            .flatMap(list -> Mono.just(ResponseEntity
                .ok()
                .body(list)));
    }

    /**
     * {@code GET  /transactions} : get all the transactions as a stream.
     *
     * @return the {@link Flux} of transactions.
     */
    @PostMapping(value = "/transactionsWithMultiCards")
    public Mono<ResponseEntity<Map<String, List<Transaction>>>> getAllTransactionsWithMultiCards(@Valid @RequestBody TransactionMultiCardGetDTO dto) {
        log.debug("REST request to get all Transactions with Multi Cards");
        return transactionService.getAllTransactionsByCardsAndDates(dto)
            .flatMap(list -> Mono.just(ResponseEntity
                .ok()
                .body(list)));
    }
}
