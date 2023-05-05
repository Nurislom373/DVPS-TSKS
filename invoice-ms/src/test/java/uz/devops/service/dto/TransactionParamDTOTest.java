package uz.devops.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import uz.devops.web.rest.TestUtil;

class TransactionParamDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TransactionParamDTO.class);
        TransactionParamDTO transactionParamDTO1 = new TransactionParamDTO();
        transactionParamDTO1.setId(1L);
        TransactionParamDTO TransactionParamDTO2 = new TransactionParamDTO();
        assertThat(transactionParamDTO1).isNotEqualTo(TransactionParamDTO2);
        TransactionParamDTO2.setId(transactionParamDTO1.getId());
        assertThat(transactionParamDTO1).isEqualTo(TransactionParamDTO2);
        TransactionParamDTO2.setId(2L);
        assertThat(transactionParamDTO1).isNotEqualTo(TransactionParamDTO2);
        transactionParamDTO1.setId(null);
        assertThat(transactionParamDTO1).isNotEqualTo(TransactionParamDTO2);
    }
}
