package org.khasanof.ratelimitingwithspring.core.strategy.tariff.classes;

import lombok.*;
import org.khasanof.ratelimitingwithspring.core.domain.enums.RequestType;
import org.khasanof.ratelimitingwithspring.core.domain.enums.TimeType;
import org.khasanof.ratelimitingwithspring.core.strategy.BaseRS;

import java.util.Objects;

/**
 * Author: Nurislom
 * <br/>
 * Date: 4/18/2023
 * <br/>
 * Time: 4:42 PM
 * <br/>
 * Package: org.khasanof.ratelimitingwithspring.core.strategy.tariff.read
 */
@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class RSTariff implements BaseRS {
    private String name;
    private RequestType requestType;
    private Long requestCount;
    private TimeType timeType;
    private Long timeCount;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RSTariff rsTariff = (RSTariff) o;
        return Objects.equals(name, rsTariff.name) && requestType == rsTariff.requestType &&
                Objects.equals(requestCount, rsTariff.requestCount) && timeType == rsTariff.timeType &&
                Objects.equals(timeCount, rsTariff.timeCount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, requestType, requestCount, timeType, timeCount);
    }
}
