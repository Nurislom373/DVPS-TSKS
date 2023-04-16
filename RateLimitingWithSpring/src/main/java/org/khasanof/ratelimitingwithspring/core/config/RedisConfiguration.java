package org.khasanof.ratelimitingwithspring.core.config;

import org.khasanof.ratelimitingwithspring.core.limiting.RateLimiting;
import org.khasanof.ratelimitingwithspring.domain.ApiEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.Map;

@Configuration
@EnableCaching
public class RedisConfiguration {

    @Value("${spring.data.redis.host}")
    private String REDIS_HOSTNAME;

    @Value("${spring.data.redis.port}")
    private Integer REDIS_PORT;

    @Bean
    public LettuceConnectionFactory connectionFactory() {
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();
        configuration.setHostName(REDIS_HOSTNAME);
        configuration.setPort(REDIS_PORT);
        return new LettuceConnectionFactory(configuration, lettuceClientConfiguration());
    }

    @Bean
    public LettuceClientConfiguration lettuceClientConfiguration() {
        return LettuceClientConfiguration.builder().build();
    }

    @Bean
    public RedisTemplate<String, Map<ApiEntity, RateLimiting>> redisTemplate() {
        final RedisTemplate<String, Map<ApiEntity, RateLimiting>> redisTemplate = new RedisTemplate<>();
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new GenericToStringSerializer<>(String.class));
        redisTemplate.setHashValueSerializer(new JdkSerializationRedisSerializer());
        redisTemplate.setConnectionFactory(connectionFactory());
        return redisTemplate;
    }

}
