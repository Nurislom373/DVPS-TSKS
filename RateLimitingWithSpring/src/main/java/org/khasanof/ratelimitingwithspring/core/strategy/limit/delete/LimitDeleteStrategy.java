package org.khasanof.ratelimitingwithspring.core.strategy.limit.delete;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.khasanof.ratelimitingwithspring.core.RateLimiting;
import org.khasanof.ratelimitingwithspring.core.common.search.classes.PTA;
import org.khasanof.ratelimitingwithspring.core.repository.PricingApiRepository;
import org.khasanof.ratelimitingwithspring.core.strategy.AbstractDeleteStrategy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
    public void delete(Map.Entry<String, Map<PTA, RateLimiting>> entry) {
        long count = entry.getValue().keySet().stream()
                .map(p -> repository.findByQuery(entry.getKey(), p.getApis().get(0)))
                .map(optional -> optional
                        .orElseThrow(() -> new RuntimeException("Match API not found!")))
                .peek(repository::delete)
                .count();
        log.info("Deleted PricingApi Count : {}", count);
    }
}
