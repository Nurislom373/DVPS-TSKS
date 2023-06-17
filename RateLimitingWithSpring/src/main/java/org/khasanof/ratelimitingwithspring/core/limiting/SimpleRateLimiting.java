package org.khasanof.ratelimitingwithspring.core.limiting;

import org.khasanof.ratelimitingwithspring.core.RateLimiting;
import org.khasanof.ratelimitingwithspring.core.common.search.classes.PTA;
import org.khasanof.ratelimitingwithspring.core.event.LimitUpdateEvent;
import org.khasanof.ratelimitingwithspring.core.exceptions.InvalidParamException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.context.annotation.RequestScope;

import java.io.Serializable;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

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
@RequestScope
public class SimpleRateLimiting implements RateLimiting, Serializable {

    public final Logger log = LoggerFactory.getLogger(this.getClass());

    private final ApplicationEventPublisher eventPublisher;

    private LocalRateLimiting localRateLimiting;
    private PTA pta;
    private String key;

    private long nanosToWaitForRefill;

    public SimpleRateLimiting(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    @Override
    public Result consumeRequest() {
        return consumerMuchAsPossible(1);
    }

    @Override
    public Result consumeRequest(int token) {
        return consumerMuchAsPossible(token);
    }

    @Override
    public long getNanosToWaitForRefill() {
        return this.nanosToWaitForRefill;
    }

    // This Method Copy New Class
    private Result consumerMuchAsPossible(int token) {
        Instant endOfLimitInstant = localRateLimitingToSeconds(localRateLimiting);
        System.out.println("rateLimitingToSeconds = " + endOfLimitInstant);
        if (localRateLimiting.isNoLimit()) {
            if (Instant.now().compareTo(endOfLimitInstant) < 0) {
                log.info("Working Well");
                localRateLimiting.setUpdatedAt(Instant.now());
                eventPush(new LimitUpdateEvent(key, pta, localRateLimiting));
                return new SimpleRateLimiting.Result(true, Message.GET_NO_LIMIT, HttpStatus.OK);
            } else {
                if (localRateLimiting.getRefillCount() >= 1) {
                    log.info("Refill Count : {}", localRateLimiting.getRefillCount());
                    localRateLimiting.setRefillCount(localRateLimiting.getRefillCount() - 1);
                    localRateLimiting.setToken(localRateLimiting.getUndiminishedCount());
                    localRateLimiting.setRefillUpdatedAt(Instant.now());
                    localRateLimiting.setUpdatedAt(Instant.now());
                    eventPush(new LimitUpdateEvent(key, pta, localRateLimiting));
                    return new SimpleRateLimiting.Result(true, Message.GET_REFILL, HttpStatus.OK);
                } else {
                    log.warn("Your limit has been reached!");
                    return new SimpleRateLimiting.Result(false, Message.LIMIT_HAS_BEEN_REACHED, HttpStatus.BAD_REQUEST);
                }
            }
        } else {
            System.out.println("localRateLimiting = " + localRateLimiting);
            log.info("Available Tokens Before Consumed : {}", localRateLimiting.getToken());
            if (Instant.now().compareTo(endOfLimitInstant) < 0) {
                if (localRateLimiting.getToken() >= 1) {
                    localRateLimiting.setToken(localRateLimiting.getToken() - token);
                    log.info("Available Tokens After Consumed : {}", localRateLimiting.getToken());
                    localRateLimiting.setUpdatedAt(Instant.now());
                    eventPush(new LimitUpdateEvent(key, pta, localRateLimiting));
                    return new SimpleRateLimiting.Result(true, Message.GET_TOKEN, HttpStatus.OK);
                } else {
                    long endOfLimitInstantToSeconds = getEndOfLimitInstantToSeconds(endOfLimitInstant);
                    localRateLimiting.setUpdatedAt(Instant.now());
                    setNanosToWaitForRefill(endOfLimitInstantToSeconds);
                    return new SimpleRateLimiting.Result(false, Message.TOO_MANY_REQUEST,
                            HttpStatus.TOO_MANY_REQUESTS, getNanosToWaitForRefill());
                }
            } else {
                if (localRateLimiting.getRefillCount() >= 1) {
                    localRateLimiting.setRefillCount(localRateLimiting.getRefillCount() - 1);
                    localRateLimiting.setToken(localRateLimiting.getUndiminishedCount() - 1);
                    localRateLimiting.setRefillUpdatedAt(Instant.now());
                    localRateLimiting.setUpdatedAt(Instant.now());
                    log.info("Refill Count : {}", localRateLimiting.getRefillCount());
                    eventPush(new LimitUpdateEvent(key, pta, localRateLimiting));
                    return new SimpleRateLimiting.Result(true, Message.GET_REFILL, HttpStatus.OK);
                } else {
                    log.warn("Your limit has been reached. Token Count : {}, Refill Count : {}",
                            localRateLimiting.getToken(), localRateLimiting.getRefillCount());
                    setNanosToWaitForRefill(0);
                    localRateLimiting.setUpdatedAt(Instant.now());
                    return new SimpleRateLimiting.Result(false, Message.LIMIT_HAS_BEEN_REACHED,
                            HttpStatus.BAD_REQUEST, getNanosToWaitForRefill());
                }
            }
        }
    }

    private Instant localRateLimitingToSeconds(LocalRateLimiting localRateLimiting) {
        if (Objects.nonNull(localRateLimiting.getRefillUpdatedAt())) {
            return instantToEpochSeconds(localRateLimiting.getRefillUpdatedAt(), localRateLimiting.getDuration());
        }
        return instantToEpochSeconds(localRateLimiting.getCreatedAt(), localRateLimiting.getDuration());
    }

    private Instant instantToEpochSeconds(Instant date, Duration duration) {
        return date.plus(duration.toSeconds(), ChronoUnit.SECONDS);
    }

    private long getEndOfLimitInstantToSeconds(Instant endOfLimitInstant) {
        return endOfLimitInstant.minus(Instant.now().getEpochSecond(), ChronoUnit.SECONDS).getEpochSecond();
    }

    @Override
    public Long availableToken() {
        return localRateLimiting.getToken();
    }

    @Override
    public Long availableRefill() {
        return localRateLimiting.getRefillCount();
    }

    @Override
    public void replaceConfiguration(LocalRateLimiting rateLimiting) {
        Assert.notNull(rateLimiting, "rateLimiting must not be null!");
        this.localRateLimiting = rateLimiting;
    }

    @Override
    public void addRefill(Long refillCount) {
        Assert.notNull(refillCount, "refillCount must not null");
        if (refillCount >= 1) {
            localRateLimiting.setRefillCount(refillCount);
        } else {
            throw new InvalidParamException("refillCount param is less than one! must be greater or equal one");
        }
    }

    @Override
    public void addTokens(Long tokenCount) {
        Assert.notNull(tokenCount, "tokenCount must not null");
        if (tokenCount >= 1) {
            localRateLimiting.setToken(tokenCount);
        } else {
            throw new InvalidParamException("tokenCount param is less than one! must be greater or equal one");
        }
    }

    @Override
    public LocalRateLimiting getLocalRateLimiting() {
        return this.localRateLimiting;
    }

    private void eventPush(LimitUpdateEvent event) {
        eventPublisher.publishEvent(event);
    }

    public void addLocalBuilder(LocalRateLimiting rateLimiting) {
        this.localRateLimiting = rateLimiting;
    }

    public void setNanosToWaitForRefill(long nanosToWaitForRefill) {
        this.nanosToWaitForRefill = nanosToWaitForRefill;
    }

    public void setPta(PTA pta) {
        this.pta = pta;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public static class Result implements Serializable {

        private boolean result;
        private HttpStatus status;
        private String message;
        private long refillNanoSeconds;

        public Result(boolean result, String message, HttpStatus status) {
            this.result = result;
            this.message = message;
            this.status = status;
        }

        public Result(boolean result, String message, HttpStatus status, long refillNanoSeconds) {
            this.result = result;
            this.message = message;
            this.status = status;
            this.refillNanoSeconds = refillNanoSeconds;
        }

        public boolean isResult() {
            return result;
        }

        public void setResult(boolean result) {
            this.result = result;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public HttpStatus getStatus() {
            return status;
        }

        public void setStatus(HttpStatus status) {
            this.status = status;
        }

        public long getRefillNanoSeconds() {
            return refillNanoSeconds;
        }

        public void setRefillNanoSeconds(long refillNanoSeconds) {
            this.refillNanoSeconds = refillNanoSeconds;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Result result1 = (Result) o;

            if (result != result1.result) return false;
            if (status != result1.status) return false;
            return Objects.equals(message, result1.message);
        }

        @Override
        public int hashCode() {
            int result1 = (result ? 1 : 0);
            result1 = 31 * result1 + (status != null ? status.hashCode() : 0);
            result1 = 31 * result1 + (message != null ? message.hashCode() : 0);
            return result1;
        }

        @Override
        public String toString() {
            return "Result{" +
                    "result=" + result +
                    ", status=" + status +
                    ", message='" + message + '\'' +
                    '}';
        }
    }

    private static class Message {
        public static String GET_TOKEN = "Get token";
        public static String GET_NO_LIMIT = "Get no limit";
        public static String GET_REFILL = "Get refill";
        public static String TOO_MANY_REQUEST = "Too many request";
        public static String LIMIT_HAS_BEEN_REACHED = "Your limit has been reached";
    }
}
