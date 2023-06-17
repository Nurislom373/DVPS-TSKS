package org.khasanof.ratelimitingwithspring.cache;

import lombok.*;
import org.khasanof.ratelimitingwithspring.core.RateLimiting;
import org.khasanof.ratelimitingwithspring.core.common.search.classes.PTA;
import org.khasanof.ratelimitingwithspring.core.limiting.LocalRateLimiting;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;
import java.util.Map;

/**
 * Author: Nurislom
 * <br/>
 * Date: 4/24/2023
 * <br/>
 * Time: 10:23 AM
 * <br/>
 * Package: org.khasanof.ratelimitingwithspring.cache
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class MergeValue implements Serializable {
    private Map<PTA, LocalRateLimiting> map;
}
