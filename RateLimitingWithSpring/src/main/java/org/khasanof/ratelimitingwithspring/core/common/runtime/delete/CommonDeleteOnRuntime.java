package org.khasanof.ratelimitingwithspring.core.common.runtime.delete;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.khasanof.ratelimitingwithspring.cache.ehcache.EhCacheService;
import org.khasanof.ratelimitingwithspring.core.common.search.classes.PTA;
import org.khasanof.ratelimitingwithspring.core.domain.enums.PricingType;
import org.khasanof.ratelimitingwithspring.core.limiting.LocalRateLimiting;
import org.khasanof.ratelimitingwithspring.core.strategy.limit.delete.LimitDeleteStrategy;
import org.khasanof.ratelimitingwithspring.core.strategy.tariff.delete.TariffDeleteStrategy;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Author: Nurislom
 * <br/>
 * Date: 4/23/2023
 * <br/>
 * Time: 6:31 PM
 * <br/>
 * Package: org.khasanof.ratelimitingwithspring.core.common.update
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CommonDeleteOnRuntime implements DeleteOnRuntime {

    private final LimitDeleteStrategy limitDeleteStrategy;
    private final TariffDeleteStrategy tariffDeleteStrategy;
    // TODO rewrite with CacheUtility
    private final EhCacheService ehCacheService;

    @Override
    public Map<String, Map<PTA, LocalRateLimiting>> delete(Map<String, Map<PTA, LocalRateLimiting>> map) {
        return map.entrySet().stream().map(this::deleteWithKey)
                .filter(f -> !f.getKey().equals(State.NO_DATA)).map(Map.Entry::getValue)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private Map.Entry<State, Map.Entry<String, Map<PTA, LocalRateLimiting>>> deleteWithKey(Map.Entry<String, Map<PTA, LocalRateLimiting>> entry) {
        Map<PTA, LocalRateLimiting> deletedMap = entry.getValue().entrySet().stream()
                .filter(f -> isDelete(f.getValue()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        if (!deletedMap.isEmpty()) {
            entry.getValue().keySet()
                    .removeIf(deletedMap::containsKey);

            deletedMap.forEach((key, value) -> {
                if (key.getPricingType().equals(PricingType.API)) {
                    log.info("API Deleted Key - {} and PTA - {}", entry.getKey(), key);
                    limitDeleteStrategy.delete(new AbstractMap.SimpleEntry<>(entry.getKey(), key));
                } else {
                    log.info("TARIFF Deleted Key - {} and PTA - {}", entry.getKey(), key);
                    tariffDeleteStrategy.delete(new AbstractMap.SimpleEntry<>(entry.getKey(), key));
                }
            });

            if (!entry.getValue().isEmpty()) {
                return new AbstractMap.SimpleEntry<>(State.DELETED_DATA, entry);
            } else {
                if (ehCacheService.delete(entry.getKey())) {
                    return new AbstractMap.SimpleEntry<>(State.NO_DATA, entry);
                } else {
                    throw new RuntimeException("Object don't deleted!");
                }
            }
        } else {
            return new AbstractMap.SimpleEntry<>(State.NO_DELETED_DATA, entry);
        }
    }

    private boolean isDelete(LocalRateLimiting localRateLimiting) {
        if (localRateLimiting.isNoLimit()) {
            return localRateLimiting.getRefillCount() < 1;
        } else {
            if (localRateLimiting.getToken() >= 1) {
                return false;
            } else {
                return localRateLimiting.getRefillCount() < 1;
            }
        }
    }

    private enum State {
        NO_DELETED_DATA, DELETED_DATA, NO_DATA
    }


}
