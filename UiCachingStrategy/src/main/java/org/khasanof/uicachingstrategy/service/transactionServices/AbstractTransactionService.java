package org.khasanof.uicachingstrategy.service.transactionServices;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.khasanof.uicachingstrategy.data.TransactionMockData;
import org.khasanof.uicachingstrategy.domain.TransactionEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

/**
 * Author: Nurislom
 * <br/>
 * Date: 3/11/2023
 * <br/>
 * Time: 11:34 AM
 * <br/>
 * Package: org.khasanof.uicachingstrategy.service.transactionServices
 */
@RequiredArgsConstructor
public abstract class AbstractTransactionService {

    private List<TransactionEntity> list = new ArrayList<>();

    protected final TransactionMockData data;

    @PostConstruct
    protected abstract void afterPropertiesSet();

    protected List<TransactionEntity> getAllTransactions(String cardNumber, LocalDateTime from, LocalDateTime to) {
        Predicate<TransactionEntity> equalPredicate = (f) -> f.getFromCard().equals(cardNumber)
                || f.getToCard().equals(cardNumber);

        Predicate<TransactionEntity> betweenPredicate = (f) -> f.getCreatedAt().isAfter(from)
                && f.getCreatedAt().isBefore(to);

        if (cardNumber.equals("*"))
            return list.stream().filter(betweenPredicate).toList();
        return list.stream().filter(equalPredicate.and(betweenPredicate))
                .toList();
    }

    protected void addList(List<TransactionEntity> transactionEntities) {
        list.addAll(transactionEntities);
    }

    protected boolean isEmpty() {
        return list.isEmpty();
    }

}
