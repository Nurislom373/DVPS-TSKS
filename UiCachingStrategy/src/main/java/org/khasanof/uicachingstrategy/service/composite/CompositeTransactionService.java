package org.khasanof.uicachingstrategy.service.composite;

import jakarta.annotation.PostConstruct;
import org.khasanof.uicachingstrategy.domain.TransactionEntity;
import org.khasanof.uicachingstrategy.service.context.AnnotationContextTransactionService;
import org.khasanof.uicachingstrategy.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Author: Nurislom
 * <br/>
 * Date: 3/9/2023
 * <br/>
 * Time: 9:18 PM
 * <br/>
 * Package: org.khasanof.uicachingstrategy.service
 */
@Service
public class CompositeTransactionService implements TransactionService {

    private final List<TransactionService> services = new ArrayList<>();

    @Autowired
    private AnnotationContextTransactionService contextTransactionServices;

    @PostConstruct
    public void afterPropertiesSet() {
        List<TransactionService> list = contextTransactionServices.getServices();
        System.out.println("list = " + list);
        services.addAll(list);
    }

    @Override
    public List<TransactionEntity> getAllTransactionsByDates(String cardNumber, LocalDateTime from, LocalDateTime to) {
        checkCardNumber(cardNumber);
        return services.stream().map(o -> o.getAllTransactionsByDates(cardNumber, from, to))
                .flatMap(Collection::stream)
                .toList();
    }

    private void checkCardNumber(String cardNumber) {
        if (!cardNumber.equals("*")) throw new RuntimeException("Invalid Composite Card!");
    }
}
