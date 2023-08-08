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
@Component(value = CommonFluentConfigRunner.NAME)
public class CommonFluentConfigRunner implements InitializingBean {

    public static final String NAME = "commonFluentConfigRunner";

    private final ApplicationContext applicationContext;

    public CommonFluentConfigRunner(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Map<String, Config> beans = applicationContext.getBeansOfType(Config.class);
        beans.values().forEach(Config::runnable);
    }
}
