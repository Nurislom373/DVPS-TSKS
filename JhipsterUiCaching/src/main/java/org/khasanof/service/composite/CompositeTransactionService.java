package org.khasanof.service.composite;

import org.khasanof.domain.transaction.Transaction;
import org.khasanof.service.TransactionService;
import org.khasanof.service.context.AnnotationContextTransactionService;
import org.khasanof.service.context.SpringFieldContextTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
    private SpringFieldContextTransactionService contextTransactionServices;

    @PostConstruct
    public void afterPropertiesSet() {
        Map<String, TransactionService> map = contextTransactionServices.getServices();
        services.addAll(map.values());
    }

    @Override
    public Flux<Transaction> getAllTransactionsByDates(String cardNumber, LocalDateTime from, LocalDateTime to) {
        checkCardNumber(cardNumber);
        return Flux.fromIterable(services)
                .flatMap(s -> s.getAllTransactionsByDates(cardNumber, from, to));
    }

    private void checkCardNumber(String cardNumber) {
        if (!cardNumber.equals("*")) throw new RuntimeException("Invalid Composite Card!");
    }
}
