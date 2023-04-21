package org.khasanof.ratelimitingwithspring.core.strategy.tariff.update;

import lombok.extern.slf4j.Slf4j;
import org.khasanof.ratelimitingwithspring.core.domain.Tariff;
import org.khasanof.ratelimitingwithspring.core.repository.TariffRepository;
import org.khasanof.ratelimitingwithspring.core.strategy.tariff.TariffUpdateStrategy;
import org.khasanof.ratelimitingwithspring.core.strategy.tariff.builder.StaticTariffBuilder;
import org.khasanof.ratelimitingwithspring.core.strategy.tariff.classes.RSTariff;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

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
        List<Tariff> tariffList = compareAndSetAfterReturn(tariffsFromDatabase, tariffsFromConfig);
        log.info("Updated Tariffs Count : {}", tariffList.size());
        if (!tariffList.isEmpty()) {
            repository.saveAll(tariffList);
        }
    }

    private List<Tariff> compareAndSetAfterReturn(List<Tariff> tariffs1, List<Tariff> tariffs2) {
        return tariffs1.stream().filter(f -> tariffs2.stream()
                .anyMatch(any -> compareAndCopyProperties(f, any)))
                .toList();
    }

    private boolean compareAndCopyProperties(Tariff tariffDatabase, Tariff tariffConfig) {
        if (tariffDatabase.equals(tariffConfig)) {
            return false;
        } else {
            if (tariffDatabase.getName().equals(tariffConfig.getName())) {
                BeanUtils.copyProperties(tariffConfig, tariffDatabase, "id", "name");
                return true;
            } else {
                return false;
            }
        }
    }
}
