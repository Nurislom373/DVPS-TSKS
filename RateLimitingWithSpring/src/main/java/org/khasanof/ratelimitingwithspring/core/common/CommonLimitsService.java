package org.khasanof.ratelimitingwithspring.core.common;

import org.khasanof.ratelimitingwithspring.core.AbstractCommonLimitsService;
import org.khasanof.ratelimitingwithspring.core.common.load.genericLoad.RSLimitLoadPostConstruct;
import org.khasanof.ratelimitingwithspring.core.common.read.CommonReadConfigAndSave;
import org.khasanof.ratelimitingwithspring.core.common.register.CommonRegisterLimits;
import org.khasanof.ratelimitingwithspring.core.common.register.classes.REGSLimit;
import org.khasanof.ratelimitingwithspring.core.common.register.classes.REGSTariff;
import org.khasanof.ratelimitingwithspring.core.common.search.RateLimitingSearchKeys;
import org.khasanof.ratelimitingwithspring.core.common.search.classes.RLSearch;
import org.khasanof.ratelimitingwithspring.core.config.ReadLimitsPropertiesConfig;
import org.khasanof.ratelimitingwithspring.core.RateLimiting;
import org.khasanof.ratelimitingwithspring.core.strategy.limit.classes.RSLimit;
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
    private final RateLimitingSearchKeys limitingSearchKeys;
    private final RSLimitLoadPostConstruct rsLimitLoadPostConstruct;

    public CommonLimitsService(ReadLimitsPropertiesConfig propertiesConfig, CommonReadConfigAndSave readConfigAndSave, CommonRegisterLimits registerLimits, RateLimitingSearchKeys limitingSearchKeys, RSLimitLoadPostConstruct rsLimitLoadPostConstruct) {
        super(propertiesConfig, readConfigAndSave);
        this.registerLimits = registerLimits;
        this.limitingSearchKeys = limitingSearchKeys;
        this.rsLimitLoadPostConstruct = rsLimitLoadPostConstruct;
    }

    public void registrationOfLimits(String key, List<REGSLimit> limits) {
        registerLimits.registrationOfLimits(key, limits);
    }

    public void registrationOfTariffs(String key, List<REGSTariff> tariffs) {
        registerLimits.registrationOfTariffs(key, tariffs);
    }

    // rewrite search
    public RateLimiting searchKeys(String key, String url, String method, Map<String, String> attributes) {
        return limitingSearchKeys.searchKeys(new RLSearch(key, url, method, attributes));
    }

    public List<RSLimit> getLimits() {
        return rsLimitLoadPostConstruct.getList();
    }
}
