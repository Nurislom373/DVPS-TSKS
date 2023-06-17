package org.khasanof.ratelimitingwithspring.core.common.search.impls;

import lombok.extern.slf4j.Slf4j;
import org.khasanof.ratelimitingwithspring.cache.CacheOperations;
import org.khasanof.ratelimitingwithspring.cache.MergeValue;
import org.khasanof.ratelimitingwithspring.core.RateLimiting;
import org.khasanof.ratelimitingwithspring.core.common.search.RateLimitingSearch;
import org.khasanof.ratelimitingwithspring.core.common.search.RateLimitingSearchUtil;
import org.khasanof.ratelimitingwithspring.core.common.search.classes.PTA;
import org.khasanof.ratelimitingwithspring.core.common.search.classes.RLSearch;
import org.khasanof.ratelimitingwithspring.core.common.search.findLimit.FindLimitWithSearch;
import org.khasanof.ratelimitingwithspring.core.domain.enums.PricingType;
import org.khasanof.ratelimitingwithspring.core.exceptions.NotRegisteredException;
import org.khasanof.ratelimitingwithspring.core.limiting.LocalRateLimiting;
import org.khasanof.ratelimitingwithspring.core.limiting.SimpleRateLimiting;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * Author: Nurislom
 * <br/>
 * Date: 13.05.2023
 * <br/>
 * Time: 19:29
 * <br/>
 * Package: org.khasanof.ratelimitingwithspring.core.common.search.impls
 */
@Slf4j
@Service
public class RateLimitingSearchRedisCache extends RateLimitingSearchUtil implements RateLimitingSearch {

    private final CacheOperations cacheOperations;
    private final ApplicationContext applicationContext;
    private final FindLimitWithSearch findLimitWithSearch;

    public RateLimitingSearchRedisCache(@Qualifier("ehCacheService") CacheOperations cacheOperations,
                                        ApplicationContext applicationContext, FindLimitWithSearch findLimitWithSearch) {
        this.cacheOperations = cacheOperations;
        this.applicationContext = applicationContext;
        this.findLimitWithSearch = findLimitWithSearch;
    }

    @Override
    public RateLimiting searchKeys(RLSearch search) {
        Optional<MergeValue> optional = cacheOperations.getValues(search.getKey());
        SimpleRateLimiting simpleRateLimiting = applicationContext.getBean(SimpleRateLimiting.class);
        if (optional.isPresent()) {
            log.info("Request Build Thread Name " + Thread.currentThread().getName());
            System.out.println("simpleRateLimiting = " + simpleRateLimiting);
            LocalRateLimiting localRateLimiting = optional.get().getMap().entrySet().stream()
                    .filter(f -> checkAndReturnPTA(f.getKey(), search))
                    .map(m -> {
                        simpleRateLimiting.setPta(m.getKey());
                        simpleRateLimiting.setKey(search.getKey());
                        return m.getValue();
                    })
                    .findFirst().orElseThrow(() -> new RuntimeException("URI not found!"));
            simpleRateLimiting.replaceConfiguration(localRateLimiting);
            return simpleRateLimiting;
        } else {
            Map.Entry<PTA, LocalRateLimiting> limitingEntry = findLimitWithSearch.searchKeys(search);
            if (Objects.nonNull(limitingEntry)) {
                simpleRateLimiting.replaceConfiguration(limitingEntry.getValue());
                cacheOperations.addValues(search.getKey(), Map.ofEntries(limitingEntry));
                return simpleRateLimiting;
            } else {
                log.warn("this url {} isn't registered by {}", search.getUri(), search.getKey());
                throw new NotRegisteredException("this url " + search.getUri() + " isn't registered by " + search.getKey());
            }
        }
    }

    private boolean checkAndReturnPTA(PTA pta, RLSearch search) {
        if (pta.getPricingType().equals(PricingType.API)) {
            return areEqual(pta.getApis().get(0), search);
        } else {
            return pta.getApis().stream()
                    .anyMatch(any -> areEqual(any, search));
        }
    }

}
