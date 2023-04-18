package org.khasanof.ratelimitingwithspring.core.repository;

import jakarta.transaction.Transactional;
import org.khasanof.ratelimitingwithspring.core.domain.Api;
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
 * Time: 11:51 AM
 * <br/>
 * Package: org.khasanof.ratelimitingwithspring.core.strategy
 */
@Service
@Transactional
public class ApiEntityRepository extends AbstractRepository<Api, Long> {

    @Override
    public void save(Api entity) {
        if (Objects.nonNull(entity)) {
            entityManager.persist(entity);
        } else {
            throw new NullPointerException("Object is null!");
        }
    }

    @Override
    public List<Api> findAll() {
        return entityManager.createQuery("FROM Api ", Api.class)
                .getResultList();
    }

    @Override
    public Optional<Api> findById(Long id) {
        return Optional.ofNullable(entityManager.find(Api.class, id));
    }

    public Optional<Api> findByUrl(String url, RequestMethod method) {
        return Optional.ofNullable(entityManager.createQuery("FROM Api e where e.url = :url and e.method = :method", Api.class)
                .setParameter("url", url)
                .setParameter("method", method)
                .getSingleResult());
    }
}
