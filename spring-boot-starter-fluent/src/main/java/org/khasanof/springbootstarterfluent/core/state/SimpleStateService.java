package org.khasanof.springbootstarterfluent.core.state;

import lombok.SneakyThrows;
import org.khasanof.springbootstarterfluent.core.utils.ReflectionUtils;
import org.khasanof.springbootstarterfluent.main.inferaces.state.StateService;
import org.reflections.Reflections;
import org.springframework.context.ApplicationContext;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * @author Nurislom
 * @see org.khasanof.core.state
 * @since 09.07.2023 18:13
 */
public class SimpleStateService implements StateService {

    private Class<? extends Enum> enumType;
    private ApplicationContext applicationContext;
    private static final SimpleStateService INSTANCE = new SimpleStateService();

    public static SimpleStateService getInstance() {
        return INSTANCE;
    }

    @Override
    @SneakyThrows
    public void registerState() {
//        Map<String, InitializingStateEnum> enumMap = applicationContext.getBeansOfType(InitializingStateEnum.class);
//        if (classes.size() != 1) {
//            throw new RuntimeException("InitializingStateEnum must be one implement!");
//        }
//        Class<? extends InitializingStateEnum> next = classes.iterator().next();
//        if (!checkInstance(next)) {
//            throw new RuntimeException("is abstract cannot create instance!");
//        }
//        Constructor<?> defaultConstructor = getDefaultConstructor(next.getDeclaredConstructors());
//        if (defaultConstructor == null) {
//            throw new RuntimeException("default constructor not found!");
//        }
//        defaultConstructor.setAccessible(true);
//        InitializingStateEnum initializingStateEnum = (InitializingStateEnum) defaultConstructor.newInstance();
//        enumType = initializingStateEnum.getType();
    }

    @Override
    public Class<? extends Enum> getType() {
        if (Objects.isNull(this.enumType)) {
            registerState();
        }
        return this.enumType;
    }

    private Constructor<?> getDefaultConstructor(Constructor<?>[] constructors) {
        return Arrays.stream(constructors)
                .filter(constructor -> constructor.getParameterCount() == 0)
                .findFirst().orElse(null);
    }

    private boolean checkInstance(Class<? extends InitializingStateEnum> aClass) {
        boolean isInterface = false, isAbstract = false;
        if (aClass.isInterface()) {
            isInterface = true;
        }
        if (Modifier.isAbstract(aClass.getModifiers())) {
            isAbstract = true;
        }
        return !isInterface && !isAbstract;
    }
}
