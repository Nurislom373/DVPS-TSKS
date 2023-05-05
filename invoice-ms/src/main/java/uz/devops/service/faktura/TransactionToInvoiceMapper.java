package uz.devops.service.faktura;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;
import uz.devops.domain.TransactionParam;
import uz.devops.invoice.service.dto.Invoice;
import uz.devops.repository.TransactionParamRepository;
import uz.devops.service.dto.TransactionDTO;
import uz.devops.service.faktura.invoice.InvoiceBuilder;
import uz.devops.service.faktura.invoice.InvoiceUpdater;
import uz.devops.service.p2pParam.CommonTransactionParamService;

import java.sql.SQLException;
import java.util.*;

/**
 * Author: Nurislom
 * <br/>
 * Date: 02.05.2023
 * <br/>
 * Time: 16:25
 * <br/>
 * Package: uz.devops.service.faktura
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionToInvoiceMapper {

    private final TransactionParamRepository paramRepository;
    private final CommonTransactionParamService commonTransactionParamService;

    public List<Invoice> transactionToInvoice(List<TransactionDTO> list) throws SQLException {
        Map<DoubleAccount, Invoice> invoiceMap = new HashMap<>();

        if (Objects.nonNull(list) && !list.isEmpty()) {
            for (TransactionDTO transactionDTO : list) {
                long id = transactionDTO.getId();
                TransactionParam dto = commonTransactionParamService.checkParamsAndGet(paramRepository.findByTransactionId(id));
                if (Objects.nonNull(dto)) {
                    log.info("Transaction required params found.");
                    DoubleAccount doubleAcc = new DoubleAccount(transactionDTO.getSenderAccount(),
                        transactionDTO.getRecipientAccount());
                    if (invoiceMap.containsKey(doubleAcc)) {
                        log.info("Match transaction found!");
                        Invoice invoice = invoiceMap.get(doubleAcc);
                        Invoice invoiceUpdate = InvoiceUpdater.invoiceUpdate(invoice, transactionDTO);
                        invoiceMap.put(doubleAcc, invoiceUpdate);
                    } else {
                        log.info("Match transaction not found!");
                        Invoice invoice = InvoiceBuilder.invoiceBuilder(doubleAcc, dto);
                        invoice.setDocument(InvoiceBuilder.documentBuilder(transactionDTO));
                        invoice.setId(String.valueOf(id));
                        setFileName(invoice);
                        log.info("Invoice Build Finished");
                        invoiceMap.put(doubleAcc, invoice);
                    }
                } else {
                    log.warn("Transaction Extra Params not found! : {}", transactionDTO);
                }
            }
        } else {
            log.error("TransactionDTO List is null!");
            return new ArrayList<>();
        }

        if (!invoiceMap.isEmpty()) {
            return new ArrayList<>(invoiceMap.values());
        } else {
            log.error("Invoice Map is null!");
            return new ArrayList<>();
        }
    }

    private void setFileName(Invoice invoice) {
        invoice.getHead().setFileName("p2p-invoice_" + invoice.getHead().getSender().getSenderInfo().getInn() + "_" +
            invoice.getHead().getReceiver().getReceiverInfo().getInn() + "_" + invoice.getDocument().getDocumentDate()
            + "_FU_" + RandomStringUtils.random(10, false, true) + ".xml");
    }


}
