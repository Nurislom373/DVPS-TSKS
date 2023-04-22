package org.khasanof.ratelimitingwithspring.core;

import lombok.RequiredArgsConstructor;
import org.khasanof.ratelimitingwithspring.core.LimitsService;
import org.khasanof.ratelimitingwithspring.core.common.read.CommonReadConfigAndSave;
import org.khasanof.ratelimitingwithspring.core.config.ReadLimitsPropertiesConfig;

/**
 * Author: Nurislom
 * <br/>
 * Date: 4/20/2023
 * <br/>
 * Time: 10:24 PM
 * <br/>
 * Package: org.khasanof.ratelimitingwithspring.core.common
 */
@RequiredArgsConstructor
public abstract class AbstractCommonLimitsService implements LimitsService {

    private final ReadLimitsPropertiesConfig propertiesConfig;
    private final CommonReadConfigAndSave readConfigAndSave;

    @Override
    public void afterPropertiesSet() throws Exception {
        readConfigAndSave.readConfigAndSave(propertiesConfig);
    }
}
