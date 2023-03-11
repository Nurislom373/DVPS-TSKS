package org.khasanof.uicachingstrategy.service.transactionServices.masterCard;

import org.khasanof.uicachingstrategy.annotation.TransactionType;
import org.khasanof.uicachingstrategy.data.TransactionMockData;
import org.khasanof.uicachingstrategy.domain.TransactionEntity;
import org.khasanof.uicachingstrategy.service.TransactionService;
import org.khasanof.uicachingstrategy.service.context.FieldContextTransactionService;
import org.khasanof.uicachingstrategy.service.context.MethodContextTransactionService;
import org.khasanof.uicachingstrategy.service.transactionServices.AbstractTransactionService;
import org.springframework.stereotype.Service;

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
    public List<TransactionEntity> getAllTransactionsByDates(String cardNumber, LocalDateTime from, LocalDateTime to) {
        return super.getAllTransactions(cardNumber, from, to);
    }

    @Override
    public String getCardNumber() {
        return cardNumber;
    }
}
