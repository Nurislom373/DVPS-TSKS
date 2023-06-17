package org.khasanof.ratelimitingwithspring.core.common.search;

import org.khasanof.ratelimitingwithspring.core.RateLimiting;
import org.khasanof.ratelimitingwithspring.core.common.search.classes.RLSearch;

/**
 * Author: Nurislom
 * <br/>
 * Date: 13.05.2023
 * <br/>
 * Time: 19:25
 * <br/>
 * Package: org.khasanof.ratelimitingwithspring.core.common.search
 */
public interface RateLimitingSearch {

    RateLimiting searchKeys(RLSearch search);

}
