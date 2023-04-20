package org.khasanof.ratelimitingwithspring.core.strategy.limit.save;

import lombok.extern.slf4j.Slf4j;
import org.khasanof.ratelimitingwithspring.core.domain.Api;
import org.khasanof.ratelimitingwithspring.core.domain.Limited;
import org.khasanof.ratelimitingwithspring.core.domain.embeddable.LimitsEmbeddable;
import org.khasanof.ratelimitingwithspring.core.strategy.limit.LimitSaveStrategy;
import org.khasanof.ratelimitingwithspring.core.strategy.limit.classes.RSLimit;
import org.khasanof.ratelimitingwithspring.core.strategy.limit.classes.RSLimitPlan;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

import static org.khasanof.ratelimitingwithspring.core.config.ApplicationProperties.SAVE_STRATEGY;

/**
 * Author: Nurislom
 * <br/>
 * Date: 4/18/2023
 * <br/>
 * Time: 5:29 PM
 * <br/>
 * Package: org.khasanof.ratelimitingwithspring.core.strategy.limit.save
 */
@Slf4j
@Service(LimitSaveWithEMStrategy.SERVICE_NAME)
public class LimitSaveWithEMStrategy extends LimitSaveStrategy {

    public static final String SERVICE_NAME = "limit" + SAVE_STRATEGY;

    @Override
    public void save(List<RSLimit> list) {
        list.forEach(this::save);
    }

    private void save(RSLimit limit) {
        Api apiEntity = buildApiEntity(limit);
        log.info("build Api : {}", apiEntity);
        entityManager.persist(apiEntity);

        limit.getPlans().stream().map(m -> buildLimitedEntity(m, apiEntity))
                .peek((e) -> log.info("build Limited : {}", e))
                .forEach(entityManager::persist);
    }

    private Api buildApiEntity(RSLimit limit) {
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

    private Limited buildLimitedEntity(RSLimitPlan plan, Api entity) {
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
