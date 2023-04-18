package org.khasanof.ratelimitingwithspring.core.strategy.tariff.classes;

import lombok.*;
import org.khasanof.ratelimitingwithspring.core.domain.enums.RequestType;
import org.khasanof.ratelimitingwithspring.core.domain.enums.TimeType;
import org.khasanof.ratelimitingwithspring.core.strategy.BaseRS;

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
@EqualsAndHashCode
@NoArgsConstructor
public class RSTariff implements BaseRS {
    private String name;
    private RequestType requestType;
    private Long requestCount;
    private TimeType timeType;
    private Long timeCount;
}
