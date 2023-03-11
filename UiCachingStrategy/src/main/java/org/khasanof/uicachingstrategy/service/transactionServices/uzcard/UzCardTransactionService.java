package org.khasanof.uicachingstrategy.service.transactionServices.uzcard;

import jakarta.annotation.PostConstruct;
import org.khasanof.uicachingstrategy.annotation.TransactionType;
import org.khasanof.uicachingstrategy.data.TransactionMockData;
import org.khasanof.uicachingstrategy.domain.TransactionEntity;
import org.khasanof.uicachingstrategy.service.TransactionService;
import org.khasanof.uicachingstrategy.service.context.FieldContextTransactionService;
import org.khasanof.uicachingstrategy.service.context.MethodContextTransactionService;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

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
public class UzCardTransactionService implements TransactionService,
        FieldContextTransactionService, MethodContextTransactionService {

    private final String cardNumber = "8600";

    private List<TransactionEntity> list = new ArrayList<>();

    private final TransactionMockData data = new TransactionMockData();

    @PostConstruct
    public void postConstruct() {
        final String path = "/data/mock_transactions_uzcard.json";
        List<TransactionEntity> mockList = data.getMockList(path, cardNumber);
        list.addAll(mockList);
    }

    @Override
    public List<TransactionEntity> getAllTransactionsByDates(String cardNumber, LocalDateTime from, LocalDateTime to) {

        Predicate<TransactionEntity> equalPredicate = (f) -> f.getFromCard().equals(cardNumber)
                || f.getToCard().equals(cardNumber);

        Predicate<TransactionEntity> betweenPredicate = (f) -> f.getCreatedAt().isAfter(from)
                && f.getCreatedAt().isBefore(to);

        if (cardNumber.equals("*"))
            return list.stream().filter(betweenPredicate).toList();
        return list.stream().filter(equalPredicate.and(betweenPredicate))
                .toList();
    }

    public boolean isEmpty() {
        return list.isEmpty();
    }

    @Override
    public String getCardNumber() {
        return cardNumber;
    }
}
