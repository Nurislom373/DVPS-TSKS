package uz.devops.service.faktura;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import uz.devops.config.ApplicationProperties;
import uz.devops.domain.TransactionParam;
import uz.devops.domain.enumeration.Status;
import uz.devops.repository.TransactionParamRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author: Nurislom
 * <br/>
 * Date: 04.05.2023
 * <br/>
 * Time: 11:10
 * <br/>
 * Package: uz.devops.service.faktura
 */
@Component
@RequiredArgsConstructor
public class CommondLineRunner implements CommandLineRunner {

    private final TransactionParamRepository paramRepository;

    @Override
    public void run(String... args) throws Exception {
//        Map<String, String> map = new HashMap<>();
//        map.put(ApplicationProperties.RECEIVER_INFO_INN, "31511945620042");
//        map.put(ApplicationProperties.RECEIVER_INFO_COMPANY_NAME, "DevopsUz");
//        map.put(ApplicationProperties.RECEIVER_INFO_ADDRESS_REGION, "TOSHKENT SHAHAR");
//        map.put(ApplicationProperties.RECEIVER_INFO_ADDRESS_STREET, "MIROBOD TUMANI, T.SHEVCHENKO KO`CHASI");
//        map.put(ApplicationProperties.RECEIVER_INFO_BANK_DETAILS_BANK_CODE, "INVEST FINANCE BANK");
//
//        map.put(ApplicationProperties.SENDER_INFO_INN, "206942764");
//        map.put(ApplicationProperties.SENDER_INFO_COMPANY_NAME, "AnorBank");
//        map.put(ApplicationProperties.SENDER_INFO_ADDRESS_REGION, "TOSHKENT SHAHAR");
//        map.put(ApplicationProperties.SENDER_INFO_ADDRESS_STREET, "MIROBOD TUMANI, T.SHEVCHENKO KO`CHASI");
//        map.put(ApplicationProperties.SENDER_INFO_BANK_DETAILS_BANK_CODE, "INVEST FINANCE BANK");
//
//        List<TransactionParam> TransactionParams = List.of(
//            new TransactionParam(Status.SUCCESS, 100203775L, map),
//            new TransactionParam(Status.SUCCESS, 100203774L, map),
//            new TransactionParam(Status.SUCCESS, 100203773L, map),
//            new TransactionParam(Status.SUCCESS, 100203772L, map),
//            new TransactionParam(Status.SUCCESS, 100203771L, map),
//            new TransactionParam(Status.SUCCESS, 100203770L, map),
//            new TransactionParam(Status.SUCCESS, 100203769L, map),
//            new TransactionParam(Status.SUCCESS, 100203768L, map),
//            new TransactionParam(Status.SUCCESS, 100019254L, map),
//            new TransactionParam(Status.SUCCESS, 100203776L, map)
//        );
//        paramRepository.saveAll(TransactionParams);
    }
}
