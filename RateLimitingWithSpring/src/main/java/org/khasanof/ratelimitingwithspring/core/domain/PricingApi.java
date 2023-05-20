package org.khasanof.ratelimitingwithspring.core.domain;

import jakarta.persistence.*;
import lombok.*;
import org.khasanof.ratelimitingwithspring.core.domain.embeddable.LimitsEmbeddable;

/**
 * Author: Nurislom
 * <br/>
 * Date: 4/13/2023
 * <br/>
 * Time: 9:46 PM
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
@EqualsAndHashCode(callSuper = true)
@Table(name = "pricing_api", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"id"}),
        @UniqueConstraint(columnNames = {"key", "limited_id"})
})
public class PricingApi extends Auditable {

    @Column(name = "key", nullable = false)
    private String key;

    @ManyToOne
    @JoinColumn(name = "limited_id", referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "limited_id_fk"), nullable = false)
    private Limited limited;

    @Embedded
    private LimitsEmbeddable limitsEmbeddable;

    @Column(name = "refill_count", nullable = false)
    private Long refillCount;
}
