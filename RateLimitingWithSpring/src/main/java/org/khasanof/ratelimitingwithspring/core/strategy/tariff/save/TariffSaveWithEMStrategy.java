package org.khasanof.ratelimitingwithspring.core.strategy.tariff.save;

import lombok.extern.slf4j.Slf4j;
import org.khasanof.ratelimitingwithspring.core.domain.embeddable.LimitsEmbeddable;
import org.khasanof.ratelimitingwithspring.core.domain.Tariff;
import org.khasanof.ratelimitingwithspring.core.strategy.tariff.TariffSaveStrategy;
import org.khasanof.ratelimitingwithspring.core.strategy.tariff.builder.StaticTariffBuilder;
import org.khasanof.ratelimitingwithspring.core.strategy.tariff.classes.RSTariff;
import org.khasanof.ratelimitingwithspring.core.validator.ValidatorResult;
import org.khasanof.ratelimitingwithspring.core.validator.tariff.TariffSaveValidator;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.khasanof.ratelimitingwithspring.core.config.ApplicationProperties.*;

/**
 * Author: Nurislom
 * <br/>
 * Date: 4/18/2023
 * <br/>
 * Time: 5:56 PM
 * <br/>
 * Package: org.khasanof.ratelimitingwithspring.core.strategy.tariff.save
 */
@Slf4j
@Service(TariffSaveWithEMStrategy.SERVICE_NAME)
public class TariffSaveWithEMStrategy extends TariffSaveStrategy {

    public static final String SERVICE_NAME = "tariff" + SAVE_STRATEGY;

    public TariffSaveWithEMStrategy(TariffSaveValidator validator) {
        super(validator);
    }

    @Override
    public void save(List<RSTariff> list) {
        ValidatorResult result = validator.validatorRSTariff(list);
        if (result.isSuccess()) {
            list.forEach(this::save);
        } else {
            throw new RuntimeException();
        }
    }

    private void save(RSTariff tariff) {
        Tariff buildTariff = StaticTariffBuilder.buildTariff(tariff);
        log.info("build Tariff : {}", buildTariff);
        entityManager.persist(buildTariff);
    }
}
