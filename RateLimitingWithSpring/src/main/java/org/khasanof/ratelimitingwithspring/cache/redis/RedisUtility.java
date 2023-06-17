package org.khasanof.ratelimitingwithspring.cache.redis;

import lombok.extern.slf4j.Slf4j;
import org.khasanof.ratelimitingwithspring.cache.CacheOperations;
import org.khasanof.ratelimitingwithspring.cache.MergeValue;
import org.khasanof.ratelimitingwithspring.core.common.search.classes.PTA;
import org.khasanof.ratelimitingwithspring.core.limiting.LocalRateLimiting;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.AbstractMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Component
public class RedisUtility implements CacheOperations {

    private final RedisTemplate<String, byte[]> redisTemplate;
    private final String CACHE_NAME = "limits";
    private final Cache cache;

    public RedisUtility(CacheManager cacheManager, RedisTemplate<String, byte[]> redisTemplate) {
        this.redisTemplate = redisTemplate;
        cache = cacheManager.getCache(CACHE_NAME);
    }

    @Override
    public void addValues(String key, Map<PTA, LocalRateLimiting> limitingMap) {
        Optional<MergeValue> optional = getValues(key);
        if (optional.isEmpty()) {
            cache.put(key, new MergeValue(limitingMap));
        } else {
            MergeValue value = optional.get();
            value.getMap().putAll(limitingMap);
            cache.put(key, value);
        }
    }

    @Override
    public void updateValues(String key, PTA pta, LocalRateLimiting limiting) {
        MergeValue value = getValues(key).orElseThrow();
        cache.evict(key);
        value.getMap().put(pta, limiting);
        cache.put(key, value);
    }

    @Override
    public boolean delete(String key) {
        return cache.evictIfPresent(key);
    }

    @Override
    public Optional<MergeValue> getValues(String key) {
        return Optional.ofNullable(cache.get(key, MergeValue.class));
    }

    @Override
    public Map<String, Map<PTA, LocalRateLimiting>> getAll() {
        return Objects.requireNonNull(redisTemplate.keys(CACHE_NAME + "*"))
                .parallelStream()
                .map((key) -> key.substring(8))
                .map(key -> new AbstractMap.SimpleEntry<>(key,
                        getValueWithoutOptional(key).getMap()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    @Override
    public void deleteAll() {
        cache.clear();
    }

    private MergeValue getValueWithoutOptional(String key) {
        return cache.get(key, MergeValue.class);
    }
}
