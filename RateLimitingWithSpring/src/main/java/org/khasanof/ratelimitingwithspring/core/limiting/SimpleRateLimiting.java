package org.khasanof.ratelimitingwithspring.core.limiting;

import io.github.bucket4j.Bucket;
import io.github.bucket4j.ConsumptionProbe;
import org.khasanof.ratelimitingwithspring.core.RateLimiting;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.Instant;
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
public class SimpleRateLimiting implements RateLimiting {

    public final Logger log = LoggerFactory.getLogger(this.getClass());

    private LocalRateLimiting localRateLimiting;

    private long nanosToWaitForRefill;

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
        if (localRateLimiting.getRefillCount() >= 1) {
            return this.nanosToWaitForRefill;
        }
        return 0;
    }

    private Result consumerMuchAsPossible(int token) {
        if (localRateLimiting.isNoLimit()) {

            if (localRateLimiting.getCreatedAt().isAfter(Instant.now())) {
                log.info("Working Well");
                return new Result(true, Message.GET_NO_LIMIT, HttpStatus.OK);
            } else {

                if (localRateLimiting.getRefillCount() >= 1) {

                    log.info("Refill Count : {}", localRateLimiting.getRefillCount());
                    localRateLimiting.setRefillCount(localRateLimiting.getRefillCount() - 1);
                    localRateLimiting.setToken(localRateLimiting.getUndiminishedCount());
                    return new Result(true, Message.GET_REFILL, HttpStatus.OK);
                } else {
                    log.warn("Your limit has been reached!");
                    return new Result(false, Message.LIMIT_HAS_BEEN_REACHED, HttpStatus.BAD_REQUEST);
                }

            }
        } else {
            System.out.println("localRateLimiting = " + localRateLimiting);
            Bucket bucket = localRateLimiting.getBucket();

            long beforeConsumedTokens = bucket.getAvailableTokens();
            log.info("Available Tokens Before Consumed : {}", beforeConsumedTokens);
            ConsumptionProbe consumptionProbe = bucket.tryConsumeAndReturnRemaining(token);
            if (consumptionProbe.isConsumed()) {

                long afterConsumedTokens = bucket.getAvailableTokens();
                log.info("Local Rate Limiting Tokens : {}", localRateLimiting.getToken());
                log.info("Available Tokens After Consumed  : {}", afterConsumedTokens);

                if (afterConsumedTokens < localRateLimiting.getToken()) {

                    log.info("Working Well");
                    localRateLimiting.setToken(localRateLimiting.getToken() - token);
                    return new Result(true, Message.GET_TOKEN, HttpStatus.OK);
                } else {

                    if (localRateLimiting.getRefillCount() >= 1) {

                        log.info("Refill Count : {}", localRateLimiting.getRefillCount());
                        localRateLimiting.setRefillCount(localRateLimiting.getRefillCount() - 1);
                        localRateLimiting.setToken(afterConsumedTokens);
                        return new Result(true, Message.GET_REFILL, HttpStatus.OK);
                    } else {

                        log.warn("Your limit has been reached. Token Count : {}, Refill Count : {}",
                                afterConsumedTokens, localRateLimiting.getRefillCount());
                        return new Result(false, Message.LIMIT_HAS_BEEN_REACHED, HttpStatus.BAD_REQUEST);
                    }
                }

            } else {

                log.warn("getNanosToWaitForRefill - {}", consumptionProbe.getNanosToWaitForRefill());
                log.warn("getNanosToWaitForReset - {}", consumptionProbe.getNanosToWaitForReset());

                setNanosToWaitForRefill(consumptionProbe.getNanosToWaitForRefill());

                log.warn("Too Many Request!");
                return new Result(false, Message.TOO_MANY_REQUEST, HttpStatus.TOO_MANY_REQUESTS);
            }
        }
    }

    @Override
    public Long availableToken() {
        return localRateLimiting.getToken();
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

    @Override
    public LocalRateLimiting getLocalRateLimiting() {
        return this.localRateLimiting;
    }

    public void addLocalBuilder(LocalRateLimiting rateLimiting) {
        this.localRateLimiting = rateLimiting;
    }

    public void setNanosToWaitForRefill(long nanosToWaitForRefill) {
        this.nanosToWaitForRefill = nanosToWaitForRefill;
    }

    public static class Result {

        private boolean result;
        private HttpStatus status;
        private String message;

        public Result(boolean result, String message, HttpStatus status) {
            this.result = result;
            this.message = message;
            this.status = status;
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
