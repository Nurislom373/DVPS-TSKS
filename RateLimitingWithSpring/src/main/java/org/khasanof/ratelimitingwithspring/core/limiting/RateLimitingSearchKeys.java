package org.khasanof.ratelimitingwithspring.core.limiting;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.khasanof.ratelimitingwithspring.core.utils.ConcurrentMapUtility;
import org.springframework.stereotype.Service;

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
        return null;
    }

}
