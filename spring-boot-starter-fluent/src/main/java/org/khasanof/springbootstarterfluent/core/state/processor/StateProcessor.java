package org.khasanof.springbootstarterfluent.core.state.processor;

import lombok.extern.slf4j.Slf4j;
import org.khasanof.springbootstarterfluent.core.config.Config;
import org.khasanof.springbootstarterfluent.core.enums.ProcessType;
import org.khasanof.springbootstarterfluent.core.state.StateConfigurer;
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
public class StateProcessor implements Config {

    private final ApplicationContext applicationContext;

    public StateProcessor(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public void runnable() {
        log.info("state processor start! ###");
        Map<String, StateConfigurer> configurerMap = applicationContext.getBeansOfType(StateConfigurer.class);
        if (configurerMap.size() != 1) {
            throw new RuntimeException("StateConfigurer must be one implement!");
        }
        StateConfigurer configurer = configurerMap.values().iterator().next();
        if (Objects.isNull(configurer.stateEnums()) || configurer.stateEnums().isEmpty()) {
            throw new RuntimeException("states is null or empty!");
        }
        StateEnumsCollector collector = applicationContext.getBean(StateEnumsCollector.NAME, StateEnumsCollector.class);
        collector.addAll(configurer.stateEnums());
    }

    @Override
    public ProcessType processType() {
        return ProcessType.STATE;
    }
}
