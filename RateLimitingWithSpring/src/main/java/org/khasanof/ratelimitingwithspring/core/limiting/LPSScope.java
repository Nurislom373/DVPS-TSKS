package org.khasanof.ratelimitingwithspring.core.limiting;

import lombok.Getter;
import lombok.Setter;
import org.khasanof.ratelimitingwithspring.core.common.search.classes.PTA;

/**
 * Author: Nurislom
 * <br/>
 * Date: 14.05.2023
 * <br/>
 * Time: 11:20
 * <br/>
 * Package: org.khasanof.ratelimitingwithspring.core.limiting
 */
@Getter
@Setter
public class LPSScope {
    private LocalRateLimiting localRateLimiting;
    private PTA pta;
    private String key;
}
