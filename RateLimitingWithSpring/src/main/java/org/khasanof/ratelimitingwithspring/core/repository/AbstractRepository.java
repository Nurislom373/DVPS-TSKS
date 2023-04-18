package org.khasanof.ratelimitingwithspring.core.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Author: Nurislom
 * <br/>
 * Date: 4/16/2023
 * <br/>
 * Time: 9:43 PM
 * <br/>
 * Package: org.khasanof.ratelimitingwithspring.core.strategy
 */
public abstract class AbstractRepository<T, ID> {

    @PersistenceContext
    protected EntityManager entityManager;

    public abstract void save(T entity);

    public abstract List<T> findAll();

    public abstract Optional<T> findById(ID id);

}
