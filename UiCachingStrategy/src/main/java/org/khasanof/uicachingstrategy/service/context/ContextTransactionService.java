package org.khasanof.uicachingstrategy.service.context;

import org.khasanof.uicachingstrategy.service.TransactionService;

import java.util.List;

/**
 * Author: Nurislom
 * <br/>
 * Date: 3/10/2023
 * <br/>
 * Time: 7:19 PM
 * <br/>
 * Package: org.khasanof.uicachingstrategy.service
 */
public interface ContextTransactionService {

    TransactionService getService(String cardNumber);

    List<TransactionService> getServices();

}
