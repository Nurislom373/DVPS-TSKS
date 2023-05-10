package org.khasanof.ratelimitingwithspring.core.validator.tariff;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.khasanof.ratelimitingwithspring.core.exceptions.InvalidValidationException;
import org.khasanof.ratelimitingwithspring.core.strategy.tariff.classes.RSTariff;
import org.khasanof.ratelimitingwithspring.core.validator.BaseValidator;
import org.khasanof.ratelimitingwithspring.core.validator.ValidatorResult;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Author: Nurislom
 * <br/>
 * Date: 4/24/2023
 * <br/>
 * Time: 11:50 PM
 * <br/>
 * Package: org.khasanof.ratelimitingwithspring.core.validator.tariff
 */
@Slf4j
@Service
public class TariffSaveValidator implements BaseValidator {

    public ValidatorResult validatorRSTariff(List<RSTariff> list) {
        return new ValidatorResult().success(list.stream().map(this::validatorRSTariff)
                .allMatch(ValidatorResult::isSuccess));
    }

    public ValidatorResult validatorRSTariff(RSTariff tariff) {
        if (tariff.getTimeCount() < 1) {
            log.error("RSTariff field => timeCount is less than one! (Object) : {}", tariff);
            return new ValidatorResult().failed("RSTariff field => timeCount is less than one!");
        } else if (tariff.getRequestCount() < 1) {
            log.error("RSTariff field => requestCount is less than one! (Object) : {}", tariff);
            return new ValidatorResult().failed("RSTariff field => requestCount is less than one!");
        } else if (StringUtils.isAlpha(tariff.getName()) && StringUtils.isAllUpperCase(tariff.getName())) {
            log.error("RSTariff field => name must contain only letters and upperCase! (Object) : {}", tariff);
            return new ValidatorResult().failed("RSTariff field => name must contain only letters and upperCase!");
        }
        return new ValidatorResult().success(true);
    }

}
