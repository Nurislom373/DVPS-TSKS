package org.khasanof.ratelimitingwithspring.core.strategy.read;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.khasanof.ratelimitingwithspring.domain.enums.Method;

import java.util.List;
import java.util.Objects;

/**
 * Author: Nurislom
 * <br/>
 * Date: 4/13/2023
 * <br/>
 * Time: 11:32 PM
 * <br/>
 * Package: org.khasanof.ratelimitingwithspring.core.json
 */
public class ReadLimit {

    private String url;
    private Method method;
    private boolean customize;
    private List<ReadPlan> plans;

    public ReadLimit() {
    }

    public ReadLimit(String url, Method method, boolean customize, List<ReadPlan> plans) {
        this.url = url;
        this.method = method;
        this.customize = customize;
        this.plans = plans;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public boolean isCustomize() {
        return customize;
    }

    public void setCustomize(boolean customize) {
        this.customize = customize;
    }

    public List<ReadPlan> getPlans() {
        return plans;
    }

    public void setPlans(List<ReadPlan> plans) {
        this.plans = plans;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ReadLimit readLimit = (ReadLimit) o;

        if (customize != readLimit.customize) return false;
        if (!Objects.equals(url, readLimit.url)) return false;
        if (method != readLimit.method) return false;
        return Objects.equals(plans, readLimit.plans);
    }

    @Override
    public int hashCode() {
        int result = url != null ? url.hashCode() : 0;
        result = 31 * result + (method != null ? method.hashCode() : 0);
        result = 31 * result + (customize ? 1 : 0);
        result = 31 * result + (plans != null ? plans.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ReadLimit{" +
                "url='" + url + '\'' +
                ", method=" + method +
                ", customize=" + customize +
                ", plans=" + plans +
                '}';
    }
}
