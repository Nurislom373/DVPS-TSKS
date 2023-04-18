package org.khasanof.ratelimitingwithspring.test;

import org.khasanof.ratelimitingwithspring.core.common.CommonLimitsService;
import org.khasanof.ratelimitingwithspring.core.common.save.REGSLimit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

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
        String key = "5h489hg84";
        List<REGSLimit> saveLimits = List.of(
                new REGSLimit("/api/v1/value", RequestMethod.GET, null, "BASIC", 1L),
                new REGSLimit("/api/v1/echo/*", RequestMethod.GET, null, "BASIC", 1L)
        );
        commonLimitsService.registrationOfLimits(key, saveLimits);
    }
}
