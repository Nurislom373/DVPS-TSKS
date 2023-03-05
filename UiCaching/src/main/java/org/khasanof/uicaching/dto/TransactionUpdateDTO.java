package org.khasanof.uicaching.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.khasanof.uicaching.enums.Status;

import java.math.BigDecimal;

/**
 * Author: Nurislom
 * <br/>
 * Date: 2/28/2023
 * <br/>
 * Time: 6:45 PM
 * <br/>
 * Package: org.khasanof.uicaching.dto
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class TransactionUpdateDTO {
    @NotNull
    @Min(1)
    private Long id;
    @NotNull
    @Min(1)
    private BigDecimal amount;
    @NotNull
    private Status status;
}
