package org.khasanof.ratelimitingwithspring.core.validator.limit.save;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.khasanof.ratelimitingwithspring.core.strategy.limit.classes.RSLimit;
import org.khasanof.ratelimitingwithspring.core.validator.BaseValidator;
import org.khasanof.ratelimitingwithspring.core.validator.ValidatorResult;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * Author: Nurislom
 * <br/>
 * Date: 4/23/2023
 * <br/>
 * Time: 5:38 PM
 * <br/>
 * Package: org.khasanof.ratelimitingwithspring.core.validator.limit.save
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class LimitSaveValidator implements BaseValidator {

    public ValidatorResult validatorRSLimits(List<RSLimit> limits) {
        Set<RSLimit> rsLimits = new HashSet<>(limits);
        return new ValidatorResult().success(Objects.equals(rsLimits.size(),
                limits.size()));
    }



}
