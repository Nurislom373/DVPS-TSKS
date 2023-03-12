package org.khasanof.reactiveuicaching.service.composite;

import jakarta.annotation.PostConstruct;
import org.khasanof.reactiveuicaching.domain.TransactionEntity;
import org.khasanof.reactiveuicaching.service.TransactionService;
import org.khasanof.reactiveuicaching.service.context.AnnotationContextTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
    public Flux<TransactionEntity> getAllTransactionsByDates(String cardNumber, LocalDateTime from, LocalDateTime to) {
        checkCardNumber(cardNumber);
        return Flux.fromIterable(services)
                .flatMap(s -> s.getAllTransactionsByDates(cardNumber, from, to));
    }

    private void checkCardNumber(String cardNumber) {
        if (!cardNumber.equals("*")) throw new RuntimeException("Invalid Composite Card!");
    }
}
