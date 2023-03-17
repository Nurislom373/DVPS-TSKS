package org.khasanof.service.v2.context;

import org.khasanof.annotation.TransactionType;
import org.khasanof.service.v2.transactionServices.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.reactive.context.GenericReactiveWebApplicationContext;
import org.springframework.stereotype.Service;

import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Author: Nurislom
 * <br/>
 * Date: 3/7/2023
 * <br/>
 * Time: 4:01 PM
 * <br/>
 * Package: org.khasanof.uicachingstrategy.service
 */
@Service
public class AnnotationContextTransactionService implements ContextTransactionService {

    @Autowired
    private GenericReactiveWebApplicationContext context;

    @Override
    public TransactionService getService(String cardNumber) {
        return (TransactionService) context.getBean(getClassBean(cardNumber));
    }

    @Override
    public Map<String, TransactionService> getServices() {
        System.out.println(context);
        return getTransactionTypes().stream()
                .map(Object::getClass)
                .map(o -> new AbstractMap.SimpleEntry<>(getCardNumberObject(o),
                        (TransactionService) context.getBean(o)))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private Class<?> getClassBean(String cardNumber) {
        return getTransactionTypes().stream()
                .filter(o -> cardNumber.startsWith(getCardNumber(o)))
                .map(Object::getClass).findFirst()
                .orElseThrow(RuntimeException::new);
    }

    private List<Object> getTransactionTypes() {
        return context.getBeansWithAnnotation(TransactionType.class)
            .values().stream().peek(System.out::println)
            .collect(Collectors.toList());
    }

    private String getCardNumber(Object o) {
        return o.getClass().getAnnotation(TransactionType.class)
                .cardNumber();
    }

    private String getCardNumberObject(Class<?> aClass) {
        return aClass.getAnnotation(TransactionType.class).cardNumber();
    }
}
