package org.khasanof.ratelimitingwithspring.core.common.search.findLimit;

import lombok.extern.slf4j.Slf4j;
import org.khasanof.ratelimitingwithspring.core.common.search.RateLimitingSearch;
import org.khasanof.ratelimitingwithspring.core.common.search.RateLimitingSearchUtil;
import org.khasanof.ratelimitingwithspring.core.common.search.classes.PTA;
import org.khasanof.ratelimitingwithspring.core.common.search.classes.RLSearch;
import org.khasanof.ratelimitingwithspring.core.domain.Api;
import org.khasanof.ratelimitingwithspring.core.domain.PricingApi;
import org.khasanof.ratelimitingwithspring.core.exceptions.NotRegisteredException;
import org.khasanof.ratelimitingwithspring.core.limiting.LocalRateLimiting;
import org.khasanof.ratelimitingwithspring.core.repository.ApiRepository;
import org.khasanof.ratelimitingwithspring.core.repository.PricingApiRepository;
import org.khasanof.ratelimitingwithspring.core.repository.PricingTariffRepository;
import org.khasanof.ratelimitingwithspring.core.utils.BaseUtils;
import org.khasanof.ratelimitingwithspring.core.utils.valueBuilder.CacheValueBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Author: Nurislom
 * <br/>
 * Date: 27.05.2023
 * <br/>
 * Time: 19:20
 * <br/>
 * Package: org.khasanof.ratelimitingwithspring.core.common.search.findLimit
 */
@Slf4j
@Service
public class FindLimitWithSearch extends RateLimitingSearchUtil {

    private final CacheValueBuilder cacheValueBuilder;
    private final PricingApiRepository pricingApiRepository;
    private final PricingTariffRepository pricingTariffRepository;
    private final ApiRepository apiRepository;

    public FindLimitWithSearch(CacheValueBuilder cacheValueBuilder, PricingApiRepository pricingApiRepository,
                               PricingTariffRepository pricingTariffRepository, ApiRepository apiRepository) {
        this.cacheValueBuilder = cacheValueBuilder;
        this.pricingApiRepository = pricingApiRepository;
        this.pricingTariffRepository = pricingTariffRepository;
        this.apiRepository = apiRepository;
    }

    public Map.Entry<PTA, LocalRateLimiting> searchKeys(RLSearch search) {
        log.info("Database search start!");
        Map.Entry<PTA, LocalRateLimiting> pricingApiRepo = getOrNullWithPricingApiRepo(search);
        if (Objects.nonNull(pricingApiRepo)) {
            return pricingApiRepo;
        } else {
            Map.Entry<PTA, LocalRateLimiting> pricingTariffRepo = getOrNullWithPricingTariffRepo(search);
            if (Objects.nonNull(pricingTariffRepo)) {
                return pricingTariffRepo;
            } else {
                return null;
            }
        }
    }

    private Map.Entry<PTA, LocalRateLimiting> getOrNullWithPricingApiRepo(RLSearch search) {
        Optional<PricingApi> optional = pricingApiRepository.findByQuery(search.getKey(), new Api(search.getUri(),
                RequestMethod.resolve(search.getMethod()), search.getAttributes()));
        return optional.map(cacheValueBuilder::convertEntityToEntry).orElse(null);
    }

    private Map.Entry<PTA, LocalRateLimiting> getOrNullWithPricingTariffRepo(RLSearch search) {
        return pricingTariffRepository.findAllByKey(search.getKey())
                .stream().filter(f -> matchSearchFound(apiRepository.findAllByIdIsIn(f.getApis()), search))
                .findFirst().map(cacheValueBuilder::convertEntityToEntry).orElse(null);
    }

    private boolean matchSearchFound(List<Api> apis, RLSearch search) {
        return apis.stream()
                .anyMatch(any -> areEqual(any, search));
    }
}
