package org.khasanof.ratelimitingwithspring.core;

import org.khasanof.ratelimitingwithspring.core.common.register.classes.REGSLimit;
import org.khasanof.ratelimitingwithspring.core.common.register.classes.REGSTariff;
import org.khasanof.ratelimitingwithspring.core.strategy.limit.classes.RSLimit;
import org.springframework.beans.factory.InitializingBean;

import java.util.List;
import java.util.Map;

/**
 * Author: Nurislom
 * <br/>
 * Date: 4/20/2023
 * <br/>
 * Time: 11:04 PM
 * <br/>
 * Package: org.khasanof.ratelimitingwithspring.core
 */
public interface LimitsService extends InitializingBean {

    void registrationOfLimits(String key, List<REGSLimit> limits);

    void registrationOfTariffs(String key, List<REGSTariff> tariffs);

    RateLimiting searchKeys(String key, String url, String method, Map<String, String> attributes);

    List<RSLimit> getLimits();


}
