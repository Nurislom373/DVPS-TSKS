package org.khasanof.ratelimitingwithspring.core.event;

import lombok.*;
import org.khasanof.ratelimitingwithspring.core.common.search.classes.PTA;
import org.khasanof.ratelimitingwithspring.core.limiting.LocalRateLimiting;

/**
 * Author: Nurislom
 * <br/>
 * Date: 13.05.2023
 * <br/>
 * Time: 21:06
 * <br/>
 * Package: org.khasanof.ratelimitingwithspring.core.event
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class LimitUpdateEvent {
    private String key;
    private PTA pta;
    private LocalRateLimiting localRateLimiting;
}
