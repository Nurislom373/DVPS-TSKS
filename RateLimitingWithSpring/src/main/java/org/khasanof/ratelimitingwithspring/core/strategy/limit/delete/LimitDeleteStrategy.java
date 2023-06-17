package org.khasanof.ratelimitingwithspring.core.strategy.limit.delete;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.khasanof.ratelimitingwithspring.core.RateLimiting;
import org.khasanof.ratelimitingwithspring.core.common.search.classes.PTA;
import org.khasanof.ratelimitingwithspring.core.domain.PricingApi;
import org.khasanof.ratelimitingwithspring.core.repository.PricingApiRepository;
import org.khasanof.ratelimitingwithspring.core.strategy.AbstractDeleteStrategy;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Author: Nurislom
 * <br/>
 * Date: 4/23/2023
 * <br/>
 * Time: 8:17 PM
 * <br/>
 * Package: org.khasanof.ratelimitingwithspring.core.strategy.limit.delete
 */
@Slf4j
@Service
public class LimitDeleteStrategy extends AbstractDeleteStrategy<PricingApiRepository> {

    public LimitDeleteStrategy(PricingApiRepository repository) {
        super(repository);
    }

    @Override
    public void delete(Map.Entry<String, PTA> entry) {
        log.info("Enter LIMIT Delete Method");
        PricingApi pricingApi = repository.findByQuery(entry.getKey(), entry.getValue().getApis().get(0))
                .orElseThrow(() -> new RuntimeException("Match API not found!"));
        repository.delete(pricingApi);
        log.info("Deleted PricingApi : {}", entry);
    }
}
