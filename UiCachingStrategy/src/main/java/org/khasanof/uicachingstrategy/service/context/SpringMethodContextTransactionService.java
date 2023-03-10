package org.khasanof.uicachingstrategy.service.context;

import org.khasanof.uicachingstrategy.service.TransactionService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Author: Nurislom
 * <br/>
 * Date: 3/10/2023
 * <br/>
 * Time: 11:24 PM
 * <br/>
 * Package: org.khasanof.uicachingstrategy.service.context
 */
@Service
public class SpringMethodContextTransactionService implements ContextTransactionService {

    @Override
    public TransactionService getService(String cardNumber) {
        return null;
    }

    @Override
    public List<TransactionService> getServices() {
        return null;
    }
}
