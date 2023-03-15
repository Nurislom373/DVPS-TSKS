package org.khasanof.reactiveuicaching.service.context;

import org.khasanof.reactiveuicaching.annotation.TransactionType;
import org.khasanof.reactiveuicaching.service.TransactionService;
import org.khasanof.reactiveuicaching.service.composite.CompositeTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
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
    private ApplicationContext context;

    @Override
    public TransactionService getService(String cardNumber) {
        return (TransactionService) context.getBean(getClassBean(cardNumber));
    }

    @Override
    public Map<String, TransactionService> getServices() {
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
                .values().stream().toList();
    }

    private String getCardNumber(Object o) {
        return o.getClass().getAnnotation(TransactionType.class)
                .cardNumber();
    }

    private String getCardNumberObject(Class<?> aClass) {
        return aClass.getAnnotation(TransactionType.class).cardNumber();
    }
}
