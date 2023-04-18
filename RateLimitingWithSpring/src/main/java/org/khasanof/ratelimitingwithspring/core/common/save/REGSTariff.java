package org.khasanof.ratelimitingwithspring.core.common.save;

import lombok.*;

import java.util.List;

/**
 * Author: Nurislom
 * <br/>
 * Date: 4/19/2023
 * <br/>
 * Time: 12:33 AM
 * <br/>
 * Package: org.khasanof.ratelimitingwithspring.core.common.save
 */
@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@EqualsAndHashCode
@NoArgsConstructor
public class REGSTariff {
    private String key;
    private String name;
    private List<REGSTariffApi> api;
    private Long refillCount;
}
