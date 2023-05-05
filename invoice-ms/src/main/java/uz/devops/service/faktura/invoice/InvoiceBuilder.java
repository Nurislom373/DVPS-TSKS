package uz.devops.service.faktura.invoice;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import uz.devops.config.ApplicationProperties;
import uz.devops.domain.TransactionParam;
import uz.devops.invoice.service.dto.*;
import uz.devops.invoice.service.dto.columns.ColumnSummaryValues;
import uz.devops.invoice.service.dto.document.Document;
import uz.devops.invoice.service.dto.head.Head;
import uz.devops.invoice.service.dto.head.HeadReceiver;
import uz.devops.invoice.service.dto.head.HeadSender;
import uz.devops.invoice.service.dto.item.Item;
import uz.devops.invoice.service.dto.item.ItemCatalog;
import uz.devops.invoice.service.dto.item.ItemExcise;
import uz.devops.invoice.service.dto.item.ItemVat;
import uz.devops.service.dto.TransactionDTO;
import uz.devops.service.faktura.DoubleAccount;
import uz.devops.service.utils.BaseUtils;

import java.sql.SQLException;
import java.util.*;

/**
 * Author: Nurislom
 * <br/>
 * Date: 03.05.2023
 * <br/>
 * Time: 11:46
 * <br/>
 * Package: uz.devops.service.faktura
 */
@Slf4j
public abstract class InvoiceBuilder {

    public static Invoice invoiceBuilder(DoubleAccount account, TransactionParam dto) throws SQLException {
        log.info("Invoice Builder Start");
        Map<String, String> params = dto.getParams();
        return Invoice.builder()
            .head(Head.builder()
                .programVersion("1.0.0")
                .formatVersion("1.0.0")
                .sender(getHeadSender(account, params))
                .receiver(getHeadReceiver(account, params))
                .build())
            .build();
    }

    public static Document documentBuilder(TransactionDTO dto) throws SQLException {
        return Document.builder()
            .contractNumber(RandomStringUtils.random(12, false, true))
            .contractDate(new Date())
            .documentVersion("v1")
            .documentNumber(RandomStringUtils.random(12, false, true))
            .documentDate(new Date())
            .facturaType("0")
            .items(new ArrayList<>(List.of(itemBuilder(dto)))) // List.of Factory Method is unmodifiable collection!
            .columnSummaryValues(ColumnSummaryValues.builder()
                .columnSubtotal(String.valueOf(dto.getRequestAmount())) // Currency Write Logic With Dollar, Rubl
                .columnVatValue("0")
                .columnVatValueUzs("0")
                .columnSubtotalWithTaxes(String.valueOf(dto.getSenderAmount()))
                .columnSubtotalWithTaxesTotal(String.valueOf(dto.getSenderAmount()))
                .build())
            .build();
    }

    public static Item itemBuilder(TransactionDTO dto) throws SQLException {
        return Item.builder()
            .description("Услуга P2P")
            .volume("1")
            .unitPrice(String.valueOf(dto.getRequestAmount()))
            .subTotal(String.valueOf(dto.getRequestAmount()))
            .subtotalWithTaxes(String.valueOf(dto.getSenderAmount()))
            .measurementUnit(BaseUtils.ccyNumToName(dto.getRequestCcy()))
            .igotaId(null)
            .excise(ItemExcise.builder()
                .exciseRate(BaseUtils.commissionUpAmount(String.valueOf(dto.getSenderAmount()),
                    dto.getSystemCommissionUpAmount())) // TODO commission
                .exciseValue(String.valueOf(dto.getSystemCommissionUpAmount()))
                .build())
            .vat(ItemVat.builder()
                .vatRate("без НДС")
                .vatValue("без НДС")
                .build())
            .catalog(ItemCatalog.builder()
                .code("10406001002000000")
                .name("Комисионные услуги по P2P-операциям")
                .build())
            .build();
    }

    private static HeadSender getHeadSender(DoubleAccount account, Map<String, String> params) {
        return HeadSender.builder()
            .senderInfo(SenderInfo.builder()
                .inn(params.get(ApplicationProperties.SENDER_INFO_INN))
                .companyName(params.get(ApplicationProperties.SENDER_INFO_COMPANY_NAME))
                .address(new Address(params.get(ApplicationProperties.SENDER_INFO_ADDRESS_REGION),
                    params.get(ApplicationProperties.SENDER_INFO_ADDRESS_STREET)))
                .bankDetails(BankDetails.builder()
                    .accountNumber(account.getSenderAcc())
                    .bankName(Objects.requireNonNullElse(params.get(
                        ApplicationProperties.SENDER_INFO_BANK_DETAILS_BANK_NAME), "null"))
                    .bankCode(Objects.requireNonNullElse(params.get(
                        ApplicationProperties.SENDER_INFO_BANK_DETAILS_BANK_CODE), "null"))
                    .build())
                .build())
            .build();
    }

    private static HeadReceiver getHeadReceiver(DoubleAccount account, Map<String, String> params) {
        return HeadReceiver.builder()
            .receiverInfo(ReceiverInfo.builder()
                .inn(params.get(ApplicationProperties.RECEIVER_INFO_INN))
                .companyName(params.get(ApplicationProperties.RECEIVER_INFO_COMPANY_NAME))
                .address(new Address(params.get(ApplicationProperties.RECEIVER_INFO_ADDRESS_REGION),
                    params.get(ApplicationProperties.RECEIVER_INFO_ADDRESS_STREET)))
                .bankDetails(BankDetails.builder()
                    .accountNumber(account.getReceiverAcc())
                    .bankName(Objects.requireNonNullElse(params.get(
                        ApplicationProperties.RECEIVER_INFO_BANK_DETAILS_BANK_NAME), "null"))
                    .bankCode(Objects.requireNonNullElse(params.get(
                        ApplicationProperties.RECEIVER_INFO_BANK_DETAILS_BANK_CODE), "null"))
                    .build())
                .build())
            .build();
    }

}
