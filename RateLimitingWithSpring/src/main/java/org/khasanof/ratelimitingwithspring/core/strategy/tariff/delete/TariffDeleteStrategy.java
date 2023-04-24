package org.khasanof.ratelimitingwithspring.core.strategy.tariff.delete;

import lombok.extern.slf4j.Slf4j;
import org.khasanof.ratelimitingwithspring.core.RateLimiting;
import org.khasanof.ratelimitingwithspring.core.common.search.classes.PTA;
import org.khasanof.ratelimitingwithspring.core.repository.PricingTariffRepository;
import org.khasanof.ratelimitingwithspring.core.strategy.AbstractDeleteStrategy;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
public class TariffDeleteStrategy extends AbstractDeleteStrategy<PricingTariffRepository> {

    public TariffDeleteStrategy(PricingTariffRepository repository) {
        super(repository);
    }

    @Override
    public void delete(Map.Entry<String, Map<PTA, RateLimiting>> entry) {
        // TODO write service
    }
}
