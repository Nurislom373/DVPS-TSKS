package org.khasanof.reactiveuicaching.service.context;


import org.khasanof.reactiveuicaching.service.TransactionService;

import java.util.List;
import java.util.Map;

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

    Map<String, TransactionService> getServices();

}
