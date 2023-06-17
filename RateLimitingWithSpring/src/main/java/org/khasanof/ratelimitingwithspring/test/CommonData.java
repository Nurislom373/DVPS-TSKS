package org.khasanof.ratelimitingwithspring.test;

import org.khasanof.ratelimitingwithspring.core.common.CommonLimitsService;
import org.khasanof.ratelimitingwithspring.core.common.register.classes.REGSLimit;
import org.khasanof.ratelimitingwithspring.core.common.register.classes.REGSTariff;
import org.khasanof.ratelimitingwithspring.core.common.register.classes.REGSTariffApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;
import java.util.Map;

/**
 * Author: Nurislom
 * <br/>
 * Date: 4/17/2023
 * <br/>
 * Time: 3:51 PM
 * <br/>
 * Package: org.khasanof.ratelimitingwithspring.test
 */
@Component
public class CommonData implements CommandLineRunner {

    @Autowired
    private CommonLimitsService commonLimitsService;

    /**
     * Callback used to run the bean.
     *
     * @param args incoming main method arguments
     * @throws Exception on error
     */
    @Override
    public void run(String... args) throws Exception {
//        registerTariffsFirst();
    }

    private void limitRegister() {
        String key = "431";
        List<REGSLimit> limits = List.of(
                new REGSLimit("/api/v1/echo/*", RequestMethod.GET, Map.of("value", "nurislom"), "BASIC", 2L),
                new REGSLimit("/api/v1/echo/*", RequestMethod.GET, Map.of("value", "373"), "BASIC", 2L)
        );
        commonLimitsService.registrationOfLimits(key, limits);
    }

    private void registerTariffsFirst() {
        String key1 = "432";
        String key2 = "434";

        List<REGSTariff> key1Tariff = List.of(
                new REGSTariff("BASIC", List.of(
                        new REGSTariffApi("/api/v1/value", RequestMethod.GET, null),
                        new REGSTariffApi("/api/v1/check", RequestMethod.GET, null)
                ), 2L)
        );

        List<REGSTariff> key2Tariff = List.of(
                new REGSTariff("PRO", List.of(
                        new REGSTariffApi("/api/v1/value", RequestMethod.GET, null),
                        new REGSTariffApi("/api/v1/echo/*", RequestMethod.GET, Map.of("value", "nurislom"))
                ), 1L)
        );

        commonLimitsService.registrationOfTariffs(key1, key1Tariff);
        commonLimitsService.registrationOfTariffs(key2, key2Tariff);
    }

    private void keyFive() {
        String key1 = "432";
        String key2 = "436";

        List<REGSTariff> key1Tariff = List.of(
                new REGSTariff("BASIC", List.of(
                        new REGSTariffApi("/api/v1/value", RequestMethod.GET, null),
                        new REGSTariffApi("/api/v1/value", RequestMethod.GET, null)
                ), 2L)
        );

        List<REGSLimit> limits = List.of(
                new REGSLimit("/api/v1/value", RequestMethod.GET, null, "FREE", 0L)
        );

//        commonLimitsService.registrationOfLimits(key2, limits);
//        commonLimitsService.registrationOfLimits(key1, limits);
//        commonLimitsService.registrationOfTariffs(key1, key1Tariff);
    }


}
