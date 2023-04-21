package org.khasanof.ratelimitingwithspring.core.common.read;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.khasanof.ratelimitingwithspring.core.common.load.check.ApiCheckRepository;
import org.khasanof.ratelimitingwithspring.core.common.load.check.TariffCheckRepository;
import org.khasanof.ratelimitingwithspring.core.common.load.genericLoad.RSLimitLoadPostConstruct;
import org.khasanof.ratelimitingwithspring.core.common.load.genericLoad.RSTariffLoadPostConstruct;
import org.khasanof.ratelimitingwithspring.core.config.ReadLimitsPropertiesConfig;
import org.khasanof.ratelimitingwithspring.core.strategy.limit.LimitSaveStrategy;
import org.khasanof.ratelimitingwithspring.core.strategy.tariff.TariffSaveStrategy;
import org.springframework.stereotype.Service;

/**
 * Author: Nurislom
 * <br/>
 * Date: 4/21/2023
 * <br/>
 * Time: 5:40 PM
 * <br/>
 * Package: org.khasanof.ratelimitingwithspring.core.common.read
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CommonReadConfigAndSave implements ReadConfigAndSave {

    private final ApiCheckRepository apiCheckRepository;
    private final TariffCheckRepository tariffCheckRepository;
    private final RSLimitLoadPostConstruct rsLimitLoadPostConstruct;
    private final RSTariffLoadPostConstruct rsTariffLoadPostConstruct;
    private final TariffSaveStrategy tariffSaveStrategy;
    private final LimitSaveStrategy limitSaveStrategy;

    @Override
    public void readConfigAndSave(ReadLimitsPropertiesConfig propertiesConfig) {
        if (propertiesConfig.getSaveDLLEnum().equals(SaveDLLEnum.CREATE)) {
            if (!apiCheckRepository.check()) {
                rsLimitLoadPostConstruct.afterPropertiesSet(propertiesConfig.getApiLimitsConfigFilePath());
                if (rsLimitLoadPostConstruct.isPresent()) {
                    limitSaveStrategy.save(rsLimitLoadPostConstruct.getList());
                }
            }
            if (!tariffCheckRepository.check()) {
                rsTariffLoadPostConstruct.afterPropertiesSet(propertiesConfig.getPackagesConfigFilePath());
                if (rsTariffLoadPostConstruct.isPresent()) {
                    tariffSaveStrategy.save(rsTariffLoadPostConstruct.getList());
                }
            }
        } else if (propertiesConfig.getSaveDLLEnum().equals(SaveDLLEnum.UPDATE)) {
            if (apiCheckRepository.check() && tariffCheckRepository.check()) {
                rsLimitLoadPostConstruct.afterPropertiesSet(propertiesConfig.getApiLimitsConfigFilePath());
                rsTariffLoadPostConstruct.afterPropertiesSet(propertiesConfig.getPackagesConfigFilePath());
                if (rsLimitLoadPostConstruct.isPresent() && rsTariffLoadPostConstruct.isPresent()) {

                }
            }
        }
    }
}
