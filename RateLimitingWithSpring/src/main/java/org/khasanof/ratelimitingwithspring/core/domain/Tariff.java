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
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Table(name = "tariff", uniqueConstraints = {
        @UniqueConstraint(name = "uniqueId", columnNames = "id")
})
public class Tariff extends Auditable {

    @Column(name = "name", nullable = false)
    private String name;

    @Embedded
    private LimitsEmbeddable limitsEmbeddable;
}
