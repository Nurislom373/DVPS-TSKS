package org.khasanof.uicachingstrategy.service;

import org.khasanof.uicachingstrategy.domain.TransactionEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

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

    List<TransactionEntity> getAllTransactionsByDates(String cardNumber, LocalDateTime from, LocalDateTime to);

}
