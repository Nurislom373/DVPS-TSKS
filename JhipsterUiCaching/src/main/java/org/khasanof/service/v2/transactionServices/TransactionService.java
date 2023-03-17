package org.khasanof.service.v2.transactionServices;

import org.khasanof.domain.transaction.Transaction;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;

/**
 * Service Interface for managing {@link Transaction}.
 */
public interface TransactionService {

    Flux<Transaction> getAllTransactionsByDates(String cardNumber, LocalDateTime from, LocalDateTime to);
}
