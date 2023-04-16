package org.khasanof.ratelimitingwithspring.core.utils;

import org.khasanof.ratelimitingwithspring.core.limiting.LocalRateLimiting;
import org.khasanof.ratelimitingwithspring.core.limiting.RateLimiting;
import org.khasanof.ratelimitingwithspring.domain.ApiEntity;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class BaseUtils {

    public Long limitMapToDays(Map<ApiEntity, RateLimiting> map) {
        return map.values().stream().map(this::getRateLimitingDaysWithRefillCount)
                .reduce(Long::max).orElseThrow(RuntimeException::new);
    }

    private Long getRateLimitingDaysWithRefillCount(RateLimiting rateLimiting) {
        LocalRateLimiting limiting = rateLimiting.getLocalRateLimiting();
        return limiting.getDuration().toDays() * limiting.getRefillCount();
    }

}
