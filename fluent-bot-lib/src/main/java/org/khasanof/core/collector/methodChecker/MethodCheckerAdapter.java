package org.khasanof.core.collector.methodChecker;

import org.khasanof.core.config.Config;
import org.khasanof.core.config.FluentConfig;
import org.khasanof.core.enums.ProcessType;
import org.khasanof.core.utils.AnnotationUtils;
import org.khasanof.core.utils.MethodUtils;
import org.khasanof.core.utils.ReflectionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author Nurislom
 * @see org.khasanof.core.collector.methodChecker
 * @since 19.07.2023 21:40
 */
public class MethodCheckerAdapter implements Config {

    private static final MethodCheckerAdapter instance = new MethodCheckerAdapter();
    private final FluentConfig fluentConfig = FluentConfig.getInstance();
    private final List<AbstractMethodChecker> methodCheckers = new ArrayList<>();

    public boolean valid(Method method) {
        AbstractMethodChecker abstractMethodChecker = methodCheckers.stream()
                .filter(methodChecker -> matchChecker(method, methodChecker))
                .findFirst().orElse(null);
        if (Objects.nonNull(abstractMethodChecker)) {
            return abstractMethodChecker.valid(method);
        }
        return false;
    }

    private boolean matchChecker(Method method, AbstractMethodChecker methodChecker) {
        return AnnotationUtils.hasAnnotation(method, methodChecker.getType(), methodChecker.hasSuperAnnotation());
    }

    @Override
    public void runnable() {
        ProcessType processType = fluentConfig.getConfig().getProcessType();
        List<AbstractMethodChecker> list = ReflectionUtils.getSubTypesObject(AbstractMethodChecker.class);
        Class<? extends Annotation> processToType = MethodUtils.processToType(processType);
        list.forEach(abstractMethodChecker -> {
            if (abstractMethodChecker.hasAny()) {
                methodCheckers.add(abstractMethodChecker);
            } else {
                if (Objects.isNull(processToType)) {
                    methodCheckers.add(abstractMethodChecker);
                } else if (abstractMethodChecker.getType().equals(processToType)) {
                    methodCheckers.add(abstractMethodChecker);
                }
            }
        });
    }

    @Override
    public ProcessType processType() {
        return ProcessType.BOTH;
    }

    public static MethodCheckerAdapter getInstance() {
        return instance;
    }
}
