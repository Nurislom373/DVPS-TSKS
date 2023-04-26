package org.khasanof.ratelimitingwithspring.core.common.runtime.update;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.khasanof.ratelimitingwithspring.core.common.search.classes.PTA;
import org.khasanof.ratelimitingwithspring.core.domain.*;
import org.khasanof.ratelimitingwithspring.core.domain.embeddable.LimitsEmbeddable;
import org.khasanof.ratelimitingwithspring.core.domain.enums.PricingType;
import org.khasanof.ratelimitingwithspring.core.exceptions.NotFoundException;
import org.khasanof.ratelimitingwithspring.core.limiting.LocalRateLimiting;
import org.khasanof.ratelimitingwithspring.core.RateLimiting;
import org.khasanof.ratelimitingwithspring.core.repository.PricingApiRepository;
import org.khasanof.ratelimitingwithspring.core.repository.PricingTariffRepository;
import org.khasanof.ratelimitingwithspring.core.utils.BaseUtils;
import org.khasanof.ratelimitingwithspring.core.utils.ConcurrentMapUtility;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Author: Nurislom
 * <br/>
 * Date: 4/20/2023
 * <br/>
 * Time: 1:36 PM
 * <br/>
 * Package: org.khasanof.ratelimitingwithspring.core.common.updateOnRuntime
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CommonUpdateOnRuntime implements UpdateOnRuntime {

    private final PricingTariffRepository pricingTariffRepository;
    private final ConcurrentMapUtility concurrentMapUtility;
    private final PricingApiRepository pricingApiRepository;

    @Override
    public void updateWithKey(String key) {
        updateWithKey(key, concurrentMapUtility.get(key)
                .orElseThrow(RuntimeException::new));
    }

    @Override
    public void updateWithKey(String key, Map<PTA, RateLimiting> limitingMap) {
        Map<PricingType, List<Map.Entry<PTA, RateLimiting>>> listMap = limitingMap.entrySet().stream()
                .collect(Collectors.groupingBy(e -> e.getKey().getPricingType()));

        log.info("key - {}, Pricings Size - {}", key, limitingMap.size());

        List<Map.Entry<PTA, RateLimiting>> entriesApi = listMap.get(PricingType.API);
        List<Map.Entry<PTA, RateLimiting>> entriesTariff = listMap.get(PricingType.TARIFF);

        if (Objects.nonNull(entriesApi)) {
            for (Map.Entry<PTA, RateLimiting> entry : entriesApi) {
                PricingApi pricingApiWithEntry = getPricingApiWithEntry(key, entry);
                if (updatePricing(entry.getValue(), pricingApiWithEntry)) {
                    log.info("Update Api - {}", pricingApiWithEntry);
                    pricingApiRepository.save(pricingApiWithEntry);
                }
            }
        }
        log.warn("entriesApi is null!");

        if (Objects.nonNull(entriesTariff)) {
            List<PricingTariff> tariffList = getPricingTariffWithEntry(key);
            for (Map.Entry<PTA, RateLimiting> entry : entriesTariff) {
                PricingTariff tariff = matchPricingReturn(entry, tariffList);
                if (updatePricing(entry.getValue(), tariff)) {
                    log.info("Update Tariff - {}", tariff);
                    pricingTariffRepository.save(tariff);
                }
            }
        }
        log.warn("entriesTariff is null!");
    }

    private PricingApi getPricingApiWithEntry(String key, Map.Entry<PTA, RateLimiting> entry) {
        return pricingApiRepository.findByQuery(key, entry.getKey().getApis().get(0))
                .orElseThrow(() -> new RuntimeException("PricingApi not found!"));
    }

    private List<PricingTariff> getPricingTariffWithEntry(String key) {
        return pricingTariffRepository.findAllByKey(key);
    }

    private PricingTariff matchPricingReturn(Map.Entry<PTA, RateLimiting> entry, List<PricingTariff> tariffs) {
        return tariffs.stream().filter(f -> BaseUtils.areEqual(
                        BaseUtils.returnIds(entry.getKey().getApis()), f.getApis()))
                .peek(p -> log.info("Show Filtering PricingTariff - {}", p))
                .findFirst().orElseThrow(() -> new RuntimeException("PricingTariff not found"));
    }

    private boolean updatePricing(RateLimiting rateLimiting, PricingTariff tariff) {
        return copyProperties(rateLimiting.getLocalRateLimiting(), tariff);
    }

    private boolean updatePricing(RateLimiting rateLimiting, PricingApi tariff) {
        return copyProperties(rateLimiting.getLocalRateLimiting(), tariff);
    }

    private boolean copyProperties(LocalRateLimiting localRateLimiting, PricingTariff tariff) {
        LimitsEmbeddable limitsEmbeddable = tariff.getLimitsEmbeddable();
        if (localRateLimiting.isNoLimit()) {
            if (!Objects.equals(localRateLimiting.getRefillCount(), tariff.getRefillCount())) {
                tariff.setRefillCount(localRateLimiting.getRefillCount());
                return true;
            }
            return false;
        } else {
            if (!Objects.equals(localRateLimiting.getRefillCount(), tariff.getRefillCount())) {
                tariff.setRefillCount(localRateLimiting.getRefillCount());
                return true;
            } else if (!Objects.equals(localRateLimiting.getToken(), limitsEmbeddable.getRequestCount())) {
                limitsEmbeddable.setRequestCount(localRateLimiting.getToken());
                tariff.setLimitsEmbeddable(limitsEmbeddable);
                return true;
            }
        }
        return false;
    }

    private boolean copyProperties(LocalRateLimiting localRateLimiting, PricingApi tariff) {
        LimitsEmbeddable limitsEmbeddable = tariff.getLimitsEmbeddable();
        if (localRateLimiting.isNoLimit()) {
            if (!Objects.equals(localRateLimiting.getRefillCount(), tariff.getRefillCount())) {
                tariff.setRefillCount(localRateLimiting.getRefillCount());
                return true;
            }
        } else {
            if (!Objects.equals(localRateLimiting.getRefillCount(), tariff.getRefillCount())) {
                tariff.setRefillCount(localRateLimiting.getRefillCount());
                return true;
            } else if (!Objects.equals(localRateLimiting.getToken(), limitsEmbeddable.getRequestCount())) {
                limitsEmbeddable.setRequestCount(localRateLimiting.getToken());
                tariff.setLimitsEmbeddable(limitsEmbeddable);
                return true;
            }
        }
        return false;
    }

}
