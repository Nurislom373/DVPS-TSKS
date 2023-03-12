package org.khasanof.reactiveuicaching.domain;

import lombok.*;
import org.khasanof.reactiveuicaching.enums.Status;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

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
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "transaction")
public class TransactionEntity {

    @Id
    private Long id;
    @Column(value = "amount")
    private BigDecimal amount;
    @Column(value = "status")
    private Status status;
    @Column(value = "from_card")
    private String fromCard;
    @Column(value = "to_card")
    private String toCard;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
