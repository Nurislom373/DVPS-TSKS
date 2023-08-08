package org.khasanof.springbootstarterfluent.core.collector.questMethod;

import org.khasanof.springbootstarterfluent.core.collector.CommonMethodAdapter;
import org.khasanof.springbootstarterfluent.core.collector.questMethod.impls.AsyncQuestMethod;
import org.khasanof.springbootstarterfluent.core.collector.questMethod.impls.SimpleQuestMethod;
import org.khasanof.springbootstarterfluent.core.executors.matcher.CompositeMatcher;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Nurislom
 * @see org.khasanof.springbootstarterfluent.core.collector.questMethod
 * @since 8/8/2023 8:47 PM
 */
@Configuration
@ConditionalOnClass(QuestMethod.class)
public class QuestConfiguration {

    @Bean
    QuestMethod asyncQuestMethod(CommonMethodAdapter commonMethodAdapter, CompositeMatcher matcher) {
        return new AsyncQuestMethod(commonMethodAdapter, matcher);
    }

    @Bean
    @ConditionalOnBean(name = "asyncQuestMethod")
    @ConditionalOnMissingBean
    QuestMethod simpleQuestMethod(CommonMethodAdapter commonMethodAdapter, CompositeMatcher matcher) {
        return new SimpleQuestMethod(commonMethodAdapter, matcher);
    }

}
