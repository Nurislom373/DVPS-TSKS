package org.khasanof.reactiveuicaching.service;


import org.khasanof.reactiveuicaching.domain.TransactionEntity;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;

/**
 * Author: Nurislom
 * <br/>
 * Date: 3/7/2023
 * <br/>
 * Time: 3:29 PM
 * <br/>
 * Package: org.khasanof.uicachingstrategy.service
 */
public interface TransactionService {

    Flux<TransactionEntity> getAllTransactionsByDates(String cardNumber, LocalDateTime from, LocalDateTime to);
}
