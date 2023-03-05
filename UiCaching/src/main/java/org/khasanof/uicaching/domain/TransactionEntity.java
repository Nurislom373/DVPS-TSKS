package org.khasanof.uicaching.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.CurrentTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.khasanof.uicaching.enums.Status;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

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
    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
