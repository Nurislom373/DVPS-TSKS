package org.khasanof.ratelimitingwithspring.core.limiting;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;

import java.time.Duration;

public class RateLimitingBuilder {

    private final LocalRateLimiting rateLimiting = new LocalRateLimiting();

    public RateLimitingBuilder token(Long token) {
        rateLimiting.setToken(token);
        rateLimiting.setTokenCount(token);
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
        Bandwidth bandwidth = Bandwidth.simple(rateLimiting.getToken(),
                rateLimiting.getDuration());
        Bucket bucket = Bucket.builder()
                .addLimit(bandwidth)
                .build();
        rateLimiting.setBucket(bucket);
        SimpleRateLimiting limiting = new SimpleRateLimiting();
        limiting.addLocalBuilder(rateLimiting);
        return limiting;
    }

}
