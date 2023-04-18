package org.khasanof.ratelimitingwithspring.core.common;

import org.khasanof.ratelimitingwithspring.core.limiting.RateLimiting;

/**
 * Author: Nurislom
 * <br/>
 * Date: 4/17/2023
 * <br/>
 * Time: 1:40 PM
 * <br/>
 * Package: org.khasanof.ratelimitingwithspring.core.common
 */
public interface CommonGetLimits {

    RateLimiting getKeyAndUrl(String key, String url, String method);

}
