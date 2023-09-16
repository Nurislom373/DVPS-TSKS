package org.khasanof.springbootstarterfluent.core.collector.questMethod;

import org.khasanof.springbootstarterfluent.core.collector.GenericMethodContext;
import org.khasanof.springbootstarterfluent.core.collector.questMethod.impls.AsyncSimpleQuestMethod;
import org.khasanof.springbootstarterfluent.core.collector.questMethod.impls.StateQuestMethod;
import org.khasanof.springbootstarterfluent.core.enums.HandleClasses;
import org.khasanof.springbootstarterfluent.core.executors.matcher.CompositeMatcher;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * @author Nurislom
 * @see org.khasanof.springbootstarterfluent.core.collector.questMethod
 * @since 8/8/2023 8:47 PM
 */
@Configuration
@ConditionalOnClass(QuestMethod.class)
public class QuestConfiguration {

    @Bean
    QuestMethod<HandleClasses> asyncSimpleQuestMethod(GenericMethodContext<HandleClasses, Map<Method, Object>> methodContext, CompositeMatcher matcher) {
        return new AsyncSimpleQuestMethod(methodContext, matcher);
    }

    @Bean
    QuestMethod<Enum> asyncStateQuestMethod(GenericMethodContext<Enum, Map.Entry<Method, Object>> methodContext) {
        return new StateQuestMethod(methodContext);
    }

}
