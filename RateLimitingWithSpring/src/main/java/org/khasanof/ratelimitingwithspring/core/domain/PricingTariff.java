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
@EqualsAndHashCode
@AllArgsConstructor
@Table(name = "pricing_package", uniqueConstraints = {
        @UniqueConstraint(name = "uniqueId", columnNames = "id")
})
public class PricingTariff {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "key", nullable = false, updatable = false)
    private String key;

    @ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinColumn(name = "package_id", referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "package_id_fk"), nullable = false)
    private Tariff tariff;

    @OneToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinColumn(name = "api_id", referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "api_id_fk"), nullable = false)
    private List<Api> apis;

    @Embedded
    private LimitsEmbeddable limitsEmbeddable;

    @Column(name = "refill_count", nullable = false)
    private Long refillCount;
}
