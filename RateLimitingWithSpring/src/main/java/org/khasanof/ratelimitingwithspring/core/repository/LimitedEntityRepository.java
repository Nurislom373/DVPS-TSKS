package org.khasanof.ratelimitingwithspring.core.repository;

import jakarta.transaction.Transactional;
import org.khasanof.ratelimitingwithspring.core.domain.Limited;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Author: Nurislom
 * <br/>
 * Date: 4/17/2023
 * <br/>
 * Time: 12:01 PM
 * <br/>
 * Package: org.khasanof.ratelimitingwithspring.core.repository
 */
@Repository
@Transactional
public class LimitedEntityRepository extends AbstractRepository<Limited, Long> {

    @Override
    public void save(Limited entity) {
        if (Objects.nonNull(entity)) {
            entityManager.persist(entity);
        } else {
            throw new NullPointerException("Object is null!");
        }
    }

    @Override
    public List<Limited> findAll() {
        return entityManager.createQuery("FROM Limited", Limited.class)
                .getResultList();
    }

    @Override
    public Optional<Limited> findById(Long id) {
        return Optional.ofNullable(entityManager.find(Limited.class, id));
    }

    public Optional<Limited> findByPlan(String planName, String url) {
        return Optional.ofNullable(entityManager.createQuery("FROM Limited e where e.plan = :plan and e.api.url = :url", Limited.class)
                        .setParameter("plan", planName)
                        .setParameter("url", url)
                .getSingleResult());
    }
}
