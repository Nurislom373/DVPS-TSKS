package org.khasanof.ratelimitingwithspring.core.limiting;

import java.time.Duration;
import java.time.Instant;

public class RateLimitingBuilder {

    private final LocalRateLimiting rateLimiting = new LocalRateLimiting();

    public RateLimitingBuilder token(Long token) {
        rateLimiting.setToken(token);
        rateLimiting.setUndiminishedCount(token);
        return this;
    }

    public RateLimitingBuilder noLimit(boolean noLimit) {
        rateLimiting.setNoLimit(noLimit);
        return this;
    }

    public RateLimitingBuilder duration(Duration duration) {
        rateLimiting.setDuration(duration);
        return this;
    }

    public RateLimitingBuilder refillCount(Long refillCount) {
        rateLimiting.setRefillCount(refillCount);
        return this;
    }

    public SimpleRateLimiting build() {
        rateLimiting.setCreatedAt(Instant.now());
        SimpleRateLimiting limiting = new SimpleRateLimiting(null);
        limiting.addLocalBuilder(rateLimiting);
        return limiting;
    }



}
