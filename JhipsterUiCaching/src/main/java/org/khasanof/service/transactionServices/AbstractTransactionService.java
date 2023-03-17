package org.khasanof.service.transactionServices;

import org.khasanof.data.TransactionMockData;
import org.khasanof.domain.transaction.Transaction;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Author: Nurislom
 * <br/>
 * Date: 3/11/2023
 * <br/>
 * Time: 11:34 AM
 * <br/>
 * Package: org.khasanof.uicachingstrategy.service.transactionServices
 */
public abstract class AbstractTransactionService {

    private List<Transaction> list = new ArrayList<>();

    protected final TransactionMockData data;

    protected AbstractTransactionService(TransactionMockData data) {
        this.data = data;
    }

    @PostConstruct
    protected abstract void afterPropertiesSet();

    protected List<Transaction> getAllTransactions(String cardNumber, LocalDateTime from, LocalDateTime to) {
        Predicate<Transaction> equalPredicate = (f) -> f.getFromCard().equals(cardNumber)
                || f.getToCard().equals(cardNumber);

        Predicate<Transaction> betweenPredicate = (f) -> f.getCreatedAt().isAfter(from)
                && f.getCreatedAt().isBefore(to);

        Predicate<Transaction> startWith = (f) -> f.getFromCard().startsWith(cardNumber)
                || f.getToCard().startsWith(cardNumber);

        if (cardNumber.length() == 4) {
            return list.stream().filter(betweenPredicate.and(startWith))
                .collect(Collectors.toList());
        }
        return list.stream().filter(equalPredicate.and(betweenPredicate))
            .collect(Collectors.toList());
    }

    protected void addList(List<Transaction> transactionEntities) {
        list.addAll(transactionEntities);
    }

    protected boolean isEmpty() {
        return list.isEmpty();
    }

}
