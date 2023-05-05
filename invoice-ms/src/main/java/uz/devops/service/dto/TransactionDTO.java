package uz.devops.service.dto;

import lombok.*;

import javax.persistence.Column;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.Instant;

/**
 * Author: Nurislom
 * <br/>
 * Date: 4/29/2023
 * <br/>
 * Time: 9:55 AM
 * <br/>
 * Package: uz.devops.service.dto
 */
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransactionDTO {

    private Long id;

    private Long requestAmount;

    private Integer requestCcy;

    private String requestTrId;

    private String requestLogin;

    private String requestGroup;

    private String senderName;

    private String senderAccount;

    private Long senderServiceId = 0L;

    private Long senderAmount;

    private Integer senderCcy;

    private String senderTrId;

    private Boolean senderSuccess;

    private Instant senderDate;

    private String senderStatus;

    private String senderStatusText;

    private Long senderBillId = 0L;

    private String senderGateway;

    private Double gatewayAmount;

    private String recipientName;

    private String recipientAccount;

    private Long recipientServiceId;

    private Long recipientAmount;

    private Integer recipientCcy;

    private String recipientTrId;

    private Boolean recipientSuccess;

    private Instant recipientDate;


    private String recipientStatus;


    private String recipientStatusText;


    private Long recipientBillId;


    private String recipientGateway;

    private Long systemCommissionUpAmount;

    private Long systemFeeAmount;

    private Long agentFeeAmount;

    private Double roundAmount;

    private Double systemFee;

    private Integer systemCurrency;

    private Integer ccyRate;

    private Instant date;

    private String requestSubAccount;

    private String eposMerchant;

    private String eposTerminal;

    private String category;

    private String promo;

}
