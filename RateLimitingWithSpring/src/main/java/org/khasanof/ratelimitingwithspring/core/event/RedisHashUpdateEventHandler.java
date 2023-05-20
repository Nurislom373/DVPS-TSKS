package org.khasanof.ratelimitingwithspring.core.event;

import lombok.RequiredArgsConstructor;
import org.khasanof.ratelimitingwithspring.cache.redis.RedisUtility;
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
@RequiredArgsConstructor
public class RedisHashUpdateEventHandler {

    private final RedisUtility redisUtility;

    @EventListener
    public void handleEvent(LimitUpdateEvent event) {
        redisUtility.updateValues(event.getKey(), event.getPta(), event.getLocalRateLimiting());
    }

}
