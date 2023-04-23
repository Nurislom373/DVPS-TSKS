package org.khasanof.ratelimitingwithspring.core.common.update;

import org.khasanof.ratelimitingwithspring.core.RateLimiting;
import org.khasanof.ratelimitingwithspring.core.common.search.classes.PTA;

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

    Map<PTA, RateLimiting> deleteWithKey(String key, Map<PTA, RateLimiting> limitingMap);

}
