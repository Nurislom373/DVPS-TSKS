package org.khasanof.ratelimitingwithspring.core.strategy.tariff.delete;

import lombok.extern.slf4j.Slf4j;
import org.khasanof.ratelimitingwithspring.core.RateLimiting;
import org.khasanof.ratelimitingwithspring.core.common.search.classes.PTA;
import org.khasanof.ratelimitingwithspring.core.domain.PricingTariff;
import org.khasanof.ratelimitingwithspring.core.repository.PricingTariffRepository;
import org.khasanof.ratelimitingwithspring.core.strategy.AbstractDeleteStrategy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class TariffDeleteStrategy extends AbstractDeleteStrategy<PricingTariffRepository> {

    public TariffDeleteStrategy(PricingTariffRepository repository) {
        super(repository);
    }

    @Override
    public void delete(Map.Entry<String, Map<PTA, RateLimiting>> entry) {
        log.info("Enter TARIFF Delete Method");
        entry.getValue().keySet().stream()
                .map(m -> repository.findAllByKey(entry.getKey())
                        .stream().filter(f -> equals(f, m))
                        .findFirst().orElseThrow(() -> new RuntimeException("PricingTariff not found"))
                ).forEach(repository::delete);
        log.info("Deleted PricingTariff Count : {}", entry.getValue().size());
    }

    private boolean equals(PricingTariff tariff, PTA pta) {
        List<Long> longs = tariff.getApis();
        return pta.getApis().stream()
                .allMatch(e -> longs.contains(e.getId()));
    }
}
