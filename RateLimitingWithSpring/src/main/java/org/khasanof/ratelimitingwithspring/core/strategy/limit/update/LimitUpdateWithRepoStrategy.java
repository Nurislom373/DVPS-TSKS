package org.khasanof.ratelimitingwithspring.core.strategy.limit.update;

import lombok.extern.slf4j.Slf4j;
import org.khasanof.ratelimitingwithspring.core.domain.Api;
import org.khasanof.ratelimitingwithspring.core.domain.Limited;
import org.khasanof.ratelimitingwithspring.core.repository.ApiRepository;
import org.khasanof.ratelimitingwithspring.core.repository.LimitedRepository;
import org.khasanof.ratelimitingwithspring.core.strategy.limit.LimitSaveStrategy;
import org.khasanof.ratelimitingwithspring.core.strategy.limit.LimitUpdateStrategy;
import org.khasanof.ratelimitingwithspring.core.strategy.limit.builder.StaticLimitBuilder;
import org.khasanof.ratelimitingwithspring.core.strategy.limit.classes.RSLimit;
import org.khasanof.ratelimitingwithspring.core.utils.BaseUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
        Result result = equals(apiDatabase, apiConfig);
        if (result.equals(Result.NOT_EQUAL)) {
            save(apiConfig);
        } else if (result.equals(Result.LIMITS_NOT_EQUAL)) {

        }
        return false;
    }

    private List<Limited> copyProperties(Map.Entry<Api, List<Limited>> apiDatabase, Map.Entry<Api, List<Limited>> apiConfig) {
        log.info("apiDatabase Limit : {}", apiDatabase);
        log.info("apiConfig Limit : {}", apiConfig);

        List<Limited> list = new ArrayList<>();

        List<Limited> apiConfigValue = apiConfig.getValue();
        List<Limited> apiDatabaseValue = apiDatabase.getValue();

        apiConfigValue.stream().collect(Collectors.filtering());

        return false;
    }

    private Result compareAll(Limited limited, List<Limited> list) {
        return list.stream().allMatch(any -> equalsPlan(limited, any).equals(Result.EQUAL))
    }

    private Limited copyProperties(Limited limited1, Limited limited2) {
        BeanUtils.copyProperties(limited2.getLimitsEmbeddable(),
                limited1.getLimitsEmbeddable());
        return limited1;
    }

    private Result equals(Map.Entry<Api, List<Limited>> apiDatabase, Map.Entry<Api, List<Limited>> apiConfig) {
        Api api1 = apiDatabase.getKey();
        Api api2 = apiConfig.getKey();
        if (!api1.getUrl().equals(api2.getUrl())) {
            return Result.NOT_EQUAL;
        }
        if (!api1.getMethod().equals(api2.getMethod())) {
            return Result.NOT_EQUAL;
        }
        if (!BaseUtils.areEqual(api1.getAttributes(), api2.getAttributes())) {
            return Result.NOT_EQUAL;
        }
        if (equals(apiDatabase.getValue(), apiConfig.getValue())) {
            return Result.LIMITS_EQUAL;
        } else {
            return Result.LIMITS_NOT_EQUAL;
        }
    }

    private boolean equals(List<Limited> limitedList1, List<Limited> limitedList2) {
        if (limitedList1.size() != limitedList2.size()) {
            return false;
        }
        return limitedList1.stream()
                .anyMatch(any1 -> limitedList2.stream()
                        .anyMatch(any2 -> equals(any1, any2)));
    }

    private Result equalsPlan(Limited limited1, Limited limited2) {
        if (limited1.getPlan().equals(limited2.getPlan()))
            return Result.EQUAL;
        return Result.NOT_EQUAL;
    }

    private boolean equals(Limited limited1, Limited limited2) {
        if (limited1.getPlan().equals(limited2.getPlan())) {
            return limited1.getLimitsEmbeddable().equals(limited2.getLimitsEmbeddable());
        }
        return false;
    }



    private void save(Map.Entry<Api, List<Limited>> entry) {
        repository.save(entry.getKey());
        limitedRepository.saveAll(entry.getValue());
    }

    private enum Result {

        LIMITS_NOT_EQUAL,
        LIMITS_EQUAL,
        NOT_EQUAL,
        EQUAL;
    }


}
