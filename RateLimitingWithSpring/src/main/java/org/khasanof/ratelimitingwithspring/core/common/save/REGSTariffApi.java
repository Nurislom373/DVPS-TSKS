package org.khasanof.ratelimitingwithspring.core.common.save;

import lombok.*;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;

/**
 * Author: Nurislom
 * <br/>
 * Date: 4/19/2023
 * <br/>
 * Time: 12:38 AM
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
public class REGSTariffApi {
    private String url;
    private RequestMethod method;
    private Map<String, String> attributes;
}
