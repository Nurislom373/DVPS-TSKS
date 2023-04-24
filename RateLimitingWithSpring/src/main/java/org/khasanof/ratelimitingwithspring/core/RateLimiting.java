package org.khasanof.ratelimitingwithspring.core;

import org.khasanof.ratelimitingwithspring.core.limiting.LocalRateLimiting;
import org.khasanof.ratelimitingwithspring.core.limiting.RateLimitingBuilder;
import org.khasanof.ratelimitingwithspring.core.limiting.SimpleRateLimiting;

import java.io.Serializable;

/**
 * Author: Nurislom
 * <br/>
 * Date: 4/14/2023
 * <br/>
 * Time: 8:12 PM
 * <br/>
 * Package: org.khasanof.ratelimitingwithspring.core
 */
public interface RateLimiting extends Serializable {

    static RateLimitingBuilder builder() {
        return new RateLimitingBuilder();
    }

    SimpleRateLimiting.Result consumeRequest();

    SimpleRateLimiting.Result consumeRequest(int token);

    long getNanosToWaitForRefill();

    Long availableToken();

    Long availableRefill();

    void replaceConfiguration(LocalRateLimiting rateLimiting);

    void addRefill(Long refillCount);

    void addTokens(Long tokenCount);

    LocalRateLimiting getLocalRateLimiting();

}
