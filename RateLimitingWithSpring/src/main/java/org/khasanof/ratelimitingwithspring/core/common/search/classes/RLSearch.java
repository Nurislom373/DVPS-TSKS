package org.khasanof.ratelimitingwithspring.core.common.search.classes;

import lombok.*;

import java.util.Map;

/**
 * Author: Nurislom
 * <br/>
 * Date: 4/19/2023
 * <br/>
 * Time: 12:20 AM
 * <br/>
 * Package: org.khasanof.ratelimitingwithspring.core.limiting
 */
@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@EqualsAndHashCode
@NoArgsConstructor
public class RLSearch {
    private String key;
    private String uri;
    private String method;
    private Map<String, String> attributes;
}
