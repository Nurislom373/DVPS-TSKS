package uz.devops.service.faktura;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import uz.devops.config.ApplicationProperties;
import uz.devops.config.TestSecurityConfiguration;
import uz.devops.domain.TransactionParam;
import uz.devops.domain.enumeration.Status;
import uz.devops.invoice.service.dto.Invoice;
import uz.devops.repository.TransactionParamRepository;
import uz.devops.service.dto.TransactionDTO;
import uz.devops.service.p2pParam.CommonTransactionParamService;

import java.sql.SQLException;
import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Author: Nurislom
 * <br/>
 * Date: 04.05.2023
 * <br/>
 * Time: 16:27
 * <br/>
 * Package: uz.devops.service.faktura
 */
@SpringBootTest(classes = {TestSecurityConfiguration.class})
public class TransactionToInvoiceMapperMockTest {

    @Autowired
    private TransactionParamRepository paramRepository;

    @Autowired
    private CommonTransactionParamService commonTransactionParamService;

    @Autowired
    private TransactionToInvoiceMapper transactionToInvoiceMapper;

    static Map<String, String> map = new HashMap<>();

    @BeforeAll
    static void beforeAll() {
        map.put(ApplicationProperties.RECEIVER_INFO_INN, "31511945620042");
        map.put(ApplicationProperties.RECEIVER_INFO_COMPANY_NAME, "DevopsUz");
        map.put(ApplicationProperties.RECEIVER_INFO_ADDRESS_REGION, "TOSHKENT SHAHAR");
        map.put(ApplicationProperties.RECEIVER_INFO_ADDRESS_STREET, "MIROBOD TUMANI, T.SHEVCHENKO KO`CHASI");
        map.put(ApplicationProperties.RECEIVER_INFO_BANK_DETAILS_BANK_CODE, "INVEST FINANCE BANK");

        map.put(ApplicationProperties.SENDER_INFO_INN, "206942764");
        map.put(ApplicationProperties.SENDER_INFO_COMPANY_NAME, "AnorBank");
        map.put(ApplicationProperties.SENDER_INFO_ADDRESS_REGION, "TOSHKENT SHAHAR");
        map.put(ApplicationProperties.SENDER_INFO_ADDRESS_STREET, "MIROBOD TUMANI, T.SHEVCHENKO KO`CHASI");
        map.put(ApplicationProperties.SENDER_INFO_BANK_DETAILS_BANK_CODE, "INVEST FINANCE BANK");
    }

    @Test
    void transactionToInvoiceSimpleTest() throws SQLException {
        List<TransactionDTO> transactionDTOS = transactionDTOS();

        TransactionParamRepository transactionParamRepository = Mockito.mock(TransactionParamRepository.class);
        CommonTransactionParamService commonTransactionParamService = Mockito.mock(CommonTransactionParamService.class);
        TransactionToInvoiceMapper toInvoiceMapper = new TransactionToInvoiceMapper(transactionParamRepository,
            commonTransactionParamService);

        Mockito.when(transactionParamRepository.findByTransactionId(ArgumentMatchers.any()))
            .thenReturn(Optional.of(buildTransactionParam()));

        Mockito.when(commonTransactionParamService.checkParamsAndGet(ArgumentMatchers.any()))
            .thenReturn(buildTransactionParam());

        List<Invoice> invoices = toInvoiceMapper.transactionToInvoice(transactionDTOS);

        assertAll(
            () -> {
                assertEquals(invoices.size(), transactionDTOS.size());

                Mockito.verify(transactionParamRepository, Mockito.times(3))
                    .findByTransactionId(ArgumentMatchers.any());

                Mockito.verify(commonTransactionParamService, Mockito.times(3))
                    .checkParamsAndGet(ArgumentMatchers.any());
            },
            () -> {
                assertAll(() -> {
                    assertTrue(invoices.stream().allMatch(all -> {
                        return transactionDTOS.stream().anyMatch(any -> all.getHead().getSender().getSenderInfo()
                            .getBankDetails().getAccountNumber()
                            .equals(any.getSenderAccount()));
                    }));
                    assertTrue(invoices.stream().allMatch(all -> {
                        return transactionDTOS.stream().anyMatch(any -> all.getHead().getReceiver().getReceiverInfo()
                            .getBankDetails().getAccountNumber()
                            .equals(any.getRecipientAccount()));
                    }));
                });
                assertAll(() -> {
                    assertEquals(
                        transactionDTOS.stream().mapToLong(TransactionDTO::getSenderAmount).sum(),
                        invoices.stream().mapToLong(in -> Long.parseLong(in.getDocument().getColumnSummaryValues()
                            .getColumnSubtotalWithTaxes())).sum()
                    );
                    assertEquals(
                        transactionDTOS.stream().mapToLong(TransactionDTO::getRequestAmount).sum(),
                        invoices.stream().mapToLong(in -> Long.parseLong(in.getDocument().getColumnSummaryValues()
                            .getColumnSubtotal())).sum()
                    );
                });
            }
        );
    }

