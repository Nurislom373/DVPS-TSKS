package org.khasanof.ratelimitingwithspring.cache;

import org.khasanof.ratelimitingwithspring.core.common.search.classes.PTA;
import org.khasanof.ratelimitingwithspring.core.limiting.LocalRateLimiting;

import java.util.Map;

/**
 * Author: Nurislom
 * <br/>
 * Date: 20.05.2023
 * <br/>
 * Time: 18:45
 * <br/>
 * Package: org.khasanof.ratelimitingwithspring.cache
 */
public interface CacheUtilityAUD {

    void addValues(String key, Map<PTA, LocalRateLimiting> limitingMap);

    void updateValues(String key, PTA pta, LocalRateLimiting limiting);

    boolean delete(String key);

    void deleteAll();

}
