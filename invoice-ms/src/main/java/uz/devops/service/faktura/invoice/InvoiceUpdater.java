package uz.devops.service.faktura.invoice;


import io.micrometer.core.ipc.http.OkHttpSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.commons.httpclient.OkHttpClientFactory;
import org.springframework.web.client.RestTemplate;
import uz.devops.invoice.service.dto.Invoice;
import uz.devops.invoice.service.dto.columns.ColumnSummaryValues;
import uz.devops.invoice.service.dto.document.Document;
import uz.devops.invoice.service.dto.item.Item;
import uz.devops.service.dto.TransactionDTO;
import uz.devops.service.utils.BaseUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Author: Nurislom
 * <br/>
 * Date: 03.05.2023
 * <br/>
 * Time: 11:58
 * <br/>
 * Package: uz.devops.service.faktura.invoice
 */
@Slf4j
public abstract class InvoiceUpdater {

    public static Invoice invoiceUpdate(Invoice invoice, TransactionDTO dto) throws SQLException {
        log.info("Invoice Update Start");
        Document document = invoice.getDocument();
        List<Item> items = document.getItems();
        Item item = InvoiceBuilder.itemBuilder(dto);
        items.add(item);
        document.setItems(items);

        ColumnSummaryValues columnSummaryValues = document.getColumnSummaryValues();
        String newSubtotalColumn = BaseUtils.addTwoString(columnSummaryValues.getColumnSubtotal(), item.getSubTotal());
        String newSubtotalColumnWithTaxes = BaseUtils.addTwoString(columnSummaryValues.getColumnSubtotalWithTaxes(),
            item.getSubtotalWithTaxes());

        setValues(columnSummaryValues, newSubtotalColumn, newSubtotalColumnWithTaxes);
        document.setColumnSummaryValues(columnSummaryValues);
        invoice.setDocument(document);
        log.warn("Invoice Update End");
        return invoice;
    }

    private static void setValues(ColumnSummaryValues values, String columnSubtotal, String columnSubtotalWithTaxes) {
        values.setColumnSubtotal(columnSubtotal);
        values.setColumnVatValue("0");
        values.setColumnSubtotalWithTaxes(columnSubtotalWithTaxes);
        values.setColumnSubtotalWithTaxesTotal(columnSubtotalWithTaxes);
    }

}
