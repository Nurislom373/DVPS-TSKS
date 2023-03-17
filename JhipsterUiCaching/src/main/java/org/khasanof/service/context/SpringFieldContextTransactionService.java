package org.khasanof.service.context;

import org.khasanof.service.TransactionService;
import org.khasanof.service.composite.CompositeTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
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
    public Map<String, TransactionService> getServices() {
        System.out.println(context);
        return getBeans().stream()
            .map(o -> new AbstractMap.SimpleEntry<>(getValue(o), (TransactionService) o))
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
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

    private String getValue(Object o) {
        System.out.println("-------------------------------------------------------------");
        System.out.println("                    " + o.getClass().getSimpleName());
        Field[] declaredFields = o.getClass().getDeclaredFields();
        for (Field declaredField : declaredFields) {
            System.out.println("declaredField = " + declaredField);
            if (declaredField.getName().equals("cardNumber")) {
                declaredField.setAccessible(true);
                System.out.println("declaredField = " + declaredField);
                System.out.println("o = " + o);
                return getFieldValue(declaredField, o);
            }
        }
        System.out.println("I have no CARD NUMBER: " + o.getClass().getSimpleName());
        throw new RuntimeException();
    }

    private List<Object> getBeans() {
        return Arrays.stream(context.getBeanNamesForType(FieldContextTransactionService.class))
            .peek(System.out::println)
            .map(b -> context.getBean(b))
            .collect(Collectors.toList());
    }

    private String getFieldValue(Field field, Object target) {
        try {
            System.out.println("f = " + field);
            System.out.println("t = " + target);
            return (String) field.get(target);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
