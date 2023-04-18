package org.khasanof.ratelimitingwithspring.core.strategy.limit.classes;

import lombok.*;
import org.khasanof.ratelimitingwithspring.core.strategy.BaseRS;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;
import java.util.Map;

/**
 * Author: Nurislom
 * <br/>
 * Date: 4/13/2023
 * <br/>
 * Time: 11:32 PM
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
public class RSLimit implements BaseRS {
    private String url;
    private RequestMethod method;
    private List<RSLimitPlan> plans;
    private Map<String, String> variables;
}
