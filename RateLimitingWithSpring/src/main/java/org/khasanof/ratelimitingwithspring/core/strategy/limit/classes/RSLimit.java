package org.khasanof.ratelimitingwithspring.core.strategy.limit.classes;

import lombok.*;
import org.khasanof.ratelimitingwithspring.core.strategy.BaseRS;
import org.khasanof.ratelimitingwithspring.core.utils.BaseUtils;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;
import java.util.Map;
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
@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class RSLimit implements BaseRS {
    private String url;
    private RequestMethod method;
    private List<RSLimitPlan> plans;
    private Map<String, String> variables;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RSLimit rsLimit = (RSLimit) o;

        if (!Objects.equals(url, rsLimit.url)) return false;
        if (method != rsLimit.method) return false;

        return BaseUtils.areEqual(variables, rsLimit.variables);
    }

    @Override
    public int hashCode() {
        int result = url != null ? url.hashCode() : 0;
        result = 31 * result + (method != null ? method.hashCode() : 0);
        result = 31 * result + (variables != null ? variables.hashCode() : 0);
        return result;
    }
}
