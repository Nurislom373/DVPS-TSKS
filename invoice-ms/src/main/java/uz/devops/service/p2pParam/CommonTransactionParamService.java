package uz.devops.service.p2pParam;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uz.devops.config.ApplicationProperties;
import uz.devops.domain.TransactionParam;

import java.util.Optional;

/**
 * Author: Nurislom
 * <br/>
 * Date: 02.05.2023
 * <br/>
 * Time: 16:43
 * <br/>
 * Package: uz.devops.service.TransactionParam
 */
@Slf4j
@Service
public class CommonTransactionParamService {

    public TransactionParam checkParamsAndGet(Optional<TransactionParam> optional) {
        log.info("checkParamsAndGet method is start");
        if (optional.isPresent()) {
            log.info("TransactionParam is present");
            TransactionParam transactionParamDTO = optional.get();
            if (checkParams(transactionParamDTO)) {
                log.info("checked all the parameters to need");
                return transactionParamDTO;
            } else {
                log.warn("failed the required parameters check!");
                return null;
            }
        }
        log.warn("TransactionParam isn't present!");
        return null;
    }

    private boolean checkParams(TransactionParam transactionParamDTO) {
        return ApplicationProperties.JSON_REQUIRED_FIELDS.stream()
            .allMatch(var -> transactionParamDTO.getParams().containsKey(var));
    }

}
