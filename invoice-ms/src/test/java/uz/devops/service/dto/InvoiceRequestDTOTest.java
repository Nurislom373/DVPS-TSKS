package uz.devops.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import uz.devops.web.rest.TestUtil;

class InvoiceRequestDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(InvoiceRequestDTO.class);
        InvoiceRequestDTO invoiceRequestDTO1 = new InvoiceRequestDTO();
        invoiceRequestDTO1.setId(1L);
        InvoiceRequestDTO invoiceRequestDTO2 = new InvoiceRequestDTO();
        assertThat(invoiceRequestDTO1).isNotEqualTo(invoiceRequestDTO2);
        invoiceRequestDTO2.setId(invoiceRequestDTO1.getId());
        assertThat(invoiceRequestDTO1).isEqualTo(invoiceRequestDTO2);
        invoiceRequestDTO2.setId(2L);
        assertThat(invoiceRequestDTO1).isNotEqualTo(invoiceRequestDTO2);
        invoiceRequestDTO1.setId(null);
        assertThat(invoiceRequestDTO1).isNotEqualTo(invoiceRequestDTO2);
    }
}
