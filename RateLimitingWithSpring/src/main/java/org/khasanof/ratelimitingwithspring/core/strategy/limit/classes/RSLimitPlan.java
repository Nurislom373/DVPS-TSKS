package org.khasanof.ratelimitingwithspring.core.strategy.limit.classes;

import lombok.*;
import org.khasanof.ratelimitingwithspring.core.domain.enums.RequestType;
import org.khasanof.ratelimitingwithspring.core.domain.enums.TimeType;

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
@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@EqualsAndHashCode
@NoArgsConstructor
public class RSLimitPlan {
    private String plan;
    private RequestType requestType;
    private Long requestCount;
    private TimeType timeType;
    private Long timeCount;
}
