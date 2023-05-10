package org.khasanof.ratelimitingwithspring.core.validator.register;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.khasanof.ratelimitingwithspring.core.common.register.classes.REGSLimit;
import org.khasanof.ratelimitingwithspring.core.common.register.classes.REGSTariff;
import org.khasanof.ratelimitingwithspring.core.common.register.classes.REGSTariffApi;
import org.khasanof.ratelimitingwithspring.core.domain.Api;
import org.khasanof.ratelimitingwithspring.core.domain.PricingTariff;
import org.khasanof.ratelimitingwithspring.core.exceptions.AlreadyRegisteredException;
import org.khasanof.ratelimitingwithspring.core.exceptions.NotFoundException;
import org.khasanof.ratelimitingwithspring.core.exceptions.NotRegisteredException;
import org.khasanof.ratelimitingwithspring.core.exceptions.properties.ExceptionProperties;
import org.khasanof.ratelimitingwithspring.core.repository.ApiRepository;
import org.khasanof.ratelimitingwithspring.core.repository.PricingApiRepository;
import org.khasanof.ratelimitingwithspring.core.repository.PricingTariffRepository;
import org.khasanof.ratelimitingwithspring.core.utils.BaseUtils;
import org.khasanof.ratelimitingwithspring.core.validator.BaseValidator;
import org.khasanof.ratelimitingwithspring.core.validator.ValidatorResult;
import org.khasanof.ratelimitingwithspring.core.validator.ValidatorUtils;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Author: Nurislom
 * <br/>
 * Date: 4/23/2023
 * <br/>
 * Time: 1:04 AM
 * <br/>
 * Package: org.khasanof.ratelimitingwithspring.core.common.register.validator
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class RegisterLimitsValidator implements BaseValidator {

    private final ApiRepository apiRepository;
    private final PricingApiRepository pricingApiRepository;

    public ValidatorResult validatorLimits(String key, List<REGSLimit> limits) {
        ValidatorUtils.nonNull(key, limits);
        return new ValidatorResult().success(limits.stream().map(e -> validatorLimit(key, e))
                .allMatch(ValidatorResult::isSuccess));
    }

    private ValidatorResult validatorLimit(String key, REGSLimit limit) {
        Optional<Api> optional;
        if (Objects.nonNull(limit.getAttributes()) && !limit.getAttributes().isEmpty()) {
            optional = apiRepository.findByUrlAndMethod(limit.getUrl(), limit.getMethod())
                    .stream().filter(f -> BaseUtils.areEqual(f.getAttributes(), limit.getAttributes()))
                    .findFirst();
        } else {
            optional = apiRepository.findApiByUrlAndMethod(limit.getUrl(), limit.getMethod());
        }
        if (optional.isPresent()) {
            Boolean exists = pricingApiRepository.existsByKeyAndLimited_Api(key, optional.get());
            if (exists) {
                return new ValidatorResult().failed("Already Get this API");
            } else {
                return new ValidatorResult().success(true);
            }
        } else {
            return new ValidatorResult().failed("API not found");
        }
    }


}
