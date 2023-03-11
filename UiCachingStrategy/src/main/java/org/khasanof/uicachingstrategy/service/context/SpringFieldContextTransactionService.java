package org.khasanof.uicachingstrategy.service.context;

import org.khasanof.uicachingstrategy.service.TransactionService;
import org.khasanof.uicachingstrategy.service.composite.CompositeTransactionService;
import org.khasanof.uicachingstrategy.service.context.ContextTransactionService;
import org.khasanof.uicachingstrategy.service.context.FieldContextTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

/**
 * Author: Nurislom
 * <br/>
 * Date: 3/10/2023
 * <br/>
 * Time: 7:24 PM
 * <br/>
 * Package: org.khasanof.uicachingstrategy.service
 */
@Service
public class SpringFieldContextTransactionService implements ContextTransactionService {

    @Autowired
    private ApplicationContext context;

    @Override
    public TransactionService getService(String cardNumber) {
        if (cardNumber.equals("*")) return context.getBean(CompositeTransactionService.class);
        return (TransactionService) context.getBean(getClassBeanImperative(cardNumber));
    }

    @Override
    public List<TransactionService> getServices() {
        return getBeans().stream()
                .map(o -> (TransactionService) o).toList();
    }

    private Class<?> getClassBeanDeclarative(String cardNumber) {
        return getBeans().stream().flatMap(o -> Arrays.stream(o.getClass().getDeclaredFields())
                        .filter(field -> field.getName().equals("cardNumber"))
                        .peek(f -> f.setAccessible(true))
                        .map(f -> getFieldValue(f, o))
                        .filter(cardNumber::startsWith)
                        .map(Object::getClass))
                .findFirst().orElseThrow(RuntimeException::new);
    }

    private Class<?> getClassBeanImperative(String cardNumber) {
        for (Object o : getBeans()) {
            Field[] declaredFields = o.getClass().getDeclaredFields();
            for (Field declaredField : declaredFields) {
                if (declaredField.getName().equals("cardNumber")) {
                    declaredField.setAccessible(true);
                    String fieldValue = getFieldValue(declaredField, o);
                    System.out.println("fieldValue = " + fieldValue);
                    if (cardNumber.startsWith(fieldValue)) {
                        return o.getClass();
                    }
                }
            }
        }

        throw new RuntimeException("Class Not Found Exception!");
    }

    private List<Object> getBeans() {
        return Arrays.stream(context.getBeanDefinitionNames()).map(b -> context.getBean(b))
                .filter(f -> f instanceof FieldContextTransactionService)
                .toList();
    }

    private String getFieldValue(Field field, Object target) {
        try {
            return (String) field.get(target);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
