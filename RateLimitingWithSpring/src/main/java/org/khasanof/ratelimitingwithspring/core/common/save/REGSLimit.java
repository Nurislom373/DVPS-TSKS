package org.khasanof.ratelimitingwithspring.core.common.save;

import lombok.*;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;

/**
 * Author: Nurislom
 * <br/>
 * Date: 4/17/2023
 * <br/>
 * Time: 11:17 AM
 * <br/>
 * Package: org.khasanof.ratelimitingwithspring.core.strategy.read
 */
@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@EqualsAndHashCode
@NoArgsConstructor
public class REGSLimit {
    private String url;
    private RequestMethod method;
    private Map<String, String> attributes;
    private String plan;
    private Long refillCount;
}
