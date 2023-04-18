package org.khasanof.ratelimitingwithspring.core.repository;

import jakarta.transaction.Transactional;
import org.khasanof.ratelimitingwithspring.core.domain.PricingApi;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Author: Nurislom
 * <br/>
 * Date: 4/17/2023
 * <br/>
 * Time: 11:42 AM
 * <br/>
 * Package: org.khasanof.ratelimitingwithspring.core.strategy
 */
@Service
@Transactional
public class PricingApiEntityRepository extends AbstractRepository<PricingApi, Long> {

    @Override
    public void save(PricingApi entity) {
        if (Objects.nonNull(entity)) {
            entityManager.merge(entity);
        } else {
            throw new NullPointerException("Object is null!");
        }
    }

    @Override
    public List<PricingApi> findAll() {
        return entityManager.createQuery("FROM PricingApi", PricingApi.class)
                .getResultList();
    }

    @Override
    public Optional<PricingApi> findById(Long id) {
        return Optional.ofNullable(entityManager.find(PricingApi.class, id));
    }

    public Optional<PricingApi> findByKeyAndUrl(String key, String url, RequestMethod method) {
        return Optional.ofNullable(
                entityManager.createQuery("FROM PricingApi p where key = :key and api.url = :url and api.method = :method",
                                PricingApi.class)
                        .setParameter("key", key)
                        .setParameter("url", url)
                        .setParameter("method", method)
                        .getSingleResult());
    }
}
