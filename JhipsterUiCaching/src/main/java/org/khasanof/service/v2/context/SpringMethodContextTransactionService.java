package org.khasanof.service.v2.context;

import org.khasanof.service.v2.transactionServices.TransactionService;
import org.khasanof.service.v2.composite.CompositeTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.AbstractMap;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Author: Nurislom
 * <br/>
 * Date: 3/10/2023
 * <br/>
 * Time: 11:24 PM
 * <br/>
 * Package: org.khasanof.uicachingstrategy.service.context
 */
@Service
public class SpringMethodContextTransactionService implements ContextTransactionService {

    @Autowired
    private ApplicationContext context;

    @Override
    public TransactionService getService(String cardNumber) {
        if (cardNumber.equals("*")) return context.getBean(CompositeTransactionService.class);
        return (TransactionService) context.getBean(getClassBean(cardNumber));
    }

    @Override
    public Map<String, TransactionService> getServices() {
        return getBeans().stream()
                .map(s -> new AbstractMap.SimpleEntry<>(getMethodValue((MethodContextTransactionService) s),
                        (TransactionService) s))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private Class<?> getClassBean(String cardNumber) {
        for (Object bean : getBeans()) {
            MethodContextTransactionService service = (MethodContextTransactionService) bean;
            if (methodEqualCardNumber(cardNumber, service)) return bean.getClass();
        }
        throw new RuntimeException("Class Not Found Exception!");
    }

    private List<Object> getBeans() {
        return Arrays.stream(context.getBeanDefinitionNames()).map(b -> context.getBean(b))
                .filter(f -> f instanceof MethodContextTransactionService)
                .collect(Collectors.toList());
    }

    private String getMethodValue(MethodContextTransactionService service) {
        return service.getCardNumber();
    }

    private boolean methodEqualCardNumber(String cardNumber, MethodContextTransactionService service) {
        return cardNumber.startsWith(service.getCardNumber());
    }
}
