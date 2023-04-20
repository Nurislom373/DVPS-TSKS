package org.khasanof.ratelimitingwithspring.test;

import org.khasanof.ratelimitingwithspring.core.common.CommonLimitsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

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
        String key1 = "432";
        String key2 = "434";

//        List<REGSTariff> key1Tariff = List.of(
//                new REGSTariff("BASIC", List.of(
//                        new REGSTariffApi("/api/v1/value", RequestMethod.GET, null),
//                        new REGSTariffApi("/api/v1/check", RequestMethod.GET, null)
//                ), 2L)
//        );
//
//        List<REGSTariff> key2Tariff = List.of(
//                new REGSTariff("PRO", List.of(
//                        new REGSTariffApi("/api/v1/value", RequestMethod.GET, null),
//                        new REGSTariffApi("/api/v1/echo/*", RequestMethod.GET, Map.of("value", "nurislom"))
//                ), 1L)
//        );
//
//        commonLimitsService.registrationOfTariffs(key1, key1Tariff);
//        commonLimitsService.registrationOfTariffs(key2, key2Tariff);
    }
}
