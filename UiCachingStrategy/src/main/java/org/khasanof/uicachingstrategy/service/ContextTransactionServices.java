package org.khasanof.uicachingstrategy.service;

import org.khasanof.uicachingstrategy.annotation.TransactionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

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
public class ContextTransactionServices {

    @Autowired
    private ApplicationContext context;

    public TransactionService getService(String cardNumber) {
        return (TransactionService) context.getBean(getClassBean(cardNumber));
    }

    public List<TransactionService> getServices() {
        return getTransactionTypes().stream().map(Object::getClass)
                .map(o -> (TransactionService) context.getBean(o))
                .toList();
    }

    private Class<?> getClassBean(String cardNumber) {
        return getTransactionTypes().stream()
                .filter(o -> cardNumber.startsWith(getCardNumber(o)))
                .map(Object::getClass).findFirst()
                .orElseThrow(RuntimeException::new);
    }

    private List<Object> getTransactionTypes() {
        return context.getBeansWithAnnotation(TransactionType.class).values()
                .stream().toList();
    }

    private String getCardNumber(Object o) {
        return o.getClass().getAnnotation(TransactionType.class)
                .cardNumber();
    }
}
