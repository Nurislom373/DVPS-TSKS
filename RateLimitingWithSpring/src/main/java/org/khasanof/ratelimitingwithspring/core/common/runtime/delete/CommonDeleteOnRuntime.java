package org.khasanof.ratelimitingwithspring.core.common.runtime.delete;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.khasanof.ratelimitingwithspring.core.RateLimiting;
import org.khasanof.ratelimitingwithspring.core.common.search.classes.PTA;
import org.khasanof.ratelimitingwithspring.core.limiting.LocalRateLimiting;
import org.khasanof.ratelimitingwithspring.core.strategy.limit.delete.LimitDeleteStrategy;
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

    // TODO ... write logic

    private final LimitDeleteStrategy limitDeleteStrategy;

    @Override
    public Map<String, Map<PTA, RateLimiting>> delete(Map<String, Map<PTA, RateLimiting>> map) {
        return map.entrySet().stream().map(this::deleteWithKey)
                .filter(f -> !f.getKey().equals(State.NO_DATA)).map(Map.Entry::getValue)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private Map.Entry<State, Map.Entry<String, Map<PTA, RateLimiting>>> deleteWithKey(Map.Entry<String, Map<PTA, RateLimiting>> entry) {
        Map<PTA, RateLimiting> deletedMap = entry.getValue().entrySet().stream()
                .filter(f -> isDelete(f.getValue()))
                .collect(Collectors.toMap(Map.Entry::getKey,
                        Map.Entry::getValue));

        if (!deletedMap.isEmpty()) {
            entry.getValue().keySet()
                    .removeIf(deletedMap::containsKey);
            limitDeleteStrategy.delete(new AbstractMap.SimpleEntry<>(entry.getKey(), deletedMap));

            if (!entry.getValue().isEmpty()) {
                return new AbstractMap.SimpleEntry<>(State.DELETED_DATA, entry);
            } else {
                return new AbstractMap.SimpleEntry<>(State.NO_DATA, entry);
            }

        } else {
            return new AbstractMap.SimpleEntry<>(State.NO_DELETED_DATA, entry);
        }
    }

    private boolean isDelete(RateLimiting rateLimiting) {
        LocalRateLimiting localRateLimiting = rateLimiting.getLocalRateLimiting();
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
