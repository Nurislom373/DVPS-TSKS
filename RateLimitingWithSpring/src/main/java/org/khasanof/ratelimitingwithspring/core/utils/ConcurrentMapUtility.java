package org.khasanof.ratelimitingwithspring.core.utils;

import org.khasanof.ratelimitingwithspring.core.limiting.PTA;
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
@Component
public class ConcurrentMapUtility {

    private final ConcurrentHashMap<String, Map<PTA, RateLimiting>> map = new ConcurrentHashMap<>();

    public void add(String key, Map<PTA, RateLimiting> limitingMap) {
        map.putIfAbsent(key, limitingMap);
    }

    public Map<PTA, RateLimiting> get(String key) {
        return map.get(key);
    }

}
