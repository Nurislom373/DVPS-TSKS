package org.khasanof.uicachingstrategy.service.humo;

import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.khasanof.uicachingstrategy.annotation.TransactionType;
import org.khasanof.uicachingstrategy.data.TransactionData;
import org.khasanof.uicachingstrategy.domain.TransactionEntity;
import org.khasanof.uicachingstrategy.service.TransactionService;
import org.khasanof.uicachingstrategy.service.uzcard.UzCardTransactionService;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

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
@TransactionType(cardNumber = "9860", name = "UzCard")
public class HumoTransactionService implements TransactionService {

    private List<TransactionEntity> list = new ArrayList<>();

    private final TransactionData data = new TransactionData();

    @PostConstruct
    public void postConstruct() {
        List<TransactionEntity> mockList = data.getMockList("/data/transaction2.json", "9860");
        list.addAll(mockList);
    }

    @Override
    public List<TransactionEntity> getAllTransactionsByDates(String cardNumber, LocalDateTime from, LocalDateTime to) {
        Predicate<TransactionEntity> equalPredicate = (f) -> f.getFromCard().equals(cardNumber)
                || f.getToCard().equals(cardNumber);

        Predicate<TransactionEntity> betweenPredicate = (f) -> f.getCreatedAt().isAfter(from)
                && f.getCreatedAt().isBefore(to);

        return list.stream()
                .filter(equalPredicate.and(betweenPredicate))
                .toList();
    }

    public boolean isEmpty() {
        return list.isEmpty();
    }
}
