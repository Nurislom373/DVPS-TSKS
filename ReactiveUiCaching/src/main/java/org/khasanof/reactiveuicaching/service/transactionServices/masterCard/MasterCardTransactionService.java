package org.khasanof.reactiveuicaching.service.transactionServices.masterCard;

import org.khasanof.reactiveuicaching.annotation.TransactionType;
import org.khasanof.reactiveuicaching.data.TransactionMockData;
import org.khasanof.reactiveuicaching.domain.TransactionEntity;
import org.khasanof.reactiveuicaching.service.TransactionService;
import org.khasanof.reactiveuicaching.service.context.FieldContextTransactionService;
import org.khasanof.reactiveuicaching.service.context.MethodContextTransactionService;
import org.khasanof.reactiveuicaching.service.transactionServices.AbstractTransactionService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Author: Nurislom
 * <br/>
 * Date: 3/11/2023
 * <br/>
 * Time: 9:48 AM
 * <br/>
 * Package: org.khasanof.uicachingstrategy.service.transactionServices.masterCard
 */
@Service
@TransactionType(cardNumber = "5425", name = "MasterCard")
public class MasterCardTransactionService extends AbstractTransactionService
        implements TransactionService, FieldContextTransactionService, MethodContextTransactionService {

    private final String cardNumber = "5425";

    public MasterCardTransactionService(TransactionMockData data) {
        super(data);
    }

    @Override
    protected void afterPropertiesSet() {
        final String path = "/data/mock_transactions_mastercard.json";
        List<TransactionEntity> mockList = data.getMockList(path, cardNumber);
        super.addList(mockList);
    }

    @Override
    public Flux<TransactionEntity> getAllTransactionsByDates(String cardNumber, LocalDateTime from, LocalDateTime to) {
        return Flux.fromIterable(super.getAllTransactions(cardNumber, from, to));
    }

    @Override
    public String getCardNumber() {
        return cardNumber;
    }
}
