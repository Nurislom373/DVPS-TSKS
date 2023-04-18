package org.khasanof.ratelimitingwithspring.core.common;

import org.khasanof.ratelimitingwithspring.core.common.save.REGSLimit;
import org.khasanof.ratelimitingwithspring.core.common.save.REGSTariff;

import java.util.List;

/**
 * Author: Nurislom
 * <br/>
 * Date: 4/16/2023
 * <br/>
 * Time: 10:45 PM
 * <br/>
 * Package: org.khasanof.ratelimitingwithspring.core
 */
public interface CommonRegisterLimits {

    void registrationOfLimits(String key, List<REGSLimit> limits);

    void registrationOfTariffs(String key, List<REGSTariff> tariffs);

}
