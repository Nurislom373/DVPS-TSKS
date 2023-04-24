package org.khasanof.ratelimitingwithspring.core.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.Serializable;
import java.util.Map;

/**
 * Author: Nurislom
 * <br/>
 * Date: 4/19/2023
 * <br/>
 * Time: 3:09 PM
 * <br/>
 * Package: org.khasanof.ratelimitingwithspring.core.domain
 */
@Entity
@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "api", uniqueConstraints = {
        @UniqueConstraint(columnNames = "id")
})
public class Api implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Basic(optional = false)
    private String url;

    @Enumerated(EnumType.STRING)
    @Basic(optional = false)
    private RequestMethod method;

    // Path Variables
    @ElementCollection(fetch = FetchType.EAGER)
    @MapKeyColumn(name = "param_key")
    @Column(name = "param_value")
    private Map<String, String> attributes;

}
