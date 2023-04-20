package org.khasanof.ratelimitingwithspring.core.common.load;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.khasanof.ratelimitingwithspring.core.common.search.classes.PTA;
import org.khasanof.ratelimitingwithspring.core.domain.PricingApi;
import org.khasanof.ratelimitingwithspring.core.domain.PricingTariff;
import org.khasanof.ratelimitingwithspring.core.limiting.RateLimiting;
import org.khasanof.ratelimitingwithspring.core.repository.PricingApiRepository;
import org.khasanof.ratelimitingwithspring.core.repository.PricingTariffRepository;
import org.khasanof.ratelimitingwithspring.core.utils.ConcurrentMapUtility;
import org.khasanof.ratelimitingwithspring.core.utils.RedisValueBuilder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

/**
 * Author: Nurislom
 * <br/>
 * Date: 4/20/2023
 * <br/>
 * Time: 1:05 AM
 * <br/>
 * Package: org.khasanof.ratelimitingwithspring.core.common.loadOnRun
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CommonLoadPTAStartUp implements LoadingPTAStartUp {

    private final PricingApiRepository pricingApiRepository;
    private final PricingTariffRepository pricingTariffRepository;
    private final RedisValueBuilder redisValueBuilder;
    private final ConcurrentMapUtility mapUtility;

    @Override
    public void loadStartUp() {
        long count = Stream.of(loadPricingApi(), loadPricingTariff())
                .flatMap(m -> m.entrySet().stream()
                        .peek(e -> mapUtility.add(e.getKey(), e.getValue()))
                ).count();
        log.info("All Saved Count : {}", count);
    }

    private Map<String, Map<PTA, RateLimiting>> loadPricingApi() {
        long count = pricingApiRepository.count();
        if (count >= 1) {
            log.info("PricingApi Found : {}", count);
            List<PricingApi> list = pricingApiRepository.findAll();
            return redisValueBuilder.convertApiListToInnerMap(list);
        }
        log.warn("PricingApi Not Found!");
        return new HashMap<>();
    }

    private Map<String, Map<PTA, RateLimiting>> loadPricingTariff() {
        long count = pricingTariffRepository.count();
        if (count >= 1) {
            log.info("PricingTariff Found : {}", count);
            List<PricingTariff> list = pricingTariffRepository.findAll();
            return redisValueBuilder.convertTariffListToInnerMap(list);
        }
        log.warn("PricingTariff Not Found!");
        return new HashMap<>();
    }
}
