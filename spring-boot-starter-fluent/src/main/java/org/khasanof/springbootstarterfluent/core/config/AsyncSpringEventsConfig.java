package org.khasanof.springbootstarterfluent.core.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ApplicationEventMulticaster;
import org.springframework.context.event.SimpleApplicationEventMulticaster;

import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;

/**
 * @author Nurislom
 * @see org.khasanof.springbootstarterfluent.core.config
 * @since 9/16/2023 11:13 PM
 */
@Configuration
public class AsyncSpringEventsConfig {

    @Bean(name = "applicationEventMulticaster")
    public ApplicationEventMulticaster simpleApplicationEventMulticaster() {
        SimpleApplicationEventMulticaster eventMulticaster = new SimpleApplicationEventMulticaster();
        eventMulticaster.setTaskExecutor(Executors.newSingleThreadExecutor());
        return eventMulticaster;
    }

}
