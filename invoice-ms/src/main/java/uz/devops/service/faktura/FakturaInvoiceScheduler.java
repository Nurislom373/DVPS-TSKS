package uz.devops.service.faktura;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import uz.devops.config.ApplicationProperties;
import uz.devops.config.InvoiceSchedulerProperties;
import uz.devops.invoice.service.InvoiceService;
import uz.devops.invoice.service.dto.*;
import uz.devops.invoice.service.dto.result.InvoiceResult;
import uz.devops.service.utils.BaseUtils;

import java.sql.SQLException;
import java.util.List;

/**
 * Author: Nurislom
 * <br/>
 * Date: 03.05.2023
 * <br/>
 * Time: 15:37
 * <br/>
 * Package: uz.devops.service.faktura
 */
@Slf4j
@Service
@EnableScheduling
@RequiredArgsConstructor
public class FakturaInvoiceScheduler {

    private final FakturaInvoiceBuilder invoiceBuilder;
    private final InvoiceService invoiceService;

    @Scheduled(cron = ApplicationProperties.MONTHLY_INVOICE_SCHEDULE_CRONE_PATTERN)
    public void scheduler() throws SQLException {
        List<Invoice> invoices = invoiceBuilder.invoiceListBuilder();
        log.info("Build Invoice Size : {}", invoices.size());
        if (!invoices.isEmpty()) {
            CommonResultData<TokenResponseDTO> token = invoiceService.getToken(BaseUtils.tokenRequestDTO());
            log.info("Successfully Get Token : {}", token);
            if (token.getSuccess()) {
                CommonResultData<InvoiceResult> importDocument = invoiceService.asyncImportDocument(new CommonInvoiceRequestDTO(new InvoiceRequestDTO(invoices),
                    token.getData().getAccessToken()));
                System.out.println("importDocument = " + importDocument);
            }
        } else {
            log.error("build invoice List is null!");
        }
    }

}
