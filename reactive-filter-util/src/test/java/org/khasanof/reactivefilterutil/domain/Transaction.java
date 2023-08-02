package org.khasanof.reactivefilterutil.domain;

import lombok.*;
import org.khasanof.reactivefilterutil.domain.enums.PayType;
import org.khasanof.reactivefilterutil.domain.enums.TransactionStatus;
import org.springframework.data.relational.core.mapping.Table;

/**
 * @author Nurislom
 * @see org.khasanof.reactivefilterutil.domain
 * @since 31.07.2023 11:08
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table("transaction")
public class Transaction {

    private Long id;
    private String senderPan;
    private Long senderAmount;
    private String recipientPan;
    private Long recipientAmount;
    private PayType payType;
    private TransactionStatus status;

}
