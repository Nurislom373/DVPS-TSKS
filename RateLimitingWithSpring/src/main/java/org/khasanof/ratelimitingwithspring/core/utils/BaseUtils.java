package org.khasanof.ratelimitingwithspring.core.utils;

import org.khasanof.ratelimitingwithspring.core.domain.Api;
import org.khasanof.ratelimitingwithspring.core.limiting.LocalRateLimiting;
import org.khasanof.ratelimitingwithspring.core.RateLimiting;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

public class BaseUtils {

    public Long limitMapToSeconds(Map<Api, RateLimiting> map) {
        return map.values().stream().map(this::getRateLimitingDaysWithRefillCount)
                .reduce(Long::max).orElseThrow(RuntimeException::new);
    }

    private Long getRateLimitingDaysWithRefillCount(RateLimiting rateLimiting) {
        LocalRateLimiting limiting = rateLimiting.getLocalRateLimiting();
        return limiting.getDuration().toSeconds() * limiting.getRefillCount();
    }

    public static boolean areEqual(Map<String, String> first, Map<String, String> second) {
        if (Objects.nonNull(first) && Objects.nonNull(second)) {
            if (first.size() != second.size()) {
                return false;
            }
            return first.entrySet().stream()
                    .allMatch(e -> e.getValue().equals(second.get(e.getKey())));
        }
        return false;
    }

    public static boolean areEqual(Collection<Long> collection1, Collection<Long> collection2) {
        if (collection1.size() != collection2.size()) {
            return false;
        }
        return collection1.containsAll(collection2) && collection2.containsAll(collection1);
    }

    public static List<Long> returnIds(List<Api> apis) {
        return apis.stream().map(Api::getId).toList();
    }

    public static boolean getOrDefault(Supplier<Boolean> supplier) {
        return supplier.get();
    }

}
