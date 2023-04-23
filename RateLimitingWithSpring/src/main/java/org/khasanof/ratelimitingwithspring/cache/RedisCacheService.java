package org.khasanof.ratelimitingwithspring.cache;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

/**
 * Author: Nurislom
 * <br/>
 * Date: 4/14/2023
 * <br/>
 * Time: 6:44 PM
 * <br/>
 * Package: org.khasanof.ratelimitingwithspring.core.cache
 */
@Service
public class RedisCacheService {

    private CacheManager cacheManager;
    private Cache cache;

}
