package org.khasanof.repository.rowmapper;

import io.r2dbc.spi.Row;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.function.BiFunction;
import org.khasanof.domain.transaction.Transaction;
import org.khasanof.enums.Status;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Transaction}, with proper type conversions.
 */
@Service
public class TransactionRowMapper implements BiFunction<Row, String, Transaction> {

    private final ColumnConverter converter;

    public TransactionRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Transaction} stored in the database.
     */
    @Override
    public Transaction apply(Row row, String prefix) {
        Transaction entity = new Transaction();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setAmount(converter.fromRow(row, prefix + "_amount", BigDecimal.class));
        entity.setStatus(converter.fromRow(row, prefix + "_status", Status.class));
        entity.setFromCard(converter.fromRow(row, prefix + "_from_card", String.class));
        entity.setToCard(converter.fromRow(row, prefix + "_to_card", String.class));
        entity.setCreatedAt(converter.fromRow(row, prefix + "_created_at", LocalDateTime.class));
        entity.setUpdatedAt(converter.fromRow(row, prefix + "_updated_at", LocalDateTime.class));
        return entity;
    }
}
