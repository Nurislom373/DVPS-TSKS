package org.khasanof.springbootstarterfluent.core.config;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author Nurislom
 * @see org.khasanof.core.config
 * @since 05.07.2023 0:29
 */
@Component(value = FluentConfigRunner.NAME)
public class FluentConfigRunner implements InitializingBean {

    public static final String NAME = "commonFluentConfigRunner";

    private final ApplicationContext applicationContext;

    public FluentConfigRunner(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Map<String, Config> beans = applicationContext.getBeansOfType(Config.class);
        beans.values().forEach(Config::runnable);
    }
}
