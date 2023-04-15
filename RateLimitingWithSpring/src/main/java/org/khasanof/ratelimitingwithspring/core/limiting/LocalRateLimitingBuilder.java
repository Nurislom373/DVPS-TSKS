package org.khasanof.ratelimitingwithspring.core.limiting;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;

import java.time.Duration;

public class LocalRateLimitingBuilder {

    private final LocalRateLimiting rateLimiting = new LocalRateLimiting();

    public LocalRateLimitingBuilder token(Long token) {
        rateLimiting.setToken(token);
        rateLimiting.setTokenCount(token);
        return this;
    }

    public LocalRateLimitingBuilder noLimit(boolean noLimit) {
        rateLimiting.setNoLimit(noLimit);
        return this;
    }

    public LocalRateLimitingBuilder duration(Duration duration) {
        rateLimiting.setDuration(duration);
        return this;
    }

    public LocalRateLimitingBuilder refillCount(Long refillCount) {
        rateLimiting.setRefillCount(refillCount);
        return this;
    }

    public LocalRateLimiting build() {
        Bandwidth bandwidth = Bandwidth.simple(rateLimiting.getToken(),
                rateLimiting.getDuration());
        Bucket bucket = Bucket.builder()
                .addLimit(bandwidth)
                .build();
        rateLimiting.setBucket(bucket);
        return rateLimiting;
    }

}
