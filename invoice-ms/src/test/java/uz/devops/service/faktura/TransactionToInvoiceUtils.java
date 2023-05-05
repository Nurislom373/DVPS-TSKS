package uz.devops.service.faktura;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import uz.devops.domain.TransactionParam;
import uz.devops.domain.enumeration.Status;
import uz.devops.repository.TransactionParamRepository;
import uz.devops.service.dto.TransactionDTO;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Author: Nurislom
 * <br/>
 * Date: 05.05.2023
 * <br/>
 * Time: 15:52
 * <br/>
 * Package: uz.devops.service.faktura
 */
public abstract class TransactionToInvoiceUtils {

    public static List<TransactionDTO> transactionDTOS(TransactionParamRepository paramRepository, boolean withRequiredParams,
                                                       Map<String, String> map) {
        return Stream.of(buildTransactionDTO(1L, "CF0B5739D195E695E053D30811AC38F9", "935035556",
                    116000L, 116000L, 860, 0L),
                buildTransactionDTO(2L, "CF547D708C92DCA6E053D30811AC1D44", "986060HZUSID7953",
                    2507500L, 2500000L, 860, 7500L),
                buildTransactionDTO(3L, "986008FZSZXE7491", "8600041727579956",
                    5717100L, 5700000L, 860, 17100L))
            .peek(p -> {
                if (withRequiredParams) {
                    paramRepository.save(buildTransactionParam(p.getId(), map));
                }
            }).collect(Collectors.toList());
    }

    public static List<TransactionDTO> transactionDTOS(TransactionParamRepository paramRepository, int count,
                                                       boolean withRequiredParams, Map<String, String> map, boolean duplicates) {
        return IntStream.rangeClosed(0, count)
            .mapToObj(i -> {
                if (duplicates) {
                    return doubleBuild(RandomUtils.nextBoolean(), (long) i, RandomStringUtils.random(16, false,
                            true), RandomStringUtils.random(16, false, true),
                        RandomUtils.nextLong(100, 9999999), RandomUtils.nextLong(100, 9999999), 860, 0L);
                } else {
                    return List.of(buildTransactionDTO((long) i, RandomStringUtils.random(16, false,
                            true), RandomStringUtils.random(16, false, true),
                        RandomUtils.nextLong(100, 9999999), RandomUtils.nextLong(100, 9999999), 860, 0L));
                }
            })
            .flatMap(Collection::stream).peek(p -> {
                if (withRequiredParams) {
                    if (!paramRepository.existsByTransactionId(p.getId())) {
                        paramRepository.save(buildTransactionParam(p.getId(), map));
                    }
                }
            })
            .collect(Collectors.toList());
    }

    public static List<TransactionDTO> similarTransactionDTOS(TransactionParamRepository paramRepository, int count,
                                                              boolean withRequiredParams, Map<String, String> map) {
        return IntStream.rangeClosed(0, count).mapToObj(m -> buildTransactionDTO((long) m,
                "CF0B5739D195E695E053D30811AC38F9", "935035556",
                146500L, 146000L, 860, 500L))
            .peek(p -> {
                if (withRequiredParams) {
                    paramRepository.save(buildTransactionParam(p.getId(), map));
                }
            })
            .collect(Collectors.toList());
    }

    private static List<TransactionDTO> doubleBuild(boolean doubleBuild, Long id, String senderAccount, String recipientAccount,
                                             Long senderAmount, Long requestAmount, Integer ccy, Long systemCommissionUpAmount) {
        if (doubleBuild) {
            return List.of(buildTransactionDTO(id, senderAccount, recipientAccount, senderAmount, requestAmount, ccy,
                systemCommissionUpAmount), buildTransactionDTO(id, senderAccount, recipientAccount, senderAmount,
                requestAmount, ccy, systemCommissionUpAmount));
        } else {
            return List.of(buildTransactionDTO(id, senderAccount, recipientAccount, senderAmount, requestAmount, ccy,
                systemCommissionUpAmount));
        }
    }

    private static TransactionDTO buildTransactionDTO(Long id, String senderAccount, String recipientAccount, Long senderAmount,
                                                      Long requestAmount, Integer ccy, Long systemCommissionUpAmount) {
        return TransactionDTO.builder()
            .id(id)
            .senderAccount(senderAccount)
            .recipientAccount(recipientAccount)
            .senderAmount(senderAmount)
            .requestAmount(requestAmount)
            .requestCcy(ccy)
            .systemCommissionUpAmount(systemCommissionUpAmount)
            .build();
    }

    private static TransactionParam buildTransactionParam(Long transactionId, Map<String, String> map) {
        return new TransactionParam(Status.NEW, transactionId, map);
    }

}
