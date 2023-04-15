package org.khasanof.ratelimitingwithspring.core.limiting;

import io.github.bucket4j.Bucket;
import org.springframework.stereotype.Service;

import java.time.Instant;

/**
 * Author: Nurislom
 * <br/>
 * Date: 4/14/2023
 * <br/>
 * Time: 8:16 PM
 * <br/>
 * Package: org.khasanof.ratelimitingwithspring.core
 */
@Service
public class SimpleRateLimitingService  implements RateLimiting {

    private LocalRateLimiting localRateLimiting;

    @Override
    public boolean consumeRequest() {
        return false;
    }

    @Override
    public boolean consumeRequest(int token) {
        return false;
    }

    private boolean consumeMuch(int token) {
        if (checkIsNoLimit()) {
            if (localRateLimiting.getCreatedAt().isAfter(Instant.now())) {
                return true;
            }
        } else {
            Bucket bucket = localRateLimiting.getBucket();

            if (localRateLimiting.getToken() > 1) {
                if (bucket.tryConsume(1)) {
                    localRateLimiting.setToken(localRateLimiting.getToken() - 1);
                } else {
                    throw new RuntimeException("Too Many Request");
                }
            } else {
                if (localRateLimiting.getRefillCount() >= 1) {
                    localRateLimiting.setToken(localRateLimiting.getTokenCount());
                    localRateLimiting.getBucket().reset();
                }
            }

        }
    }

    @Override
    public Long availableToken() {
        return null;
    }

    @Override
    public Long availableRefill() {
        return null;
    }

    @Override
    public void replaceConfiguration(LocalRateLimiting rateLimiting) {

    }

    @Override
    public void addRefill(Long refillCount) {

    }

    @Override
    public void addTokens(Long tokenCount) {

    }

    private boolean checkIsNoLimit() {
        return localRateLimiting.isNoLimit();
    }
}
