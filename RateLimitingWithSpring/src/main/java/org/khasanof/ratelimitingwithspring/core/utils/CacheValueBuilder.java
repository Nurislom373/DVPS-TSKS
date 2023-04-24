package org.khasanof.ratelimitingwithspring.core.utils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.khasanof.ratelimitingwithspring.core.RateLimiting;
import org.khasanof.ratelimitingwithspring.core.common.search.classes.PTA;
import org.khasanof.ratelimitingwithspring.core.domain.PricingApi;
import org.khasanof.ratelimitingwithspring.core.domain.PricingTariff;
import org.khasanof.ratelimitingwithspring.core.domain.enums.PricingType;
import org.khasanof.ratelimitingwithspring.core.domain.enums.RequestType;
import org.khasanof.ratelimitingwithspring.core.domain.enums.TimeType;
import org.khasanof.ratelimitingwithspring.core.repository.ApiRepository;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Period;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Author: Nurislom
 * <br/>
 * Date: 4/17/2023
 * <br/>
 * Time: 12:40 PM
 * <br/>
 * Package: org.khasanof.ratelimitingwithspring.core.utils
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class CacheValueBuilder {

    private final ApiRepository apiRepository;

    public Map<String, Map<PTA, RateLimiting>> convertApiListToInnerMap(List<PricingApi> list) {
        Map<String, Map<PTA, RateLimiting>> map = new ConcurrentHashMap<>();

        for (PricingApi p : list) {
            Map.Entry<PTA, RateLimiting> entity = convertEntityToEntry(p);

            Map<PTA, RateLimiting> rateLimitingMap = map.get(p.getKey());

            if (rateLimitingMap != null) {
                rateLimitingMap.putIfAbsent(entity.getKey(), entity.getValue());
                map.put(p.getKey(), rateLimitingMap);
            } else {
                map.put(p.getKey(), new HashMap<>() {{
                    put(entity.getKey(), entity.getValue());
                }});
            }
        }

        log.info("PricingAPI LoadSize = {}", map.size());

        return map;
    }

    public Map<String, Map<PTA, RateLimiting>> convertTariffListToInnerMap(List<PricingTariff> list) {
        Map<String, Map<PTA, RateLimiting>> map = new ConcurrentHashMap<>();

        for (PricingTariff p : list) {
            Map.Entry<PTA, RateLimiting> entity = convertEntityToEntry(p);

            Map<PTA, RateLimiting> rateLimitingMap = map.get(p.getKey());

            if (rateLimitingMap != null) {
                rateLimitingMap.putIfAbsent(entity.getKey(), entity.getValue());
                map.put(p.getKey(), rateLimitingMap);
            } else {
                map.put(p.getKey(), new HashMap<>() {{
                    put(entity.getKey(), entity.getValue());
                }});
            }
        }

        log.info("PricingTariff LoadSize = {}", map.size());

        return map;
    }

    public Map<PTA, RateLimiting> convertApiListToMap(List<PricingApi> list) {
        return list.stream().map(this::convertEntityToEntry)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public Map<PTA, RateLimiting> convertTariffListToMap(List<PricingTariff> list) {
        return list.stream().map(this::convertEntityToEntry)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private Map.Entry<PTA, RateLimiting> convertEntityToEntry(PricingApi entity) {
        return new AbstractMap.SimpleEntry<>(PTA.builder()
                .apis(List.of(entity.getLimited().getApi()))
                .pricingType(PricingType.API)
                .build(), rateLimitingBuild(entity));
    }

    private Map.Entry<PTA, RateLimiting> convertEntityToEntry(PricingTariff entity) {
        return new AbstractMap.SimpleEntry<>(PTA.builder()
                .apis(apiRepository.findAllByIdIsIn(entity.getApis()))
                .pricingType(PricingType.TARIFF)
                .build(), rateLimitingBuild(entity));
    }

    private RateLimiting rateLimitingBuild(PricingApi entity) {
        if (entity.getLimitsEmbeddable().getRequestType().equals(RequestType.NO_LIMIT)) {
            return RateLimiting.builder()
                    .noLimit(true)
                    .token(entity.getLimitsEmbeddable().getRequestCount())
                    .duration(convertDuration(entity.getLimitsEmbeddable().getTimeType(),
                            entity.getLimitsEmbeddable().getTimeCount()))
                    .refillCount(entity.getRefillCount())
                    .build();
        } else {
            return RateLimiting.builder()
                    .noLimit(false)
                    .token(entity.getLimitsEmbeddable().getRequestCount())
                    .duration(convertDuration(entity.getLimitsEmbeddable().getTimeType(),
                            entity.getLimitsEmbeddable().getTimeCount()))
                    .refillCount(entity.getRefillCount())
                    .build();
        }
    }

    private RateLimiting rateLimitingBuild(PricingTariff entity) {
        if (entity.getLimitsEmbeddable().getRequestType().equals(RequestType.NO_LIMIT)) {
            return RateLimiting.builder()
                    .noLimit(true)
                    .token(entity.getLimitsEmbeddable().getRequestCount())
                    .duration(convertDuration(entity.getLimitsEmbeddable().getTimeType(),
                            entity.getLimitsEmbeddable().getTimeCount()))
                    .refillCount(entity.getRefillCount())
                    .build();
        } else {
            return RateLimiting.builder()
                    .noLimit(false)
                    .token(entity.getLimitsEmbeddable().getRequestCount())
                    .duration(convertDuration(entity.getLimitsEmbeddable().getTimeType(),
                            entity.getLimitsEmbeddable().getTimeCount()))
                    .refillCount(entity.getRefillCount())
                    .build();
        }
    }

    // rewrite
    private Duration convertDuration(TimeType timeType, Long timeCount) {
        return switch (timeType) {
            case MINUTE -> Duration.ofMinutes(timeCount);
            case HOUR -> Duration.ofHours(timeCount);
            case DAY -> Duration.ofDays(timeCount);
            case WEEK -> Duration.ofDays((7 * timeCount));
            case MONTH -> Duration.ofDays((30 * timeCount));
            case YEAR -> Duration.ofDays(Period.ofYears(timeCount.intValue()).getDays());
        };
    }

}
