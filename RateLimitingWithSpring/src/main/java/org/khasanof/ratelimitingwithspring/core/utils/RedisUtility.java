package org.khasanof.ratelimitingwithspring.core.utils;

import org.khasanof.ratelimitingwithspring.core.domain.Api;
import org.khasanof.ratelimitingwithspring.core.limiting.RateLimiting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Component
public class RedisUtility {

    @Autowired
    private RedisTemplate<String, Map<Api, RateLimiting>> redisTemplate;

    public void addValues(String key, Map<Api, RateLimiting> limitingMap) {
        // add validator
        Map<Api, RateLimiting> keyMap = redisTemplate.opsForValue().get(key);
        if (Objects.nonNull(keyMap)) {
            keyMap.putAll(limitingMap);
            redisTemplate.opsForValue().set(key, keyMap);
        } else {
            redisTemplate.opsForValue().set(key, limitingMap);
        }
    }

    public Optional<Map<Api, RateLimiting>> getValue(String key) {
        return Optional.ofNullable(redisTemplate.opsForValue().get(key));
    }
}
