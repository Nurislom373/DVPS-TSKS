package org.khasanof.springbootstarterfluent.core.state.processor;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.khasanof.springbootstarterfluent.core.config.ApplicationConstants;
import org.khasanof.springbootstarterfluent.core.config.ApplicationProperties;
import org.khasanof.springbootstarterfluent.core.enums.ProcessType;
import org.khasanof.springbootstarterfluent.core.state.StateConfigurer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Objects;

/**
 * @author Nurislom
 * @see org.khasanof.core.state.processor
 * @since 23.07.2023 23:20
 */
@Slf4j
@Component
@ConditionalOnExpression(ApplicationConstants.STATE_PROCESSOR_EXPRESSION)
public class StateProcessor {

    private final ApplicationContext applicationContext;
    private final ApplicationProperties.Bot bot;

    public StateProcessor(ApplicationContext applicationContext, ApplicationProperties properties) {
        this.applicationContext = applicationContext;
        this.bot = properties.getBot();
    }

    @PostConstruct
    public void processor() {
        if (bot.getProcessType().equals(ProcessType.STATE)) {
            log.info("state processor start! ###");
            Map<String, StateConfigurer> configurerMap = applicationContext.getBeansOfType(StateConfigurer.class);
            if (configurerMap.size() != 1) {
                throw new RuntimeException("StateConfigurer must be one implement!");
            }
            StateConfigurer configurer = configurerMap.values().iterator().next();
            if (Objects.isNull(configurer.stateEnums()) || configurer.stateEnums().isEmpty()) {
                throw new RuntimeException("states is null or empty!");
            }
            StateCollector collector = applicationContext.getBean(StateCollector.NAME, StateCollector.class);
            collector.addAll(configurer.stateEnums());
        }
    }

}
