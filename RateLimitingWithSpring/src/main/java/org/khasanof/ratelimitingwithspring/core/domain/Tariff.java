package org.khasanof.ratelimitingwithspring.core.domain;

import jakarta.persistence.*;
import lombok.*;
import org.khasanof.ratelimitingwithspring.core.domain.embeddable.LimitsEmbeddable;

/**
 * Author: Nurislom
 * <br/>
 * Date: 4/18/2023
 * <br/>
 * Time: 2:45 PM
 * <br/>
 * Package: org.khasanof.ratelimitingwithspring.core.domain
 */
@Entity
@Getter
@Setter
@Builder
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "package", uniqueConstraints = {
        @UniqueConstraint(name = "uniqueId", columnNames = "id")
})
public class Tariff {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Embedded
    private LimitsEmbeddable limitsEmbeddable;
}
