package org.khasanof.ratelimitingwithspring.core.common;

import org.khasanof.ratelimitingwithspring.core.AbstractCommonLimitsService;
import org.khasanof.ratelimitingwithspring.core.RateLimiting;
import org.khasanof.ratelimitingwithspring.core.common.load.genericLoad.RSLimitLoadPostConstruct;
import org.khasanof.ratelimitingwithspring.core.common.read.CommonReadConfigAndSave;
import org.khasanof.ratelimitingwithspring.core.common.register.CommonRegisterLimits;
import org.khasanof.ratelimitingwithspring.core.common.register.classes.REGSLimit;
import org.khasanof.ratelimitingwithspring.core.common.register.classes.REGSTariff;
import org.khasanof.ratelimitingwithspring.core.common.search.RateLimitingSearch;
import org.khasanof.ratelimitingwithspring.core.common.search.classes.RLSearch;
import org.khasanof.ratelimitingwithspring.core.config.ReadLimitsPropertiesConfig;
import org.khasanof.ratelimitingwithspring.core.exceptions.AlreadyRegisteredException;
import org.khasanof.ratelimitingwithspring.core.strategy.limit.classes.RSLimit;
import org.khasanof.ratelimitingwithspring.core.validator.ValidatorResult;
import org.khasanof.ratelimitingwithspring.core.validator.register.RegisterLTValidator;
import org.khasanof.ratelimitingwithspring.core.validator.register.RegisterLimitsValidator;
import org.khasanof.ratelimitingwithspring.core.validator.register.RegisterTariffValidator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Author: Nurislom
 * <br/>
 * Date: 4/14/2023
 * <br/>
 * Time: 2:58 PM
 * <br/>
 * Package: org.khasanof.ratelimitingwithspring.core
 */
@Service
public class CommonLimitsService extends AbstractCommonLimitsService {

    private final CommonRegisterLimits registerLimits;
    private final RateLimitingSearch rateLimitingSearch;
    private final RSLimitLoadPostConstruct rsLimitLoadPostConstruct;

    public CommonLimitsService(ReadLimitsPropertiesConfig propertiesConfig, CommonReadConfigAndSave readConfigAndSave,
                               RegisterLimitsValidator registerLimitsValidator, RegisterTariffValidator registerTariffValidator,
                               CommonRegisterLimits registerLimits, @Qualifier("rateLimitingSearchRedisCache") RateLimitingSearch rateLimitingSearch,
                               RSLimitLoadPostConstruct rsLimitLoadPostConstruct, RegisterLTValidator validator) {
        super(propertiesConfig, readConfigAndSave, registerLimitsValidator, registerTariffValidator, validator);
        this.registerLimits = registerLimits;
        this.rateLimitingSearch = rateLimitingSearch;
        this.rsLimitLoadPostConstruct = rsLimitLoadPostConstruct;
    }

    public void registrationOfLimits(String key, List<REGSLimit> limits) {
        ValidatorResult result = validator.validatorLimit(key, limits);
        if (result.isSuccess()) {
            registerLimits.registrationOfLimits(key, limits);
        } else {
            throw new RuntimeException(result.getMessage());
        }
    }

    public void registrationOfTariffs(String key, List<REGSTariff> tariffs) {
        ValidatorResult result = validator.validatorTariff(key, tariffs);
        if (result.isSuccess()) {
            registerLimits.registrationOfTariffs(key, tariffs);
        } else {
            throw new AlreadyRegisteredException("one of the APIs you want to register is already registered.");
        }
    }

    // rewrite search
    public RateLimiting searchKeys(String key, String url, String method, Map<String, String> attributes) {
        return rateLimitingSearch.searchKeys(new RLSearch(key, url, method, attributes));
    }

    public List<RSLimit> getLimits() {
        return rsLimitLoadPostConstruct.getList();
    }
}
