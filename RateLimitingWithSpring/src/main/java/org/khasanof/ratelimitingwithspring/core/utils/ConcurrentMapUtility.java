package org.khasanof.ratelimitingwithspring.core.utils;

import lombok.extern.slf4j.Slf4j;
import org.khasanof.ratelimitingwithspring.core.common.search.classes.PTA;
import org.khasanof.ratelimitingwithspring.core.limiting.RateLimiting;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Author: Nurislom
 * <br/>
 * Date: 4/19/2023
 * <br/>
 * Time: 12:23 AM
 * <br/>
 * Package: org.khasanof.ratelimitingwithspring.core.utils
 */
@Slf4j
@Component
public class ConcurrentMapUtility {

    private final ConcurrentHashMap<String, Map<PTA, RateLimiting>> map = new ConcurrentHashMap<>();

    public void add(String key, Map<PTA, RateLimiting> limitingMap) {
        map.putIfAbsent(key, limitingMap);
    }

    public Map<PTA, RateLimiting> get(String key) {
        return map.get(key);
    }

    public Map<String, Map<PTA, RateLimiting>> getAll() {
        return this.map;
    }

    public void showSize() {
        log.info("Show Limits Info => Count : {}", map.size());
    }

}
