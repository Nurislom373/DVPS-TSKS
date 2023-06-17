package org.khasanof.ratelimitingwithspring.core.common.runtime.delete;

import org.khasanof.ratelimitingwithspring.core.RateLimiting;
import org.khasanof.ratelimitingwithspring.core.common.search.classes.PTA;
import org.khasanof.ratelimitingwithspring.core.limiting.LocalRateLimiting;

import java.util.Map;

/**
 * Author: Nurislom
 * <br/>
 * Date: 4/23/2023
 * <br/>
 * Time: 6:32 PM
 * <br/>
 * Package: org.khasanof.ratelimitingwithspring.core.common.update
 */
public interface DeleteOnRuntime {

    Map<String, Map<PTA, LocalRateLimiting>> delete(Map<String, Map<PTA, LocalRateLimiting>> map);

}
