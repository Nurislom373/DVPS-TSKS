package com.example.ratelimitingtests;

import lombok.RequiredArgsConstructor;
import org.khasanof.ratelimitingwithspring.core.LimitsService;
import org.khasanof.ratelimitingwithspring.core.common.register.classes.REGSTariff;
import org.khasanof.ratelimitingwithspring.core.common.register.classes.REGSTariffApi;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;
import java.util.Map;

/**
 * Author: Nurislom
 * <br/>
 * Date: 4/23/2023
 * <br/>
 * Time: 3:41 PM
 * <br/>
 * Package: com.example.ratelimitingtests
 */
@Component
@RequiredArgsConstructor
public class CommonData implements CommandLineRunner {

    private final LimitsService limitsService;

    @Override
    public void run(String... args) throws Exception {
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

        limitsService.registrationOfTariffs(key1, key1Tariff);
        limitsService.registrationOfTariffs(key2, key2Tariff);
    }
}
