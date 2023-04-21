package org.khasanof.ratelimitingwithspring.core.strategy.limit.update;

import lombok.extern.slf4j.Slf4j;
import org.khasanof.ratelimitingwithspring.core.domain.Api;
import org.khasanof.ratelimitingwithspring.core.domain.Limited;
import org.khasanof.ratelimitingwithspring.core.domain.Tariff;
import org.khasanof.ratelimitingwithspring.core.repository.ApiRepository;
import org.khasanof.ratelimitingwithspring.core.repository.LimitedRepository;
import org.khasanof.ratelimitingwithspring.core.strategy.limit.LimitUpdateStrategy;
import org.khasanof.ratelimitingwithspring.core.strategy.limit.builder.StaticLimitBuilder;
import org.khasanof.ratelimitingwithspring.core.strategy.limit.classes.RSLimit;
import org.khasanof.ratelimitingwithspring.core.utils.BaseUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Author: Nurislom
 * <br/>
 * Date: 4/21/2023
 * <br/>
 * Time: 8:25 PM
 * <br/>
 * Package: org.khasanof.ratelimitingwithspring.core.strategy.limit.update
 */
@Slf4j
@Service
public class LimitUpdateWithRepoStrategy extends LimitUpdateStrategy {

    private final LimitedRepository limitedRepository;

    public LimitUpdateWithRepoStrategy(ApiRepository repository, LimitedRepository limitedRepository) {
        super(repository);
        this.limitedRepository = limitedRepository;
    }

    @Override
    public void update(List<RSLimit> list) {
        Map<Api, List<Limited>> apisFromDatabase = limitedRepository.findAll().stream()
                .collect(Collectors.groupingBy(Limited::getApi));
        Map<Api, List<Limited>> apisFromConfig = StaticLimitBuilder.buildApiAndLimitsMap(list);
    }

    private Map<Api, List<Limited>> compareAndSetAfterReturn(Map<Api, List<Limited>> apis1, Map<Api, List<Limited>> api2) {
        return null;
    }

    private boolean compareAndCopyProperties(Map.Entry<Api, List<Limited>> apiDatabase, Map.Entry<Api, List<Limited>> apiConfig) {
        return false;
    }

    private boolean equals(Api api1, Api api2) {
        if (!api1.getUrl().equals(api2.getUrl())) {
            return false;
        }
        if (!api1.getMethod().equals(api2.getMethod())) {
            return false;
        }
        return BaseUtils.areEqual(api1.getAttributes(), api2.getAttributes());
    }
}
