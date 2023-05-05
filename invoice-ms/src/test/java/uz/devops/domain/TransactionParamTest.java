package uz.devops.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import uz.devops.web.rest.TestUtil;

class TransactionParamTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TransactionParam.class);
        TransactionParam transactionParam1 = new TransactionParam();
        transactionParam1.setId(1L);
        TransactionParam TransactionParam2 = new TransactionParam();
        TransactionParam2.setId(transactionParam1.getId());
        assertThat(transactionParam1).isEqualTo(TransactionParam2);
        TransactionParam2.setId(2L);
        assertThat(transactionParam1).isNotEqualTo(TransactionParam2);
        transactionParam1.setId(null);
        assertThat(transactionParam1).isNotEqualTo(TransactionParam2);
    }
}
