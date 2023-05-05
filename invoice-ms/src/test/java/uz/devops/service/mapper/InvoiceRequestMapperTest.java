package uz.devops.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class InvoiceRequestMapperTest {

    private InvoiceRequestMapper invoiceRequestMapper;

    @BeforeEach
    public void setUp() {
        invoiceRequestMapper = new InvoiceRequestMapperImpl();
    }
}
