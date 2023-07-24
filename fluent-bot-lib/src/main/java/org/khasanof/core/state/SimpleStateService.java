package org.khasanof.core.state;

import lombok.SneakyThrows;
import org.khasanof.core.config.Config;
import org.khasanof.core.enums.ProcessType;
import org.khasanof.core.utils.ReflectionUtils;
import org.khasanof.main.inferaces.state.StateConfiguration;
import org.khasanof.main.inferaces.state.StateService;
import org.reflections.Reflections;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * @author Nurislom
 * @see org.khasanof.core.state
 * @since 09.07.2023 18:13
 */
public class SimpleStateService implements StateService, Config {

    private final static SimpleStateService instance = new SimpleStateService();
    private final List<String> states = new ArrayList<>();
    private final Reflections reflections = ReflectionUtils.getReflections(false);

    @Override
    @SneakyThrows
    public void registerState() {
        Set<Class<? extends StateConfiguration>> classes = reflections.getSubTypesOf(StateConfiguration.class);
        if (classes.size() != 1) {
            throw new RuntimeException("StateConfiguration must be one implement!");
        }
        Class<? extends StateConfiguration> next = classes.iterator().next();
        if (!checkInstance(next)) {
            throw new RuntimeException("is abstract cannot create instance!");
        }
        Constructor<?> defaultConstructor = getDefaultConstructor(next.getDeclaredConstructors());
        if (defaultConstructor == null) {
            throw new RuntimeException("default constructor not found!");
        }
        defaultConstructor.setAccessible(true);
        StateConfiguration stateConfiguration = (StateConfiguration) defaultConstructor.newInstance();
        states.addAll(stateConfiguration.states());
    }

    @Override
    public boolean checkState(String value) {
        return states.contains(value);
    }

    private Constructor<?> getDefaultConstructor(Constructor<?>[] constructors) {
        return Arrays.stream(constructors)
                .filter(constructor -> constructor.getParameterCount() == 0)
                .findFirst().orElse(null);
    }

    private boolean checkInstance(Class<? extends StateConfiguration> aClass) {
        boolean isInterface = false, isAbstract = false;
        if (aClass.isInterface()) {
            isInterface = true;
        }
        if (Modifier.isAbstract(aClass.getModifiers())) {
            isAbstract = true;
        }
        return !isInterface && !isAbstract;
    }

    public static SimpleStateService getInstance() {
        return instance;
    }

    @Override
    public void runnable() {
        registerState();
    }

    @Override
    public ProcessType processType() {
        return ProcessType.STATE;
    }
}
