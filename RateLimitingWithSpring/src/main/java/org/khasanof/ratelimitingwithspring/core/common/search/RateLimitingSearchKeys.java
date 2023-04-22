package org.khasanof.ratelimitingwithspring.core.common.search;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.khasanof.ratelimitingwithspring.core.common.search.classes.PTA;
import org.khasanof.ratelimitingwithspring.core.common.search.classes.RLSearch;
import org.khasanof.ratelimitingwithspring.core.domain.Api;
import org.khasanof.ratelimitingwithspring.core.domain.enums.PricingType;
import org.khasanof.ratelimitingwithspring.core.RateLimiting;
import org.khasanof.ratelimitingwithspring.core.utils.BaseUtils;
import org.khasanof.ratelimitingwithspring.core.utils.ConcurrentMapUtility;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;

/**
 * Author: Nurislom
 * <br/>
 * Date: 4/19/2023
 * <br/>
 * Time: 12:06 AM
 * <br/>
 * Package: org.khasanof.ratelimitingwithspring.core.limiting
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RateLimitingSearchKeys {

    private final ConcurrentMapUtility mapUtility;

    public RateLimiting searchKeys(RLSearch search) {
        Assert.notNull(search, "search param is null!");

        Map<PTA, RateLimiting> ptaRateLimitingMap = mapUtility.get(search.getKey());
        return ptaRateLimitingMap.entrySet().stream()
                .filter(f -> checkAndReturnPTA(f.getKey(), search))
                .map(Map.Entry::getValue)
                .findFirst().orElseThrow(() -> new RuntimeException("URI not found!"));
    }

    private boolean checkAndReturnPTA(PTA pta, RLSearch search) {
        if (pta.getPricingType().equals(PricingType.API)) {
            return areEqual(pta.getApis().get(0), search);
        } else {
            return pta.getApis().stream()
                    .anyMatch(any -> areEqual(any, search));
        }
    }

    private boolean areEqual(Api api, RLSearch search) {
        System.out.println(search.getUri());
        System.out.println("api = " + api);

        if (search.getAttributes() != null && !search.getAttributes().isEmpty()) {
            if (twoURI(api.getUrl(), search.getUri())) {
                System.out.println("Enter with attributes");
                if (api.getMethod().equals(RequestMethod.resolve(search.getMethod()))) {
                    return BaseUtils.areEqual(api.getAttributes(), search.getAttributes());
                }
            }
        } else {
            System.out.println("Without Attributes");
            if (api.getUrl().equals(search.getUri())) {
                return api.getMethod().equals(RequestMethod.resolve(search.getMethod()));
            }
        }
        return false;
    }

    private boolean twoURI(String limitURI, String requestURI) {
        return requestURI.startsWith(limitURI.replace("*", ""));
    }
}
