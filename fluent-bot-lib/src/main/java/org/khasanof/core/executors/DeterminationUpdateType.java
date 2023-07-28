package org.khasanof.core.executors;

import org.khasanof.core.collector.Collector;
import org.khasanof.core.custom.BreakerForEach;
import org.khasanof.core.custom.FluentContext;
import org.khasanof.core.enums.HandleType;
import org.khasanof.core.enums.Proceed;
import org.khasanof.core.executors.determination.DeterminationService;
import org.khasanof.core.state.StateCore;
import org.khasanof.core.state.StateRepository;
import org.khasanof.core.utils.UpdateUtils;
import org.khasanof.main.annotation.extra.HandleState;
import org.khasanof.main.annotation.methods.HandleAny;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.BiConsumer;

/**
 * @author <a href="https://github.com/Nurislom373">Nurislom</a>
 * @see org.khasanof.core.executors
 * @since 24.06.2023 20:00
 */
public class DeterminationUpdateType {

    private final Collector collector;
    private final HandleAnyFunctionMatcher anyFunctionMatcher = new HandleAnyFunctionMatcher();
    private final StateRepository usersRepository = StateRepository.getInstance();
    private final DeterminationService determinationService;

    public DeterminationUpdateType(Collector collector) {
        this.collector = collector;
        this.determinationService = new DeterminationService(new ArrayList<>(
                List.of(collector, anyFunctionMatcher, usersRepository)));
    }

    public Map<Method, Class> determinationV2(Update update) {
        Map<Method, Class> methods = new LinkedHashMap<>();
        List<BiConsumer<Update, Map<Method, Class>>> list = determinationService.getDeterminationsByOrder();
        BreakerForEach.forEach(list.stream(), ((updateMapBiConsumer, breaker) -> {
            if (!FluentContext.determinationServiceBoolean.get()) {
                updateMapBiConsumer.accept(update, methods);
            } else {
                FluentContext.determinationServiceBoolean.set(true);
            }
        }), () -> FluentContext.determinationServiceBoolean.set(false));
        return methods;
    }

    public Map<Method, Class> determination(Update update) {
        Map<Method, Class> methods = new LinkedHashMap<>();
        if (collector.hasHandle(HandleAny.class)) {
            Map.Entry<HandleType, Object> typeEntry = handleTypeEntry(update);
            Map.Entry<Method, Class> handleAnyMethod = collector.getHandleAnyMethod(typeEntry.getKey());
            if (Objects.nonNull(handleAnyMethod)) {
                methods.put(handleAnyMethod.getKey(), handleAnyMethod.getValue());
                Proceed proceed = handleAnyMethod.getKey().getAnnotation(HandleAny.class)
                        .proceed();
                if (proceed.equals(Proceed.PROCEED)) {
                    setStateMethod(update, methods);
                    pushMatchMethod(update, methods);
                }
            } else {
                setStateMethod(update, methods);
                pushMatchMethod(update, methods);
            }
        } else {
            setStateMethod(update, methods);
            pushMatchMethod(update, methods);
        }
        return methods;
    }

    private void setStateMethod(Update update, Map<Method, Class> methods) {
        Long id = UpdateUtils.getUserId(update);
        boolean hassed = usersRepository.hasUserId(id);
        if (hassed) {
            addStateMethod(methods, id);
        } else {
            usersRepository.addUser(UpdateUtils.getFrom(update));
            addStateMethod(methods, id);
        }
    }

    private void addStateMethod(Map<Method, Class> methods, Long id) {
        int count = usersRepository.count();
        System.out.println("count = " + count);
        Enum state = usersRepository.getState(id);
        System.out.println("state = " + state);
        if (Objects.nonNull(state)) {
            Map.Entry<Method, Class> classEntry = collector.getMethodValueAnn(state, HandleState.class);
            if (Objects.nonNull(classEntry)) {
                methods.put(classEntry.getKey(), classEntry.getValue());
            }
        }
    }

    private void pushMatchMethod(Update update, Map<Method, Class> methods) {
        Map.Entry<Object, Class<? extends Annotation>> entry = switchAndGet(update);
        if (Objects.nonNull(entry)) {
            Map.Entry<Method, Class> classEntry = collector.getMethodValueAnn(entry.getKey(), entry.getValue());
            if (Objects.nonNull(classEntry))
                methods.put(classEntry.getKey(), classEntry.getValue());
            else
                System.out.println("Method not found!");
        }
    }

    private Map.Entry<Object, Class<? extends Annotation>> switchAndGet(Update update) {
        Map.Entry<HandleType, Object> objectEntry = anyFunctionMatcher.matchFunctions(update);
        if (HandleType.hasHandleAnnotation(objectEntry.getKey())) {
            return Map.entry(objectEntry.getValue(), objectEntry.getKey().getHandleClasses().getType());
        }
        return null;
    }

    private Map.Entry<HandleType, Object> handleTypeEntry(Update update) {
        return anyFunctionMatcher.matchFunctions(update);
    }

}
