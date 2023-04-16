package org.khasanof.ratelimitingwithspring.core.utils;

import com.google.gson.Gson;
import org.khasanof.ratelimitingwithspring.core.limiting.RateLimiting;
import org.khasanof.ratelimitingwithspring.domain.ApiEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Map;
import java.util.Optional;

@Service
public class RedisUtility {

    @Autowired
    private RedisTemplate<String, Map<ApiEntity, RateLimiting>> redisTemplate;

    @Autowired
    private BaseUtils baseUtils;

    public void addValue(String key, Map<ApiEntity, RateLimiting> limitingMap) {
        redisTemplate.opsForValue().set(key, limitingMap);
        redisTemplate.expire(key, Duration.ofDays(baseUtils.limitMapToDays(limitingMap)));
    }

    public Optional<Map<ApiEntity, RateLimiting>> getValue(String key) {
        return Optional.ofNullable(redisTemplate.opsForValue().get(key));
    }

    public boolean deleteValue(String key) {
        return redisTemplate.delete(key);
    }
}
