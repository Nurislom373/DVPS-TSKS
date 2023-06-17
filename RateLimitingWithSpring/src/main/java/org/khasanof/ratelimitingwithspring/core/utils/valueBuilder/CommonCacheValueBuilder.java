package org.khasanof.ratelimitingwithspring.core.utils.valueBuilder;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.khasanof.ratelimitingwithspring.core.common.search.classes.PTA;
import org.khasanof.ratelimitingwithspring.core.domain.PricingApi;
import org.khasanof.ratelimitingwithspring.core.domain.PricingTariff;
import org.khasanof.ratelimitingwithspring.core.domain.enums.PricingType;
import org.khasanof.ratelimitingwithspring.core.domain.enums.RequestType;
import org.khasanof.ratelimitingwithspring.core.domain.enums.TimeType;
import org.khasanof.ratelimitingwithspring.core.limiting.LocalRateLimiting;
import org.khasanof.ratelimitingwithspring.core.repository.ApiRepository;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Period;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * Author: Nurislom
 * <br/>
 * Date: 20.05.2023
 * <br/>
 * Time: 16:17
 * <br/>
 * Package: org.khasanof.ratelimitingwithspring.core.utils.valueBuilder
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class CommonCacheValueBuilder extends CacheValueBuilder {

    private final ApiRepository apiRepository;

    @Override
    public Map<String, Map<PTA, LocalRateLimiting>> pricingAPIListToInnerMap(List<PricingApi> list) {
        Map<String, Map<PTA, LocalRateLimiting>> map = new ConcurrentHashMap<>();
        list.forEach(o -> entityPushMap(convertEntityToEntry(o), map, o.getKey()));
        log.info("PricingAPI LoadSize = {}", map.size());
        return map;
    }

    @Override
    public Map<String, Map<PTA, LocalRateLimiting>> pricingTariffListToInnerMap(List<PricingTariff> list) {
        Map<String, Map<PTA, LocalRateLimiting>> map = new ConcurrentHashMap<>();
        list.forEach(o -> entityPushMap(convertEntityToEntry(o), map, o.getKey()));
        log.info("PricingTariff LoadSize = {}", map.size());
        return map;
    }

    @Override
    public Map<PTA, LocalRateLimiting> pricingAPIListToMap(List<PricingApi> list) {
        return list.stream().map(this::convertEntityToEntry)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    @Override
    public Map<PTA, LocalRateLimiting> pricingTariffListToMap(List<PricingTariff> list) {
        return list.stream().map(this::convertEntityToEntry)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private void entityPushMap(Map.Entry<PTA, LocalRateLimiting> entity, Map<String, Map<PTA, LocalRateLimiting>> map, String p1) {
        Map<PTA, LocalRateLimiting> rateLimitingMap = map.get(p1);
        if (rateLimitingMap != null) {
            rateLimitingMap.putIfAbsent(entity.getKey(), entity.getValue());
            map.put(p1, rateLimitingMap);
        } else {
            map.put(p1, new HashMap<>() {{
                put(entity.getKey(), entity.getValue());
            }});
        }
    }

    @Override
    public Map.Entry<PTA, LocalRateLimiting> convertEntityToEntry(PricingApi entity) {
        return new AbstractMap.SimpleEntry<>(PTA.builder()
                .apis(List.of(entity.getLimited().getApi()))
                .pricingType(PricingType.API)
                .build(), localRateLimitingBuild(entity, () -> entity.getLimitsEmbeddable()
                .getRequestType().equals(RequestType.NO_LIMIT)));
    }

    @Override
    public Map.Entry<PTA, LocalRateLimiting> convertEntityToEntry(PricingTariff entity) {
        return new AbstractMap.SimpleEntry<>(PTA.builder()
                .apis(apiRepository.findAllByIdIsIn(entity.getApis()))
                .pricingType(PricingType.TARIFF)
                .build(), localRateLimitingBuild(entity, () -> entity.getLimitsEmbeddable()
                .getRequestType().equals(RequestType.NO_LIMIT)));
    }

    private LocalRateLimiting localRateLimitingBuild(PricingTariff entity, Supplier<Boolean> supplier) {
        return LocalRateLimiting.builder()
                .noLimit(supplier.get())
                .createdAt(entity.getCreatedAt())
                .undiminishedCount(entity.getLimitsEmbeddable().getUndiminishedCount())
                .token(entity.getLimitsEmbeddable().getRequestCount())
                .duration(convertDuration(entity.getLimitsEmbeddable().getTimeType(),
                        entity.getLimitsEmbeddable().getTimeCount()))
                .refillCount(entity.getRefillCount())
                .build();
    }

    private LocalRateLimiting localRateLimitingBuild(PricingApi entity, Supplier<Boolean> supplier) {
        return LocalRateLimiting.builder()
                .noLimit(supplier.get())
                .createdAt(entity.getCreatedAt())
                .undiminishedCount(entity.getLimitsEmbeddable().getUndiminishedCount())
                .token(entity.getLimitsEmbeddable().getRequestCount())
                .duration(convertDuration(entity.getLimitsEmbeddable().getTimeType(),
                        entity.getLimitsEmbeddable().getTimeCount()))
                .refillCount(entity.getRefillCount())
                .build();
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
