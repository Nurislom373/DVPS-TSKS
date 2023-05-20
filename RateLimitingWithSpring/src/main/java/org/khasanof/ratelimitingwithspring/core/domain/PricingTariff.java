package org.khasanof.ratelimitingwithspring.core.domain;

import jakarta.persistence.*;
import lombok.*;
import org.khasanof.ratelimitingwithspring.core.domain.embeddable.LimitsEmbeddable;

import java.util.List;

/**
 * Author: Nurislom
 * <br/>
 * Date: 4/18/2023
 * <br/>
 * Time: 3:33 PM
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
@Table(name = "pricing_tariff", uniqueConstraints = {
        @UniqueConstraint(name = "uniqueId", columnNames = "id"),
        @UniqueConstraint(name = "uniqueKeyTariff", columnNames = {"key", "package_id"})
})
public class PricingTariff extends Auditable {

    @Column(name = "key", nullable = false, updatable = false)
    private String key;

    @ManyToOne
    @JoinColumn(name = "package_id", referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "package_id_fk"), nullable = false)
    private Tariff tariff;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "pricing_tariff_api")
    @Column(name = "api_id")
    private List<Long> apis;

    @Embedded
    private LimitsEmbeddable limitsEmbeddable;

    @Column(name = "refill_count", nullable = false)
    private Long refillCount;
}