    @Test
    void transactionsListIsNullTest() throws SQLException {
        TransactionParamRepository transactionParamRepository = Mockito.mock(TransactionParamRepository.class);
        CommonTransactionParamService commonTransactionParamService = Mockito.mock(CommonTransactionParamService.class);
        TransactionToInvoiceMapper toInvoiceMapper = new TransactionToInvoiceMapper(transactionParamRepository,
            commonTransactionParamService);

        Stream<List<TransactionDTO>> stream = Stream.<List<TransactionDTO>>builder()
            .add(new ArrayList<>())
            .add(null)
            .add(new LinkedList<>())
            .build();

        List<Invoice> list = stream.map(m -> {
            try {
                return toInvoiceMapper.transactionToInvoice(m);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }).flatMap(Collection::stream).collect(Collectors.toList());

        assertAll(
            () -> {
                assertTrue(list.isEmpty());

                Mockito.verify(transactionParamRepository, Mockito.never())
                    .findByTransactionId(ArgumentMatchers.any());

                Mockito.verify(commonTransactionParamService, Mockito.never())
                    .checkParamsAndGet(ArgumentMatchers.any());
            }
        );
    }

    @Test
    void invoiceMapIsNullTest() throws SQLException {
        List<TransactionDTO> transactionDTOS = transactionDTOS();

        TransactionParamRepository transactionParamRepository = Mockito.mock(TransactionParamRepository.class);
        TransactionToInvoiceMapper toInvoiceMapper = new TransactionToInvoiceMapper(transactionParamRepository,
            commonTransactionParamService);

        Mockito.when(transactionParamRepository.findByTransactionId(ArgumentMatchers.any()))
            .thenReturn(Optional.empty());

        List<Invoice> invoices = toInvoiceMapper.transactionToInvoice(transactionDTOS);

        assertAll(
            () -> {
                assertTrue(invoices.isEmpty());
                assertEquals(transactionDTOS.size(), 3);

                Mockito.verify(transactionParamRepository, Mockito.times(3))
                    .findByTransactionId(ArgumentMatchers.any());
            }
        );
    }

    @Test
    void onlyMatchTransactionFoundTest() throws SQLException {
        List<TransactionDTO> transactionDTOS = TransactionToInvoiceUtils.similarTransactionDTOS(paramRepository,
            20, true, map);

        TransactionToInvoiceMapper toInvoiceMapper = new TransactionToInvoiceMapper(paramRepository,
            commonTransactionParamService);

        List<Invoice> invoices = toInvoiceMapper.transactionToInvoice(transactionDTOS);

        assertAll(
            () -> {
                assertFalse(invoices.isEmpty());
                assertEquals(invoices.size(), 1);
                assertEquals(transactionDTOS.size(), invoices.get(0).getDocument()
                    .getItems().size());
            },
            () -> {
                assertAll(
                    () -> {
                        assertEquals(
                            transactionDTOS.stream().mapToLong(TransactionDTO::getRequestAmount).sum(),
                            Long.parseLong(invoices.get(0).getDocument().getColumnSummaryValues().getColumnSubtotal())
                        );
                        assertEquals(
                            transactionDTOS.stream().mapToLong(TransactionDTO::getSenderAmount).sum(),
                            Long.parseLong(invoices.get(0).getDocument().getColumnSummaryValues()
                                .getColumnSubtotalWithTaxes())
                        );
                        assertEquals(
                            transactionDTOS.stream().mapToLong(TransactionDTO::getSenderAmount).sum(),
                            Long.parseLong(invoices.get(0).getDocument().getColumnSummaryValues()
                                .getColumnSubtotalWithTaxesTotal())
                        );
                    }
                );
            }
        );
    }

    @Test
    void similarTransactionAndNotSimilarTransactionTest() throws SQLException {
        List<TransactionDTO> transactionDTOS = TransactionToInvoiceUtils.transactionDTOS(paramRepository,
            5000, true, map, true);

        TransactionToInvoiceMapper toInvoiceMapper = new TransactionToInvoiceMapper(paramRepository,
            commonTransactionParamService);

        List<Invoice> invoices = toInvoiceMapper.transactionToInvoice(transactionDTOS);

        assertAll(
            () -> {
                assertFalse(transactionDTOS.isEmpty());
                assertFalse(invoices.isEmpty());
                assertNotEquals(transactionDTOS.size(), invoices.size());
            },
            () -> {
                assertEquals(
                    transactionDTOS.stream().mapToLong(TransactionDTO::getRequestAmount).sum(),
                    invoices.stream().mapToLong(m -> Long.parseLong(m.getDocument().getColumnSummaryValues()
                        .getColumnSubtotal())).sum()
                );
                assertEquals(
                    transactionDTOS.stream().mapToLong(TransactionDTO::getSenderAmount).sum(),
                    invoices.stream().mapToLong(m -> Long.parseLong(m.getDocument().getColumnSummaryValues()
                        .getColumnSubtotalWithTaxes())).sum()
                );
            },
            () -> {
                assertEquals(
                    transactionDTOS.size(),
                    invoices.stream().mapToInt(m -> m.getDocument()
                        .getItems().size()).sum()
                );
                assertTimeout(Duration.ofSeconds(15), () -> {
                    toInvoiceMapper.transactionToInvoice(transactionDTOS);
                });
            }
        );
    }

    @AfterEach
    void afterEach() {
        paramRepository.deleteAll();;
    }

    private TransactionParam buildTransactionParam() {
        return new TransactionParam(Status.NEW, 0L, map);
    }

    private List<TransactionDTO> transactionDTOS() {
        return List.of(
            buildTransactionDTO(100203775L, "CF0B5739D195E695E053D30811AC38F9", "935035556",
                116000L, 116000L, 860, 0L),
            buildTransactionDTO(100203774L, "CF547D708C92DCA6E053D30811AC1D44", "986060HZUSID7953",
                2507500L, 2500000L, 860, 7500L),
            buildTransactionDTO(100203772L, "986008FZSZXE7491", "8600041727579956",
                5717100L, 5700000L, 860, 17100L)
        );
    }

    private List<TransactionDTO> similarTransactionDTOS() {
        return List.of(
            buildTransactionDTO(100203775L, "CF0B5739D195E695E053D30811AC38F9", "935035556",
                146500L, 146000L, 860, 500L),
            buildTransactionDTO(100203776L, "CF0B5739D195E695E053D30811AC38F9", "935035556",
                126000L, 126000L, 860, 6000L),
            buildTransactionDTO(100203777L, "CF0B5739D195E695E053D30811AC38F9", "935035556",
                76000L, 76000L, 860, 0L),
            buildTransactionDTO(100203778L, "CF0B5739D195E695E053D30811AC38F9", "935035556",
                17656200L, 17656000L, 860, 6200L),
            buildTransactionDTO(100203779L, "CF0B5739D195E695E053D30811AC38F9", "935035556",
                116500L, 116500L, 860, 6500L),
            buildTransactionDTO(100203780L, "CF0B5739D195E695E053D30811AC38F9", "935035556",
                7698900L, 7698000L, 860, 900L)
        );
    }

    private TransactionDTO buildTransactionDTO(Long id, String senderAccount, String recipientAccount, Long senderAmount,
                                               Long requestAmount, Integer ccy, Long systemCommissionUpAmount) {
        TransactionDTO build = TransactionDTO.builder()
            .id(id)
            .senderAccount(senderAccount)
            .recipientAccount(recipientAccount)
            .senderAmount(senderAmount)
            .requestAmount(requestAmount)
            .requestCcy(ccy)
            .systemCommissionUpAmount(systemCommissionUpAmount)
            .build();
        paramRepository.save(buildTransactionParam(build.getId(), map));
        return build;
    }

    private TransactionParam buildTransactionParam(Long transactionId, Map<String, String> map) {
        return new TransactionParam(Status.NEW, transactionId, map);
    }

}
