package org.khasanof.ratelimitingwithspring.cache.redis;

import lombok.RequiredArgsConstructor;
import org.khasanof.ratelimitingwithspring.cache.ehcache.MergeValue;
import org.khasanof.ratelimitingwithspring.core.RateLimiting;
import org.khasanof.ratelimitingwithspring.core.common.search.classes.PTA;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class RedisUtility {

    @Autowired
    private CacheManager cacheManager;
    private Cache cache;

    public void addValues(String key, Map<PTA, RateLimiting> limitingMap) {
        cache = cacheManager.getCache("limits");
        assert cache != null;
        cache.putIfAbsent(key, new MergeValue(key, limitingMap));
    }

    public Optional<Map<PTA, RateLimiting>> getValue(String key) {
        return Optional.ofNullable(cache.get(key, MergeValue.class)
                .getMap());
    }
}
