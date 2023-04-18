package org.khasanof.ratelimitingwithspring.core.utils;

import org.khasanof.ratelimitingwithspring.core.domain.PricingTariff;
import org.khasanof.ratelimitingwithspring.core.domain.enums.PricingType;
import org.khasanof.ratelimitingwithspring.core.limiting.PTA;
import org.khasanof.ratelimitingwithspring.core.limiting.RateLimiting;
import org.khasanof.ratelimitingwithspring.core.domain.PricingApi;
import org.khasanof.ratelimitingwithspring.core.domain.enums.RequestType;
import org.khasanof.ratelimitingwithspring.core.domain.enums.TimeType;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
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
@Component
public class RedisValueBuilder {

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
                .apis(entity.getApis())
                .pricingType(PricingType.TARIFF)
                .build(), rateLimitingBuild(entity));
    }

    private RateLimiting rateLimitingBuild(PricingApi entity) {
        if (entity.getLimited().getLimitsEmbeddable().getRequestType().equals(RequestType.NO_LIMIT)) {
            return RateLimiting.builder()
                    .noLimit(true)
                    .token(entity.getLimited().getLimitsEmbeddable().getRequestCount())
                    .duration(convertDuration(entity.getLimited().getLimitsEmbeddable().getTimeType(),
                            entity.getLimited().getLimitsEmbeddable().getTimeCount()))
                    .refillCount(entity.getRefillCount())
                    .build();
        } else {
            return RateLimiting.builder()
                    .noLimit(false)
                    .token(entity.getLimited().getLimitsEmbeddable().getRequestCount())
                    .duration(convertDuration(entity.getLimited().getLimitsEmbeddable().getTimeType(),
                            entity.getLimited().getLimitsEmbeddable().getTimeCount()))
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

    private Duration convertDuration(TimeType timeType, Long timeCount) {
        return switch (timeType) {
            case MINUTE -> Duration.ofMinutes(timeCount);
            case HOUR -> Duration.ofHours(timeCount);
            case DAY -> Duration.ofDays(timeCount);
            case WEEK -> Duration.ofDays((7 * timeCount));
            case MONTH -> Duration.ofDays((30 * timeCount));
            case YEAR -> Duration.ofDays((356 * timeCount));
        };
    }

}
