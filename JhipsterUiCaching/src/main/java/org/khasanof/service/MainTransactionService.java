package org.khasanof.service;

import org.khasanof.domain.transaction.Transaction;
import org.khasanof.dto.transaction.TransactionCardGetDTO;
import org.khasanof.dto.transaction.TransactionMultiCardGetDTO;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

/**
 * Author: Nurislom
 * <br/>
 * Date: 3/17/2023
 * <br/>
 * Time: 5:14 PM
 * <br/>
 * Package: org.khasanof.service
 */
public interface MainTransactionService {

    Mono<List<Transaction>> getAllTransactionsByCardAndDates(TransactionCardGetDTO dto);

    Mono<Map<String, List<Transaction>>> getAllTransactionsByCardsAndDates(TransactionMultiCardGetDTO dto);

}
