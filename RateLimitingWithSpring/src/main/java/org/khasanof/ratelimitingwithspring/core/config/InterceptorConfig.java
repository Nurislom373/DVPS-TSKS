package org.khasanof.ratelimitingwithspring.core.config;

import org.khasanof.ratelimitingwithspring.core.CommonLimitsAdapter;
import org.khasanof.ratelimitingwithspring.core.RateLimitInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    @Autowired
    private RateLimitInterceptor interceptor;

    @Autowired
    private CommonLimitsAdapter limitsAdapter;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(interceptor)
                .addPathPatterns(limitsAdapter.getLimitApis());
    }
}
