package uz.devops.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

/**
 * Author: Nurislom
 * <br/>
 * Date: 4/29/2023
 * <br/>
 * Time: 9:53 AM
 * <br/>
 * Package: uz.devops.repository
 */
@Repository
public class TransactionJdbcTemplate {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    public TransactionJdbcTemplate(DataSource dataSource) {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }


}
