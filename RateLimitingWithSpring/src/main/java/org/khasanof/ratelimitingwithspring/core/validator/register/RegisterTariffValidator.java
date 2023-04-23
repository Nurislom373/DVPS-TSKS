package org.khasanof.ratelimitingwithspring.core.validator.register;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.khasanof.ratelimitingwithspring.core.common.register.classes.REGSTariff;
import org.khasanof.ratelimitingwithspring.core.common.register.classes.REGSTariffApi;
import org.khasanof.ratelimitingwithspring.core.domain.Api;
import org.khasanof.ratelimitingwithspring.core.domain.PricingTariff;
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

/**
 * Author: Nurislom
 * <br/>
 * Date: 4/23/2023
 * <br/>
 * Time: 5:30 PM
 * <br/>
 * Package: org.khasanof.ratelimitingwithspring.core.validator.register
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class RegisterTariffValidator implements BaseValidator {

    private final ApiRepository apiRepository;
    private final PricingTariffRepository pricingTariffRepository;

    public ValidatorResult validatorTariffs(String key, List<REGSTariff> tariffs) {
        ValidatorUtils.nonNull(key, tariffs);
        return new ValidatorResult().success(tariffs.stream().map(e -> validatorTariff(key, e))
                .allMatch(ValidatorResult::isSuccess));
    }

    public ValidatorResult validatorTariff(String key, REGSTariff tariff) {
        List<Api> collect = tariff.getApi().stream()
                .map(this::regsTariffApiMatchApiGet).toList();
        List<PricingTariff> allByKey = pricingTariffRepository.findAllByKey(key);
        if (!checkAlreadySellAPI(allByKey, collect)) {
            return new ValidatorResult().failed(new RuntimeException("API match found!"));
        } else {
            return new ValidatorResult().success(true);
        }
    }

    private boolean checkAlreadySellAPI(List<PricingTariff> tariffs, List<Api> apis) {
        return tariffs.stream()
                .allMatch(tariff -> checkTariffAPI(tariff, apis));
    }

    private boolean checkTariffAPI(PricingTariff tariff, List<Api> apis) {
        return apis.stream().allMatch(e -> tariff.getApis()
                .stream().noneMatch(t -> t.equals(e.getId())));
    }

    private Api regsTariffApiMatchApiGet(REGSTariffApi api) {
        if (Objects.nonNull(api.getAttributes()) && !api.getAttributes().isEmpty()) {
            return apiRepository.findByUrlAndMethod(api.getUrl(), api.getMethod())
                    .stream().filter(f -> BaseUtils.areEqual(f.getAttributes(), api.getAttributes()))
                    .findFirst().orElseThrow(() -> new RuntimeException("API match not found"));
        } else {
            return apiRepository.findApiByUrlAndMethod(api.getUrl(), api.getMethod())
                    .orElseThrow(() -> new RuntimeException("API match not found"));
        }
    }

}
