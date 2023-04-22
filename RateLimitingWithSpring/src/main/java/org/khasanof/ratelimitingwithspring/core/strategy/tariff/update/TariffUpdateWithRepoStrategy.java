package org.khasanof.ratelimitingwithspring.core.strategy.tariff.update;

import lombok.extern.slf4j.Slf4j;
import org.khasanof.ratelimitingwithspring.core.domain.Tariff;
import org.khasanof.ratelimitingwithspring.core.repository.TariffRepository;
import org.khasanof.ratelimitingwithspring.core.strategy.tariff.TariffUpdateStrategy;
import org.khasanof.ratelimitingwithspring.core.strategy.tariff.builder.StaticTariffBuilder;
import org.khasanof.ratelimitingwithspring.core.strategy.tariff.classes.RSTariff;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Author: Nurislom
 * <br/>
 * Date: 4/21/2023
 * <br/>
 * Time: 8:00 PM
 * <br/>
 * Package: org.khasanof.ratelimitingwithspring.core.strategy.tariff.update
 */
@Slf4j
@Service
public class TariffUpdateWithRepoStrategy extends TariffUpdateStrategy {

    public TariffUpdateWithRepoStrategy(TariffRepository repository) {
        super(repository);
    }

    @Override
    public void update(List<RSTariff> list) {
        List<Tariff> tariffsFromDatabase = repository.findAll();
        List<Tariff> tariffsFromConfig = StaticTariffBuilder.buildTariffList(list);

        List<Tariff> tariffList = new ArrayList<>(compareAndDifferenceReturn(tariffsFromDatabase, tariffsFromConfig));
        log.info("Difference Tariffs Count : {}", tariffList.size());

        if (!tariffList.isEmpty()) {
            repository.saveAll(tariffList);
            tariffsFromConfig.removeAll(tariffList);
        }

        List<Tariff> updatedTariffs = tariffsFromConfig.stream()
                .filter(f1 -> tariffsFromDatabase.stream()
                        .anyMatch(f2 -> compareAndCopyProperties(f1, f2)))
                .toList();
        log.info("Updated Tariffs Count : {}", updatedTariffs.size());
    }

    private List<Tariff> compareAndDifferenceReturn(List<Tariff> tariffs1, List<Tariff> tariffs2) {
        return tariffs2.stream()
                .filter(f -> tariffs1.stream()
                        .noneMatch(f2 -> f.getName().equals(f2.getName())))
                .toList();
    }

    private boolean compareAndCopyProperties(Tariff tariffConfig, Tariff tariffDatabase) {
        if (tariffDatabase.getName().equals(tariffConfig.getName())) {
            if (!Objects.equals(tariffDatabase.getLimitsEmbeddable(), tariffConfig.getLimitsEmbeddable())) {
                tariffDatabase.setLimitsEmbeddable(tariffConfig.getLimitsEmbeddable());
                repository.save(tariffDatabase);
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
}
