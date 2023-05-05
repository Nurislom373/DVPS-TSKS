package uz.devops.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TransactionParamMapperTest {

    private TransactionParamMapper TransactionParamMapper;

    @BeforeEach
    public void setUp() {
        TransactionParamMapper = new TransactionParamMapperImpl();
    }
}
