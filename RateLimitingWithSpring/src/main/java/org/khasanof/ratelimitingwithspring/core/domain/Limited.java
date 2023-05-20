package org.khasanof.ratelimitingwithspring.core.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Cascade;
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
@EqualsAndHashCode(callSuper = true)
@Table(name = "limited", uniqueConstraints = {
        @UniqueConstraint(columnNames = "id"),
        @UniqueConstraint(columnNames = {"api_id", "plan"})
})
public class Limited extends Auditable {

    @ManyToOne
    @JoinColumn(name = "api_id", referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "api_id_fk"))
    private Api api;

    @Basic(optional = false)
    private String plan;

    @Embedded
    private LimitsEmbeddable limitsEmbeddable;

}
