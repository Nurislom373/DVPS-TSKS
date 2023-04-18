package org.khasanof.ratelimitingwithspring.core.domain;

import jakarta.persistence.*;
import lombok.*;

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
@Table(name = "pricing_api", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"id"}),
        @UniqueConstraint(columnNames = {"key", "limited_id"})
})
public class PricingApi {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "key", nullable = false)
    private String key;

    @ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinColumn(name = "limited_id", referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "limited_id_fk"), nullable = false)
    private Limited limited;

    @Column(name = "refill_count", nullable = false)
    private Long refillCount;
}
