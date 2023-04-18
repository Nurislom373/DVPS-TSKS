package org.khasanof.ratelimitingwithspring.core.common;

import lombok.RequiredArgsConstructor;
import org.khasanof.ratelimitingwithspring.core.common.save.REGSLimit;
import org.khasanof.ratelimitingwithspring.core.common.save.REGSTariff;
import org.khasanof.ratelimitingwithspring.core.domain.Limited;
import org.khasanof.ratelimitingwithspring.core.domain.PricingApi;
import org.khasanof.ratelimitingwithspring.core.domain.PricingTariff;
import org.khasanof.ratelimitingwithspring.core.domain.Tariff;
import org.khasanof.ratelimitingwithspring.core.repository.*;
import org.khasanof.ratelimitingwithspring.core.utils.ConcurrentMapUtility;
import org.khasanof.ratelimitingwithspring.core.utils.RedisValueBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Author: Nurislom
 * <br/>
 * Date: 4/17/2023
 * <br/>
 * Time: 11:35 AM
 * <br/>
 * Package: org.khasanof.ratelimitingwithspring.core
 */
@Service
@RequiredArgsConstructor
public class SimpleCommonRegisterLimits implements CommonRegisterLimits {

    private final PricingApiEntityRepository pricingApiEntityRepository;
    private final LimitedRepository limitedRepository;
    private final PricingTariffRepository pricingTariffRepository;
    private final TariffRepository tariffRepository;
    private final ApiRepository apiRepository;
    private final ConcurrentMapUtility mapUtility;
    private final RedisValueBuilder redisValueBuilder;

    @Override
    public void registrationOfLimits(String key, List<REGSLimit> limits) {
        Assert.notNull(key, "key param null!");
        Assert.notNull(limits, "limits param null!");

        List<PricingApi> apiEntityList = limits.stream()
                .map(limit -> buildPricingAPIEntity(key, limit))
                .peek(pricingApiEntityRepository::save)
                .toList();

        mapUtility.add(key, redisValueBuilder.convertApiListToMap(apiEntityList));
    }

    @Override
    public void registrationOfTariffs(String key, List<REGSTariff> tariffs) {
        Assert.notNull(key, "key param null!");
        Assert.notNull(tariffs, "tariffs param null!");

        List<PricingTariff> pricingTariffs = tariffs.stream()
                .map(tariff -> buildPricingTariffEntity(key, tariff))
                .peek(pricingTariffRepository::saveAndFlush)
                .toList();

        mapUtility.add(key, redisValueBuilder.convertTariffListToMap(pricingTariffs));
    }

    private PricingTariff buildPricingTariffEntity(String key, REGSTariff tariff) {
        Optional<Tariff> optional = tariffRepository.findByName(tariff.getName());
        if (optional.isPresent()) {
            Tariff tariffEntity = optional.get();
            return PricingTariff.builder()
                    .key(key)
                    .tariff(tariffEntity)
                    .apis(tariff.getApi().stream().map(e ->
                            apiRepository.findByUrlAndMethodAndVariables(e.getUrl(), e.getMethod(), e.getAttributes()))
                            .map(Optional::orElseThrow).toList())
                    .limitsEmbeddable(tariffEntity.getLimitsEmbeddable())
                    .refillCount(tariff.getRefillCount())
                    .build();
        }
        throw new RuntimeException("Tariff not found");
    }

    private PricingApi buildPricingAPIEntity(String key, REGSLimit limit) {
        return PricingApi.builder()
                .key(key)
                .limited(attributesEqualsAndGet(limitedRepository.findByREGSLimit(limit.getPlan(),
                        limit.getUrl(), limit.getMethod()), limit.getAttributes()))
                .refillCount(limit.getRefillCount())
                .build();
    }

    private Limited attributesEqualsAndGet(List<Limited> list, Map<String, String> attributes) {
        if (list != null && !list.isEmpty()) {
            if (attributes == null || attributes.isEmpty()) {
                if (list.size() == 1) {
                    return list.get(0);
                } else {
                    throw new RuntimeException("Duplicate API found!");
                }
            } else {
                return list.stream()
                        .filter(f -> areEqual(f.getApi().getVariables(), attributes))
                        .findFirst().orElseThrow(() -> new RuntimeException("Limited match not found!"));
            }
        }
        throw new RuntimeException("Limit not found!");
    }

    private boolean areEqual(Map<String, String> first, Map<String, String> second) {
        if (first.size() != second.size()) {
            return false;
        }
        return first.entrySet().stream()
                .allMatch(e -> e.getValue().equals(second.get(e.getKey())));
    }
}
