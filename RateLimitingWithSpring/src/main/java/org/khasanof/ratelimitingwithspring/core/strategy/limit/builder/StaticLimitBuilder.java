package org.khasanof.ratelimitingwithspring.core.strategy.limit.builder;

import org.khasanof.ratelimitingwithspring.core.domain.Api;
import org.khasanof.ratelimitingwithspring.core.domain.Limited;
import org.khasanof.ratelimitingwithspring.core.domain.embeddable.LimitsEmbeddable;
import org.khasanof.ratelimitingwithspring.core.strategy.limit.classes.RSLimit;
import org.khasanof.ratelimitingwithspring.core.strategy.limit.classes.RSLimitPlan;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Author: Nurislom
 * <br/>
 * Date: 4/21/2023
 * <br/>
 * Time: 8:26 PM
 * <br/>
 * Package: org.khasanof.ratelimitingwithspring.core.strategy.limit.builder
 */
public abstract class StaticLimitBuilder {

    public static Map<Api, List<Limited>> buildApiAndLimitsMap(List<RSLimit> list) {
        return list.stream().map(StaticLimitBuilder::buildApiAndLimitsEntry)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public static Map.Entry<Api, List<Limited>> buildApiAndLimitsEntry(RSLimit limit) {
        Api api = buildApiEntity(limit);
        return new AbstractMap.SimpleEntry<>(api, limit.getPlans().stream()
                .map(plan -> buildLimitedEntity(plan, api))
                .toList());
    }

    public static Api buildApiEntity(RSLimit limit) {
        if (limit.getVariables() == null) {
            return Api.builder()
                    .url(limit.getUrl())
                    .method(limit.getMethod())
                    .attributes(new HashMap<>())
                    .build();
        } else {
            return Api.builder()
                    .url(limit.getUrl())
                    .method(limit.getMethod())
                    .attributes(limit.getVariables())
                    .build();
        }
    }

    public static Limited buildLimitedEntity(RSLimitPlan plan, Api entity) {
        return Limited.builder()
                .api(entity)
                .plan(plan.getPlan())
                .limitsEmbeddable(LimitsEmbeddable.builder()
                        .undiminishedCount(plan.getRequestCount())
                        .requestType(plan.getRequestType())
                        .requestCount(plan.getRequestCount())
                        .timeType(plan.getTimeType())
                        .timeCount(plan.getTimeCount())
                        .build())
                .build();
    }

}
