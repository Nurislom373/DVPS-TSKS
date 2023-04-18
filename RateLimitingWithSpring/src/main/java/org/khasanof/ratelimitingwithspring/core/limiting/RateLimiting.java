package org.khasanof.ratelimitingwithspring.core.limiting;

/**
 * Author: Nurislom
 * <br/>
 * Date: 4/14/2023
 * <br/>
 * Time: 8:12 PM
 * <br/>
 * Package: org.khasanof.ratelimitingwithspring.core
 */
public interface RateLimiting {

    static RateLimitingBuilder builder() {
        return new RateLimitingBuilder();
    }

    boolean consumeRequest();

    long getNanosToWaitForRefill();

    boolean consumeRequest(int token);

    Long availableToken();

    Long availableRefill();

    void replaceConfiguration(LocalRateLimiting rateLimiting);

    void addRefill(Long refillCount);

    void addTokens(Long tokenCount);

    LocalRateLimiting getLocalRateLimiting();

}
