package org.khasanof.service.transactionServices.humo;

import org.khasanof.annotation.TransactionType;
import org.khasanof.service.transactionServices.TransactionService;
import org.khasanof.domain.transaction.Transaction;
import org.khasanof.data.TransactionMockData;
import org.khasanof.service.context.FieldContextTransactionService;
import org.khasanof.service.context.MethodContextTransactionService;
import org.khasanof.service.transactionServices.AbstractTransactionService;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Author: Nurislom
 * <br/>
 * Date: 3/7/2023
 * <br/>
 * Time: 3:58 PM
 * <br/>
 * Package: org.khasanof.uicachingstrategy.service.humo
 */
@Component
@TransactionType(cardNumber = "9860", name = "Humo")
public class HumoTransactionService extends AbstractTransactionService implements TransactionService,
    FieldContextTransactionService, MethodContextTransactionService {

    private final String cardNumber = "9860";

    public HumoTransactionService(TransactionMockData data) {
        super(data);
    }

    @Override
    protected void afterPropertiesSet() {
        final String path = "/data/mock_transactions_humo.json";
        List<Transaction> mockList = data.getMockList(path, cardNumber);
        super.addList(mockList);
    }

    @Override
    public Flux<Transaction> getAllTransactionsByDates(String cardNumber, LocalDateTime from, LocalDateTime to) {
        return Flux.fromIterable(super.getAllTransactions(cardNumber, from, to));
    }

    @Override
    public String getCardNumber() {
        return cardNumber;
    }
}
