package org.khasanof.ratelimitingwithspring.core.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;

/**
 * Author: Nurislom
 * <br/>
 * Date: 4/13/2023
 * <br/>
 * Time: 2:00 PM
 * <br/>
 * Package: org.khasanof.ratelimitingwithspring.domain
 */
@Entity
@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "api", uniqueConstraints = {
        @UniqueConstraint(name = "uniqueId", columnNames = "id")
})
public class Api {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Basic(optional = false)
    private String url;

    @Enumerated(EnumType.STRING)
    @Basic(optional = false)
    private RequestMethod method;

    // Path Variables
    @ElementCollection
    @CollectionTable(
            name = "api_variable",
            joinColumns = {
                    @JoinColumn(name = "api_id", referencedColumnName = "id",
                    foreignKey = @ForeignKey(name = "api_id_fk"))
            }
    )
    @MapKeyJoinColumn(name = "var_name")
    @Column(name = "var_value")
    private Map<String, String> variables;
}
