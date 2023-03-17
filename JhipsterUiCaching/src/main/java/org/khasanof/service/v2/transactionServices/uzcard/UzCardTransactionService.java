package org.khasanof.service.v2.transactionServices.uzcard;

import org.khasanof.annotation.TransactionType;
import org.khasanof.data.TransactionMockData;
import org.khasanof.domain.transaction.Transaction;
import org.khasanof.service.v2.context.FieldContextTransactionService;
import org.khasanof.service.v2.transactionServices.AbstractTransactionService;
import org.khasanof.service.v2.transactionServices.TransactionService;
import org.khasanof.service.v2.context.MethodContextTransactionService;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Author: Nurislom
 * <br/>
 * Date: 3/7/2023
 * <br/>
 * Time: 3:56 PM
 * <br/>
 * Package: org.khasanof.uicachingstrategy.service.uzcard
 */
@Component
@TransactionType(cardNumber = "8600", name = "UzCard")
public class UzCardTransactionService extends AbstractTransactionService implements TransactionService,
    FieldContextTransactionService, MethodContextTransactionService {

    private final String cardNumber = "8600";

    public UzCardTransactionService(TransactionMockData data) {
        super(data);
    }

    @Override
    protected void afterPropertiesSet() {
        final String path = "/data/mock_transactions_uzcard.json";
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
