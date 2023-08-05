package org.khasanof.springbootstarterfluent.core.collector.methodChecker;

import org.khasanof.springbootstarterfluent.core.config.ApplicationProperties;
import org.khasanof.springbootstarterfluent.core.config.Config;
import org.khasanof.springbootstarterfluent.core.enums.ProcessType;
import org.khasanof.springbootstarterfluent.core.utils.AnnotationUtils;
import org.khasanof.springbootstarterfluent.core.utils.MethodUtils;
import org.khasanof.springbootstarterfluent.core.utils.ReflectionUtils;
import org.springframework.stereotype.Component;

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
@Component
public class MethodCheckerAdapter implements Config {

    private final ApplicationProperties.Bot bot;
    private final List<AbstractMethodChecker> methodCheckers = new ArrayList<>();

    public MethodCheckerAdapter(ApplicationProperties properties) {
        this.bot = properties.getBot();
    }

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
        ProcessType processType = bot.getProcessType();
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

}
