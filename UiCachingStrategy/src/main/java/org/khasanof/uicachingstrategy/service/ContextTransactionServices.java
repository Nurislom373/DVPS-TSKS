package org.khasanof.uicachingstrategy.service;

import lombok.AllArgsConstructor;
import org.khasanof.uicachingstrategy.enums.CardType;
import org.khasanof.uicachingstrategy.service.humo.HumoTransactionService;
import org.khasanof.uicachingstrategy.service.uzcard.UzCardTransactionService;
import org.springframework.stereotype.Service;

/**
 * Author: Nurislom
 * <br/>
 * Date: 3/7/2023
 * <br/>
 * Time: 4:01 PM
 * <br/>
 * Package: org.khasanof.uicachingstrategy.service
 */
@Service
@AllArgsConstructor
public class ContextTransactionServices {

    private UzCardTransactionService uzCardTransactionService;
    private HumoTransactionService humoTransactionService;

    public TransactionService getService(String cardNumber) {
        if (cardNumber.startsWith(CardType.UZCARD.getCardNumber())) {
            return uzCardTransactionService;
        } else if (cardNumber.startsWith(CardType.HUMO.getCardNumber())) {
            return humoTransactionService;
        } else {
            throw new RuntimeException("Invalid Card Number!");
        }
    }

}
