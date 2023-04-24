package org.khasanof.ratelimitingwithspring.core.common.update;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.khasanof.ratelimitingwithspring.core.RateLimiting;
import org.khasanof.ratelimitingwithspring.core.common.search.classes.PTA;
import org.khasanof.ratelimitingwithspring.core.limiting.LocalRateLimiting;
import org.springframework.stereotype.Service;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
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

    @Override
    public Map<String, Map<PTA, RateLimiting>> deleteWithKey(String key, Map<PTA, RateLimiting> limitingMap) {
        HashMap<String, Map.Entry<PTA, RateLimiting>> deletedHashMap = limitingMap.entrySet().stream()
                .filter(f -> isDelete(f.getValue())).map(m -> new AbstractMap.SimpleEntry<>(key, m))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (prev, next) -> next, HashMap::new));


        return new HashMap<>() {{
            put(key, limitingMap.entrySet().stream()
                    .filter(f -> !isDelete(f.getValue()))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                            (prev, next) -> next, HashMap::new)));
        }};
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


}
