package org.khasanof.ratelimitingwithspring.core.common;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.khasanof.ratelimitingwithspring.core.common.save.REGSTariff;
import org.khasanof.ratelimitingwithspring.core.factory.ReadStrategyFactory;
import org.khasanof.ratelimitingwithspring.core.limiting.RateLimiting;
import org.khasanof.ratelimitingwithspring.core.limiting.ReadLimitsPropertiesConfig;
import org.khasanof.ratelimitingwithspring.core.strategy.limit.LimitReadStrategy;
import org.khasanof.ratelimitingwithspring.core.strategy.limit.LimitSaveStrategy;
import org.khasanof.ratelimitingwithspring.core.strategy.limit.classes.RSLimit;
import org.khasanof.ratelimitingwithspring.core.common.save.REGSLimit;
import org.khasanof.ratelimitingwithspring.core.strategy.tariff.TariffReadStrategy;
import org.khasanof.ratelimitingwithspring.core.strategy.tariff.TariffSaveStrategy;
import org.khasanof.ratelimitingwithspring.core.strategy.tariff.classes.RSTariff;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Author: Nurislom
 * <br/>
 * Date: 4/14/2023
 * <br/>
 * Time: 2:58 PM
 * <br/>
 * Package: org.khasanof.ratelimitingwithspring.core
 */
@Service
@RequiredArgsConstructor
public class CommonLimitsService {

    private List<RSLimit> limits = new ArrayList<>();
    private List<RSTariff> tariffs = new ArrayList<>();
    private final ReadStrategyFactory strategyFactory;
    private final ReadLimitsPropertiesConfig propertiesConfig;
    private final CommonRegisterLimits registerLimits;
    private final CommonGetLimits commonGetLimits;
    private final LimitSaveStrategy limitSaveStrategy;
    private final TariffSaveStrategy tariffSaveStrategy;

    @PostConstruct
    void afterPropertiesSet() {
        readConfigAndSave();
    }

    public void registrationOfLimits(String key, List<REGSLimit> limits) {
        registerLimits.registrationOfLimits(key, limits);
    }

    public void registrationOfTariffs(String key, List<REGSTariff> tariffs) {
        registerLimits.registrationOfTariffs(key, tariffs);
    }

    // rewrite search
    public RateLimiting getKeyAndUrl(String key, String url, String method) {
        return commonGetLimits.getKeyAndUrl(key, url, method);
    }

    public List<RSLimit> getPublicLimits() {
        return limits;
    }

    public List<RSTariff> getPublicTariffs() {
        return tariffs;
    }

    private void readConfigAndSave() {
        if (!propertiesConfig.getApiLimitsEnabled() && !propertiesConfig.getPackageEnabled()) {
            throw new RuntimeException("Both cannot be false!");
        } else if (propertiesConfig.getApiLimitsEnabled() && propertiesConfig.getPackageEnabled()) {
            setLimits(getLimits());
            setTariffs(getTariffs());
            saveLimits(getPublicLimits());
            saveTariff(getPublicTariffs());
        } else if (propertiesConfig.getApiLimitsEnabled()) {
            setLimits(getLimits());
            saveLimits(getPublicLimits());
        } else {
            setTariffs(getTariffs());
            saveTariff(getPublicTariffs());
        }
    }

    private List<RSLimit> getLimits() {
        try {
            String filePath = propertiesConfig.getApiLimitsConfigFilePath();
            LimitReadStrategy readStrategy = strategyFactory.limitReadStrategy(filePath);
            return readStrategy.read(filePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private List<RSTariff> getTariffs() {
        try {
            String filePath = propertiesConfig.getPackagesConfigFilePath();
            TariffReadStrategy readStrategy = strategyFactory.tariffReadStrategy(filePath);
            return readStrategy.read(filePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void saveLimits(List<RSLimit> limits) {
        limitSaveStrategy.save(limits);
    }

    private void saveTariff(List<RSTariff> tariffs) {
        tariffSaveStrategy.save(tariffs);
    }

    private void setLimits(List<RSLimit> limits) {
        this.limits = limits;
    }

    private void setTariffs(List<RSTariff> tariffs) {
        this.tariffs = tariffs;
    }


}
