package org.khasanof.ratelimitingwithspring.core.common.register;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.khasanof.ratelimitingwithspring.core.common.register.classes.REGSLimit;
import org.khasanof.ratelimitingwithspring.core.common.register.classes.REGSTariff;
import org.khasanof.ratelimitingwithspring.core.common.register.classes.REGSTariffApi;
import org.khasanof.ratelimitingwithspring.core.domain.*;
import org.khasanof.ratelimitingwithspring.core.repository.*;
import org.khasanof.ratelimitingwithspring.core.utils.BaseUtils;
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
@Slf4j
@Service
@RequiredArgsConstructor
public class SimpleCommonRegisterLimits implements CommonRegisterLimits {

    private final PricingApiRepository pricingApiRepository;
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
                .peek((p) -> log.info("Build PricingApi - {}", p))
                .peek(pricingApiRepository::saveAndFlush)
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
                    .apis(buildPricingTariffEntity(tariff.getApi()))
                    .limitsEmbeddable(tariffEntity.getLimitsEmbeddable())
                    .refillCount(tariff.getRefillCount())
                    .build();
        }
        throw new RuntimeException("Tariff not found");
    }

    private List<Long> buildPricingTariffEntity(List<REGSTariffApi> apis) {
        return apis.stream()
                .map(this::isPresentReturnIdOrNull)
                .peek(p -> {
                    if (p == null)
                        throw new RuntimeException("API not found!");
                }).toList();
    }

    private Long isPresentReturnIdOrNull(REGSTariffApi api) {
        log.info("isPresentReturnIdOrNull param api - {}", api);
        if (api.getAttributes() == null || api.getAttributes().isEmpty()) {
            return apiRepository.findByUrlAndMethodAndAttributes(api.getUrl(), api.getMethod(),
                    api.getAttributes()).map(Api::getId)
                    .orElse(null);
        } else {
            List<Api> list = apiRepository.findByUrlAndMethod(api.getUrl(), api.getMethod());
            return list.stream().filter(f -> BaseUtils.areEqual(f.getAttributes(), api.getAttributes()))
                    .findFirst().orElseThrow(() -> new RuntimeException("Match API not found!"))
                    .getId();
        }
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
                        .filter(f -> BaseUtils.areEqual(f.getApi().getAttributes(), attributes))
                        .findFirst().orElseThrow(() -> new RuntimeException("Limited match not found!"));
            }
        }
        throw new RuntimeException("Limit not found!");
    }
}
