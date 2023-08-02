package org.khasanof.reactivefilterutil.domain;

import org.khasanof.reactivefilterutil.domain.enums.CardType;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;

/**
 * @author Nurislom
 * @see org.khasanof.reactivefilterutil.domain
 * @since 31.07.2023 11:12
 */
@Table("card_limit")
public class CardLimit {

    @Column("holder_name")
    private String holderName;

    @Column("pan")
    private String pan;

    @Column("bin")
    private String bin;

    @Column("expiry")
    private LocalDate expiry;

    @Column("value")
    private Long value;

    @Column("type")
    private CardType type;

    @Column("transaction_id")
    private Long transactionId;

}
