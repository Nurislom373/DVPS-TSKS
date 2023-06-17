package org.khasanof.ratelimitingwithspring.cache;

import org.khasanof.ratelimitingwithspring.core.common.search.classes.PTA;
import org.khasanof.ratelimitingwithspring.core.limiting.LocalRateLimiting;

import java.util.Map;
import java.util.Optional;

/**
 * Author: Nurislom
 * <br/>
 * Date: 27.05.2023
 * <br/>
 * Time: 10:59
 * <br/>
 * Package: org.khasanof.ratelimitingwithspring.cache
 * <br/>
 *
 * Cache Get All With Map
 */
public interface CacheUtilityGD {

    Map<String, Map<PTA, LocalRateLimiting>> getAll();

    Optional<MergeValue> getValues(String key);



}
