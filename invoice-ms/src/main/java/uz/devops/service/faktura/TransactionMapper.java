package uz.devops.service.faktura;

import org.springframework.jdbc.core.RowMapper;
import uz.devops.service.dto.TransactionDTO;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Author: Nurislom
 * <br/>
 * Date: 03.05.2023
 * <br/>
 * Time: 16:22
 * <br/>
 * Package: uz.devops.service.faktura
 */
public class TransactionMapper implements RowMapper<TransactionDTO> {

    @Override
    public TransactionDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
        return TransactionDTO.builder()
            .id(rs.getLong("id"))
            .recipientAccount(rs.getString("recipient_account"))
            .senderAccount(rs.getString("sender_account"))
            .senderAmount(rs.getLong("sender_amount"))
            .requestAmount(rs.getLong("request_amount"))
            .requestCcy(rs.getInt("request_ccy"))
            .systemCommissionUpAmount(rs.getLong("system_commission_up_amount"))
            .build();
    }
}
