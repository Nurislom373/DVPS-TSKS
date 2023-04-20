package org.khasanof.ratelimitingwithspring.core.domain;

import jakarta.persistence.*;
import lombok.*;
import org.khasanof.ratelimitingwithspring.core.domain.embeddable.LimitsEmbeddable;

/**
 * Author: Nurislom
 * <br/>
 * Date: 4/13/2023
 * <br/>
 * Time: 12:18 PM
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
@EqualsAndHashCode
@Table(name = "limited", uniqueConstraints = {
        @UniqueConstraint(columnNames = "id")
})
public class Limited {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "api_id", referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "api_id_fk"))
    private Api api;

    @Basic(optional = false)
    private String plan;

    @Embedded
    private LimitsEmbeddable limitsEmbeddable;

}
