package uz.devops.repository;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import uz.devops.service.dto.TransactionDTO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * Author: Nurislom
 * <br/>
 * Date: 4/29/2023
 * <br/>
 * Time: 10:01 AM
 * <br/>
 * Package: uz.devops.repository
 */
@Component
public class TransactionMapper implements RowMapper<List<TransactionDTO>> {

    @Override
    public List<TransactionDTO> mapRow(ResultSet rs, int rowNum) throws SQLException {
        List<TransactionDTO> list = new ArrayList<>();
        if (rs.next()) {
            list.add(TransactionDTO.builder()
                .id(rs.getLong("id"))
                .senderName(rs.getString("sender_name"))
                .senderAccount(rs.getString("sender_account"))
                .senderSuccess(rs.getBoolean("sender_success"))
                .senderDate(rs.getObject("sender_date", Instant.class))
                .senderAmount(rs.getLong("sender_amount"))
                .senderCcy(rs.getInt("sender_ccy"))
                .recipientName(rs.getString("recipient_name"))
                .recipientAccount(rs.getString("recipient_account"))
                .recipientStatus(rs.getString("recipient_status"))
                .recipientDate(rs.getObject("recipient_date", Instant.class))
                .recipientAmount(rs.getLong("recipient_amount"))
                .recipientCcy(rs.getInt("recipient_ccy"))
                .build());
        }
        return list;
    }
}
