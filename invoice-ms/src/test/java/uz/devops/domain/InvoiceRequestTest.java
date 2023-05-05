package uz.devops.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import uz.devops.web.rest.TestUtil;

class InvoiceRequestTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(InvoiceRequest.class);
        InvoiceRequest invoiceRequest1 = new InvoiceRequest();
        invoiceRequest1.setId(1L);
        InvoiceRequest invoiceRequest2 = new InvoiceRequest();
        invoiceRequest2.setId(invoiceRequest1.getId());
        assertThat(invoiceRequest1).isEqualTo(invoiceRequest2);
        invoiceRequest2.setId(2L);
        assertThat(invoiceRequest1).isNotEqualTo(invoiceRequest2);
        invoiceRequest1.setId(null);
        assertThat(invoiceRequest1).isNotEqualTo(invoiceRequest2);
    }
}
