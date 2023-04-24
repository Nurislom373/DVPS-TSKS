package org.khasanof.ratelimitingwithspring.core.validator.register;

import lombok.RequiredArgsConstructor;
import org.khasanof.ratelimitingwithspring.core.common.register.classes.REGSLimit;
import org.khasanof.ratelimitingwithspring.core.common.register.classes.REGSTariff;
import org.khasanof.ratelimitingwithspring.core.common.register.classes.REGSTariffApi;
import org.khasanof.ratelimitingwithspring.core.validator.ValidatorResult;
import org.khasanof.ratelimitingwithspring.core.validator.ValidatorUtils;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Author: Nurislom
 * <br/>
 * Date: 4/24/2023
 * <br/>
 * Time: 10:20 PM
 * <br/>
 * Package: org.khasanof.ratelimitingwithspring.core.validator.register
 */
@Component
@RequiredArgsConstructor
public class RegisterLTValidator {

    private final RegisterLimitsValidator registerLimitsValidator;
    private final RegisterTariffValidator registerTariffValidator;

    public ValidatorResult validatorLimit(String key, List<REGSLimit> limits) {
        ValidatorUtils.nonNull(key, limits);
        return new ValidatorResult().success(Stream.of(registerLimitsValidator.validatorLimits(key, limits),
                        registerTariffValidator.validatorTariffs(key, limitsToTariffs(limits)))
                .allMatch(ValidatorResult::isSuccess));
    }

    public ValidatorResult validatorTariff(String key, List<REGSTariff> tariffs) {
        ValidatorUtils.nonNull(key, tariffs);
        return new ValidatorResult().success(Stream.of(registerTariffValidator.validatorTariffs(key, tariffs),
                        registerLimitsValidator.validatorLimits(key, tariffsToLimits(tariffs)))
                .allMatch(ValidatorResult::isSuccess));
    }

    private List<REGSLimit> tariffsToLimits(List<REGSTariff> tariffs) {
        return tariffs.stream().map(this::tariffToLimits)
                .flatMap(Collection::stream).toList();
    }

    private List<REGSLimit> tariffToLimits(REGSTariff tariff) {
        return tariff.getApi().stream().map(e -> new REGSLimit(e.getUrl(), e.getMethod(),
                e.getAttributes(), "VALID#&#", 0L)).toList();
    }

    private List<REGSTariff> limitsToTariffs(List<REGSLimit> limits) {
        return List.of(new REGSTariff("VALID#&#", limitsToRegsTariffApi(limits), 0L));
    }

    private List<REGSTariffApi> limitsToRegsTariffApi(List<REGSLimit> limits) {
        return limits.stream().map(m -> new REGSTariffApi(m.getUrl(),
                m.getMethod(), m.getAttributes())).collect(Collectors.toList());
    }

}
