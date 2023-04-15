package org.khasanof.ratelimitingwithspring.core.limiting;

import io.github.bucket4j.Bucket;

import java.time.Duration;
import java.time.Instant;
import java.util.Objects;

/**
 * Author: Nurislom
 * <br/>
 * Date: 4/14/2023
 * <br/>
 * Time: 8:20 PM
 * <br/>
 * Package: org.khasanof.ratelimitingwithspring.core.limiting
 */
public class LocalRateLimiting {

    private Long tokenCount;

    private Long token;

    private Duration duration;

    private boolean noLimit;

    private Long refillCount;

    private Bucket bucket;

    private Instant createdAt;

    public LocalRateLimiting() {
    }

    public LocalRateLimiting(Long token, Duration duration, boolean noLimit, Long refillCount, Bucket bucket) {
        this.token = token;
        this.tokenCount = token;
        this.duration = duration;
        this.noLimit = noLimit;
        this.refillCount = refillCount;
        this.bucket = bucket;
        this.createdAt = Instant.now();
    }

    public Long getToken() {
        return token;
    }

    public void setToken(Long token) {
        this.token = token;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public boolean isNoLimit() {
        return noLimit;
    }

    public void setNoLimit(boolean noLimit) {
        this.noLimit = noLimit;
    }

    public Long getRefillCount() {
        return refillCount;
    }

    public void setRefillCount(Long refillCount) {
        this.refillCount = refillCount;
    }

    public Bucket getBucket() {
        return bucket;
    }

    public void setBucket(Bucket bucket) {
        this.bucket = bucket;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Long getTokenCount() {
        return tokenCount;
    }

    public void setTokenCount(Long tokenCount) {
        this.tokenCount = tokenCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LocalRateLimiting that = (LocalRateLimiting) o;

        if (noLimit != that.noLimit) return false;
        if (!Objects.equals(token, that.token)) return false;
        if (!Objects.equals(duration, that.duration)) return false;
        if (!Objects.equals(refillCount, that.refillCount)) return false;
        if (!Objects.equals(bucket, that.bucket)) return false;
        return Objects.equals(createdAt, that.createdAt);
    }

    @Override
    public int hashCode() {
        int result = token != null ? token.hashCode() : 0;
        result = 31 * result + (duration != null ? duration.hashCode() : 0);
        result = 31 * result + (noLimit ? 1 : 0);
        result = 31 * result + (refillCount != null ? refillCount.hashCode() : 0);
        result = 31 * result + (bucket != null ? bucket.hashCode() : 0);
        result = 31 * result + (createdAt != null ? createdAt.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "LocalRateLimiting{" +
                "token=" + token +
                ", duration=" + duration +
                ", noLimit=" + noLimit +
                ", refillCount=" + refillCount +
                ", bucket=" + bucket +
                ", createdAt=" + createdAt +
                '}';
    }
}
