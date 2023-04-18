package org.khasanof.ratelimitingwithspring.core.utils;

import org.khasanof.ratelimitingwithspring.core.limiting.LocalRateLimiting;
import org.khasanof.ratelimitingwithspring.core.limiting.RateLimiting;
import org.khasanof.ratelimitingwithspring.core.domain.Api;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class BaseUtils {

    public Long limitMapToSeconds(Map<Api, RateLimiting> map) {
        return map.values().stream().map(this::getRateLimitingDaysWithRefillCount)
                .reduce(Long::max).orElseThrow(RuntimeException::new);
    }

    private Long getRateLimitingDaysWithRefillCount(RateLimiting rateLimiting) {
        LocalRateLimiting limiting = rateLimiting.getLocalRateLimiting();
        return limiting.getDuration().toSeconds() * limiting.getRefillCount();
    }

}
