package org.khasanof.ratelimitingwithspring.core.config;

import org.khasanof.ratelimitingwithspring.core.common.CommonLimitsService;
import org.khasanof.ratelimitingwithspring.core.RateLimitInterceptor;
import org.khasanof.ratelimitingwithspring.core.common.load.genericLoad.RSLimitLoadPostConstruct;
import org.khasanof.ratelimitingwithspring.core.strategy.limit.classes.RSLimit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    @Autowired
    private RateLimitInterceptor interceptor;

    @Autowired
    private RSLimitLoadPostConstruct rsLimitLoadPostConstruct;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(interceptor)
                .addPathPatterns(rsLimitLoadPostConstruct.getList().stream()
                        .map(RSLimit::getUrl).toList());
    }
}
