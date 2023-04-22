package org.khasanof.ratelimitingwithspring.core.common.read;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.khasanof.ratelimitingwithspring.core.common.load.LoadingPTAStartUp;
import org.khasanof.ratelimitingwithspring.core.common.load.check.ApiCheckRepository;
import org.khasanof.ratelimitingwithspring.core.common.load.check.TariffCheckRepository;
import org.khasanof.ratelimitingwithspring.core.common.load.genericLoad.RSLimitLoadPostConstruct;
import org.khasanof.ratelimitingwithspring.core.common.load.genericLoad.RSTariffLoadPostConstruct;
import org.khasanof.ratelimitingwithspring.core.config.ReadLimitsPropertiesConfig;
import org.khasanof.ratelimitingwithspring.core.strategy.limit.LimitSaveStrategy;
import org.khasanof.ratelimitingwithspring.core.strategy.limit.LimitUpdateStrategy;
import org.khasanof.ratelimitingwithspring.core.strategy.tariff.TariffSaveStrategy;
import org.khasanof.ratelimitingwithspring.core.strategy.tariff.TariffUpdateStrategy;
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
    private final LimitUpdateStrategy limitUpdateStrategy;
    private final TariffUpdateStrategy tariffUpdateStrategy;
    private final LoadingPTAStartUp startUp;

    @Override
    public void readConfigAndSave(ReadLimitsPropertiesConfig propertiesConfig) {
        rsTariffLoadPostConstruct.afterPropertiesSet(propertiesConfig.getPackagesConfigFilePath());
        rsLimitLoadPostConstruct.afterPropertiesSet(propertiesConfig.getApiLimitsConfigFilePath());
        if (propertiesConfig.getSaveDLLEnum().equals(SaveDLLEnum.CREATE)) {
            log.info("CREATE start");
            if (!apiCheckRepository.check()) {
                if (rsLimitLoadPostConstruct.isPresent()) {
                    limitSaveStrategy.save(rsLimitLoadPostConstruct.getList());
                }
            }
            if (!tariffCheckRepository.check()) {
                if (rsTariffLoadPostConstruct.isPresent()) {
                    tariffSaveStrategy.save(rsTariffLoadPostConstruct.getList());
                }
            }
            log.warn("CREATE end");
        } else if (propertiesConfig.getSaveDLLEnum().equals(SaveDLLEnum.UPDATE)) {
            log.info("UPDATE start");
            if (apiCheckRepository.check() && tariffCheckRepository.check()) {
                if (rsLimitLoadPostConstruct.isPresent() && rsTariffLoadPostConstruct.isPresent()) {
                    tariffUpdateStrategy.update(rsTariffLoadPostConstruct.getList());
                    limitUpdateStrategy.update(rsLimitLoadPostConstruct.getList());
                }
            }
            if (!tariffCheckRepository.check()) {
                tariffSaveStrategy.save(rsTariffLoadPostConstruct.getList());
            }
            if (!apiCheckRepository.check()) {
                limitSaveStrategy.save(rsLimitLoadPostConstruct.getList());
            }
            log.warn("UPDATE end");
        } else {
            log.info("Already Created");
        }
        startUp.loadStartUp();
    }
}
