package org.khasanof.ratelimitingwithspring.core.strategy.save;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.khasanof.ratelimitingwithspring.core.strategy.read.ReadLimit;
import org.khasanof.ratelimitingwithspring.core.strategy.read.ReadPlan;
import org.khasanof.ratelimitingwithspring.domain.ApiEntity;
import org.khasanof.ratelimitingwithspring.domain.LimitedEntity;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Author: Nurislom
 * <br/>
 * Date: 4/16/2023
 * <br/>
 * Time: 9:48 PM
 * <br/>
 * Package: org.khasanof.ratelimitingwithspring.core.strategy.save
 */
@Service
public class ApiESLStrategy {

    @PersistenceContext
    private EntityManager entityManager;

    public void saveAll(List<ReadLimit> limits) {
        limits.forEach(this::save);
    }

    public void save(ReadLimit limit) {
        ApiEntity apiEntity = buildApiEntity(limit);
        entityManager.persist(apiEntity);

        limit.getPlans().stream().map(m -> buildLimitedEntity(m, apiEntity))
                .forEach(entityManager::persist);
    }

    public List<ApiEntity> findAll() {
        return entityManager.createQuery("FROM ApiEntity", ApiEntity.class)
                .getResultList();
    }

    private ApiEntity buildApiEntity(ReadLimit limit) {
        return ApiEntity.builder()
                .url(limit.getUrl())
                .method(limit.getMethod())
                .isCustomized(limit.isCustomize())
                .build();
    }

    private LimitedEntity buildLimitedEntity(ReadPlan plan, ApiEntity entity) {
        return LimitedEntity.builder()
                .customized(entity.isCustomized())
                .api(entity)
                .requestType(plan.getRequestType())
                .requestCount(plan.getRequestCount())
                .timeType(plan.getTimeType())
                .timeCount(plan.getTimeCount())
                .plan(plan.getPlan())
                .build();
    }
}
