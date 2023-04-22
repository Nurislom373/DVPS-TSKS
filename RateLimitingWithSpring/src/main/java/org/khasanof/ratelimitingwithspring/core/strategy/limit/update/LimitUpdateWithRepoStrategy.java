package org.khasanof.ratelimitingwithspring.core.strategy.limit.update;

import lombok.extern.slf4j.Slf4j;
import org.khasanof.ratelimitingwithspring.core.domain.Api;
import org.khasanof.ratelimitingwithspring.core.domain.Limited;
import org.khasanof.ratelimitingwithspring.core.repository.ApiRepository;
import org.khasanof.ratelimitingwithspring.core.repository.LimitedRepository;
import org.khasanof.ratelimitingwithspring.core.strategy.limit.LimitUpdateStrategy;
import org.khasanof.ratelimitingwithspring.core.strategy.limit.builder.StaticLimitBuilder;
import org.khasanof.ratelimitingwithspring.core.strategy.limit.classes.RSLimit;
import org.khasanof.ratelimitingwithspring.core.utils.BaseUtils;
import org.springframework.stereotype.Service;

import java.util.*;
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
        System.out.println("apisFromConfig.size() = " + apisFromConfig.size());

        Map<Api, List<Limited>> falsiesGet = compareAndFalsiesGet(apisFromDatabase, apisFromConfig);
        log.info("New APIs Count : {}", falsiesGet.size());

        if (!falsiesGet.isEmpty()) {
            falsiesGet.entrySet()
                    .forEach(this::save);
            falsiesGet.keySet()
                    .forEach(apisFromConfig::remove);
            log.info("All New APIs Saved!");
        }
        System.out.println("apisFromConfig.size() = " + apisFromConfig.size());

        Map<Api, List<Limited>> updatedMap = compareAndSetAfterReturn(apisFromDatabase, apisFromConfig);
        log.info("Updated APIs Count : {}", updatedMap.size());

        if (!updatedMap.isEmpty()) {
            updatedMap.values().forEach(this::save);
            log.info("Now Updated Limits : {}", updatedMap.size());
        } else {
            log.warn("Now Updated Limits not found!");
        }
    }

    private Map<Api, List<Limited>> compareAndFalsiesGet(Map<Api, List<Limited>> apis1, Map<Api, List<Limited>> apis2) {
        return apis2.entrySet().stream()
                .filter(f1 -> apis1.entrySet().stream()
                        .noneMatch(f2 -> equals(f1.getKey(), f2.getKey())))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (prev, next) -> next, HashMap::new));
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

    private Map<Api, List<Limited>> compareAndSetAfterReturn(Map<Api, List<Limited>> apis1, Map<Api, List<Limited>> apis2) {
        return apis1.entrySet().stream().filter(api1 -> apis2.entrySet().
                        stream().anyMatch(api2 -> compareAndCopyProperties(api1, api2)))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private boolean compareAndCopyProperties(Map.Entry<Api, List<Limited>> apiDatabase, Map.Entry<Api, List<Limited>> apiConfig) {
        if (equals(apiDatabase, apiConfig).equals(Result.LIMITS_NOT_EQUAL)) {
            copyProperties(apiDatabase, apiConfig);
            return true;
        }
        return false;
    }

    private void copyProperties(Map.Entry<Api, List<Limited>> apiDatabase, Map.Entry<Api, List<Limited>> apiConfig) {
        log.info("apiDatabase Limit : {}", apiDatabase);
        log.info("apiConfig Limit : {}", apiConfig);

        List<Limited> list = new ArrayList<>();

        List<Limited> apiConfigValue = new ArrayList<>(apiConfig.getValue());
        List<Limited> apiDatabaseValue = apiDatabase.getValue();

        List<Limited> newLimits = apiConfigValue.stream()
                .filter(f -> !matchAny(f, apiDatabaseValue))
                .collect(Collectors.toCollection(ArrayList::new));
        log.info("New Limits Size : {}", newLimits.size());

        apiConfigValue.removeAll(newLimits);

        List<Limited> updateLimits = apiDatabaseValue.stream()
                .filter(f1 -> apiConfigValue.stream()
                        .anyMatch(f2 -> compareAndCopyProperties(f1, f2))
                ).toList();
        log.info("Updated Limits Size : {}", updateLimits.size());

        if (!newLimits.isEmpty()) {
            list.addAll(newLimits);
            log.info("New Limits Add");
        }
        if (!updateLimits.isEmpty()) {
            list.addAll(updateLimits);
            log.info("Update Limits Add");
        }

        apiDatabase.setValue(list);
    }

    private boolean matchAny(Limited limited, List<Limited> list) {
        boolean anyMatch = list.stream().anyMatch(any -> equalsPlan(limited, any)
                .equals(Result.EQUAL));
        if (!anyMatch) {
            limited.setApi(list.get(0).getApi());
        }
        return anyMatch;
    }

    private boolean compareAndCopyProperties(Limited limited1, Limited limited2) {
        if (equalLimits(limited1, limited2)) {
            return false;
        } else {
            if (limited1.getPlan().equals(limited2.getPlan())) {
                if (!Objects.equals(limited1.getLimitsEmbeddable(), limited2.getLimitsEmbeddable())) {
                    limited1.setLimitsEmbeddable(limited2.getLimitsEmbeddable());
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        }
    }

    private boolean equalLimits(Limited limited1, Limited limited2) {
        if (!limited1.getPlan().equals(limited2.getPlan())) {
            return false;
        }
        if (!Objects.equals(limited1.getLimitsEmbeddable(), limited2.getLimitsEmbeddable())) {
            return false;
        }
        if (!limited1.getApi().getUrl().equals(limited2.getApi().getUrl())) {
            return false;
        }
        if (!limited1.getApi().getMethod().equals(limited2.getApi().getMethod())) {
            return false;
        }
        return BaseUtils.areEqual(limited1.getApi().getAttributes(), limited2.getApi().getAttributes());
    }

    private Result equals(Map.Entry<Api, List<Limited>> apiDatabase, Map.Entry<Api, List<Limited>> apiConfig) {
        Api api1 = apiDatabase.getKey(), api2 = apiConfig.getKey();

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
        return isEqualCollection(limitedList1, limitedList2);
    }

    private boolean isEqualCollection(List<Limited> limitedList1, List<Limited> limitedList2) {
        for (Limited limited : limitedList1) {
            boolean equalPresent = false;
            for (Limited limited1 : limitedList2) {
                if (limited.getPlan().equals(limited1.getPlan())) {
                    if (Objects.equals(limited.getLimitsEmbeddable(), limited1.getLimitsEmbeddable())) {
                        equalPresent = true;
                    }
                }
            }
            if (!equalPresent) {
                return false;
            }
        }
        return true;
    }

    private Result equalsPlan(Limited limited1, Limited limited2) {
        if (limited1.getPlan().equals(limited2.getPlan()))
            return Result.EQUAL;
        return Result.NOT_EQUAL;
    }

    private void save(Map.Entry<Api, List<Limited>> entry) {
        repository.save(entry.getKey());
        limitedRepository.saveAll(entry.getValue());
    }

    private void save(List<Limited> limitedList) {
        limitedRepository.saveAll(limitedList);
    }

    private enum Result {
        LIMITS_NOT_EQUAL,
        LIMITS_EQUAL,
        NOT_EQUAL,
        EQUAL;
    }


}
