package org.khasanof.ratelimitingwithspring.cache;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.khasanof.ratelimitingwithspring.cache.redis.RedisUtility;
import org.khasanof.ratelimitingwithspring.core.common.search.classes.PTA;
import org.khasanof.ratelimitingwithspring.core.domain.Api;
import org.khasanof.ratelimitingwithspring.core.domain.enums.PricingType;
import org.khasanof.ratelimitingwithspring.core.limiting.LocalRateLimiting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMethod;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Author: Nurislom
 * <br/>
 * Date: 27.05.2023
 * <br/>
 * Time: 15:51
 * <br/>
 * Package: org.khasanof.ratelimitingwithspring.cache
 */
@SpringBootTest
public class RedisUtilityTest {

    @Autowired
    private CacheManager cacheManager;

    @Autowired
    private RedisTemplate redisTemplate;

    private RedisUtility redisUtility;

    @BeforeEach
    void setUp() {
        redisUtility = new RedisUtility(cacheManager, redisTemplate);
        mockMap().forEach(redisUtility::addValues);
    }

    @Test
    void getAllMethodTest() {
    }

    Map<String, Map<PTA, LocalRateLimiting>> mockMap() {
        return new HashMap<>() {{
            put("4332", new HashMap<>() {{
                put(new PTA(List.of(new Api("/api/v1/users", RequestMethod.POST, null)), PricingType.API),
                        LocalRateLimiting.builder()
                                .undiminishedCount(30L)
                                .token(30L)
                                .noLimit(false)
                                .refillCount(2L)
                                .createdAt(Instant.now())
                        .build());
            }});
            put("43782", new HashMap<>() {{
                put(new PTA(List.of(new Api("/api/v1/users", RequestMethod.POST, null)), PricingType.API),
                        LocalRateLimiting.builder()
                                .undiminishedCount(30L)
                                .token(30L)
                                .noLimit(false)
                                .refillCount(1L)
                                .createdAt(Instant.now())
                                .build());
            }});
        }};
    }

}
