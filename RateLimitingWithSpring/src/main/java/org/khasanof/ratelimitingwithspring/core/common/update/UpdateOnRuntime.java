package org.khasanof.ratelimitingwithspring.core.common.update;

import org.khasanof.ratelimitingwithspring.core.common.search.classes.PTA;
import org.khasanof.ratelimitingwithspring.core.limiting.RateLimiting;

import java.util.Map;

/**
 * Author: Nurislom
 * <br/>
 * Date: 4/20/2023
 * <br/>
 * Time: 3:41 PM
 * <br/>
 * Package: org.khasanof.ratelimitingwithspring.core.common.updateOnRuntime
 */
public interface UpdateOnRuntime {

    void updateWithKey(String key);

    void updateWithKey(String key, Map<PTA, RateLimiting> limitingMap);

}
