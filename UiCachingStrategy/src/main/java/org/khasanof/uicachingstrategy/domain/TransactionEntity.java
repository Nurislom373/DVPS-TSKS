package org.khasanof.uicachingstrategy.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.khasanof.uicachingstrategy.enums.Status;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Author: Nurislom
 * <br/>
 * Date: 2/28/2023
 * <br/>
 * Time: 4:28 PM
 * <br/>
 * Package: org.khasanof.uicaching.domain
 */
@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "transaction", uniqueConstraints = {
        @UniqueConstraint(columnNames = "id")
})
public class TransactionEntity {

    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(name = "amount", nullable = false)
    private BigDecimal amount;
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;
    @Column(name = "from_card", nullable = false, length = 16)
    private String fromCard;
    @Column(name = "to_card", nullable = false, length = 16)
    private String toCard;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
