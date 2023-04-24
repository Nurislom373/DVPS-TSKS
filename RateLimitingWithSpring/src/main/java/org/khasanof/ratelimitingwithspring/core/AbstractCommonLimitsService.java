package org.khasanof.ratelimitingwithspring.core;

import lombok.RequiredArgsConstructor;
import org.khasanof.ratelimitingwithspring.core.common.read.CommonReadConfigAndSave;
import org.khasanof.ratelimitingwithspring.core.validator.register.RegisterLTValidator;
import org.khasanof.ratelimitingwithspring.core.validator.register.RegisterLimitsValidator;
import org.khasanof.ratelimitingwithspring.core.config.ReadLimitsPropertiesConfig;
import org.khasanof.ratelimitingwithspring.core.validator.register.RegisterTariffValidator;

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
    protected final RegisterLimitsValidator registerLimitsValidator;
    protected final RegisterTariffValidator registerTariffValidator;
    protected final RegisterLTValidator validator;

    @Override
    public void afterPropertiesSet() throws Exception {
        readConfigAndSave.readConfigAndSave(propertiesConfig);
    }
}
