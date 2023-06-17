package org.khasanof.ratelimitingwithspring.core.event;

import org.khasanof.ratelimitingwithspring.cache.CacheOperations;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * Author: Nurislom
 * <br/>
 * Date: 13.05.2023
 * <br/>
 * Time: 21:23
 * <br/>
 * Package: org.khasanof.ratelimitingwithspring.core.event
 */
@Component
public class RedisHashUpdateEventHandler {

    private final CacheOperations redisUtility;

    public RedisHashUpdateEventHandler(@Qualifier("ehCacheService") CacheOperations redisUtility) {
        this.redisUtility = redisUtility;
    }

    @EventListener
    public void handleEvent(LimitUpdateEvent event) {
        redisUtility.updateValues(event.getKey(), event.getPta(), event.getLocalRateLimiting());
    }

}
