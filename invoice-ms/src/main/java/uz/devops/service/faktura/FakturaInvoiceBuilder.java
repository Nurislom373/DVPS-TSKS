package uz.devops.service.faktura;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Service;
import uz.devops.invoice.service.dto.Invoice;
import uz.devops.service.dto.TransactionDTO;

import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

/**
 * Author: Nurislom
 * <br/>
 * Date: 02.05.2023
 * <br/>
 * Time: 16:08
 * <br/>
 * Package: uz.devops.service.faktura
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class FakturaInvoiceBuilder {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final TransactionToInvoiceMapper invoiceRowMapper;

    public List<Invoice> invoiceListBuilder() throws SQLException {
        LocalDateTime start = LocalDate.now().minus(1, ChronoUnit.MONTHS).atStartOfDay();
        LocalDateTime end = LocalDate.now().atStartOfDay();
        log.info("transaction search interval start : {}, end : {}", start, end);

        SqlParameterSource parameterSource = new MapSqlParameterSource()
            .addValue("end", end)
            .addValue("start", start);

        List<TransactionDTO> query = jdbcTemplate.query("select * from transaction t where (t.sender_success = true " +
            "and t.recipient_success = true) and (t.pay_type = 'P2P' and t.created_date between :start and :end)",
            parameterSource, new TransactionMapper());

        log.info("Found P2P Transaction count : {}", query.size());
        query.forEach(System.out::println);

        return invoiceRowMapper.transactionToInvoice(query);
    }
}
