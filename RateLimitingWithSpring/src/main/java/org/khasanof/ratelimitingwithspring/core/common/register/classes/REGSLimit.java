package org.khasanof.ratelimitingwithspring.core.common.register.classes;

import lombok.*;
import org.khasanof.ratelimitingwithspring.core.utils.BaseUtils;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;
import java.util.Objects;

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
@NoArgsConstructor
public class REGSLimit {

    private String url;
    private RequestMethod method;
    private Map<String, String> attributes;
    private String plan;
    private Long refillCount;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        REGSLimit regsLimit = (REGSLimit) o;

        if (!Objects.equals(url, regsLimit.url)) return false;
        if (method != regsLimit.method) return false;
        if (attributes != null || regsLimit.attributes != null) {
            return (!BaseUtils.areEqual(attributes, regsLimit.attributes));
        } else {
            return true;
        }
    }

    @Override
    public int hashCode() {
        int result = url != null ? url.hashCode() : 0;
        result = 31 * result + (method != null ? method.hashCode() : 0);
        result = 31 * result + (attributes != null ? attributes.hashCode() : 0);
        return result;
    }
}
