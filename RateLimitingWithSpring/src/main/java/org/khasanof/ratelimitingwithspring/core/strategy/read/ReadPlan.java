package org.khasanof.ratelimitingwithspring.core.strategy.read;

import lombok.*;
import org.khasanof.ratelimitingwithspring.core.PricingPlan;
import org.khasanof.ratelimitingwithspring.domain.enums.RequestType;
import org.khasanof.ratelimitingwithspring.domain.enums.TimeType;

import java.util.Objects;

/**
 * Author: Nurislom
 * <br/>
 * Date: 4/13/2023
 * <br/>
 * Time: 11:33 PM
 * <br/>
 * Package: org.khasanof.ratelimitingwithspring.core.json
 */
public class ReadPlan {

    private PricingPlan plan;
    private RequestType requestType;
    private Long requestCount;
    private TimeType timeType;

    public ReadPlan() {
    }

    public ReadPlan(PricingPlan plan, RequestType requestType, Long requestCount, TimeType timeType) {
        this.plan = plan;
        this.requestType = requestType;
        this.requestCount = requestCount;
        this.timeType = timeType;
    }

    public PricingPlan getPlan() {
        return plan;
    }

    public void setPlan(PricingPlan plan) {
        this.plan = plan;
    }

    public RequestType getRequestType() {
        return requestType;
    }

    public void setRequestType(RequestType requestType) {
        this.requestType = requestType;
    }

    public Long getRequestCount() {
        return requestCount;
    }

    public void setRequestCount(Long requestCount) {
        this.requestCount = requestCount;
    }

    public TimeType getTimeType() {
        return timeType;
    }

    public void setTimeType(TimeType timeType) {
        this.timeType = timeType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ReadPlan readPlan = (ReadPlan) o;

        if (plan != readPlan.plan) return false;
        if (requestType != readPlan.requestType) return false;
        if (!Objects.equals(requestCount, readPlan.requestCount))
            return false;
        return timeType == readPlan.timeType;
    }

    @Override
    public int hashCode() {
        int result = plan != null ? plan.hashCode() : 0;
        result = 31 * result + (requestType != null ? requestType.hashCode() : 0);
        result = 31 * result + (requestCount != null ? requestCount.hashCode() : 0);
        result = 31 * result + (timeType != null ? timeType.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ReadPlan{" +
                "plan=" + plan +
                ", requestType=" + requestType +
                ", requestCount=" + requestCount +
                ", timeType=" + timeType +
                '}';
    }
}
