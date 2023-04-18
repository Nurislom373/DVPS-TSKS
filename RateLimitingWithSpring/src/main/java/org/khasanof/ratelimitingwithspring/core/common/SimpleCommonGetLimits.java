package org.khasanof.ratelimitingwithspring.core.common;

import lombok.RequiredArgsConstructor;
import org.khasanof.ratelimitingwithspring.core.limiting.RateLimiting;
import org.khasanof.ratelimitingwithspring.core.repository.PricingApiEntityRepository;
import org.khasanof.ratelimitingwithspring.core.utils.RedisUtility;
import org.khasanof.ratelimitingwithspring.core.utils.RedisValueBuilder;
import org.khasanof.ratelimitingwithspring.core.domain.Api;
import org.khasanof.ratelimitingwithspring.core.domain.PricingApi;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;
import java.util.Optional;

/**
 * Author: Nurislom
 * <br/>
 * Date: 4/17/2023
 * <br/>
 * Time: 1:41 PM
 * <br/>
 * Package: org.khasanof.ratelimitingwithspring.core.common
 */
@Service
@RequiredArgsConstructor
public class SimpleCommonGetLimits implements CommonGetLimits {

    private final PricingApiEntityRepository entityRepository;
    private final RedisUtility redisUtility;
    private final RedisValueBuilder redisValueBuilder;

    @Override
    public RateLimiting getKeyAndUrl(String key, String url, String method) {
        Assert.notNull(key, "key param is null!");
        Assert.notNull(url, "url param is null!");
        Assert.notNull(method, "method param is null!");
        RequestMethod requestMethod = RequestMethod.resolve(method);

        Optional<Map<Api, RateLimiting>> optional = redisUtility.getValue(key);
        if (optional.isPresent()) {
            Map<Api, RateLimiting> limitingMap = optional.get();
            return limitingMap.entrySet().stream().filter(f -> f.getKey().getUrl().equals(url) &&
                    f.getKey().getMethod().equals(requestMethod)).findFirst()
                    .orElse(entryGet(key, url, requestMethod)).getValue();
        }
        return null;
    }

    private Map.Entry<Api, RateLimiting> entryGet(String key, String url, RequestMethod method) {
        PricingApi entity = entityRepository.findByKeyAndUrl(key, url, method)
                .orElseThrow(() -> new RuntimeException("PricingApiEntity not found"));
        return null;
    }
}
