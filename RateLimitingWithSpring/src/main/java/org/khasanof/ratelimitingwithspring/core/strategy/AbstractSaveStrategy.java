package org.khasanof.ratelimitingwithspring.core.strategy;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

import java.util.List;

/**
 * Author: Nurislom
 * <br/>
 * Date: 4/18/2023
 * <br/>
 * Time: 4:46 PM
 * <br/>
 * Package: org.khasanof.ratelimitingwithspring.core.strategy
 */
@Transactional
public abstract class AbstractSaveStrategy<T extends BaseRS> {

    @PersistenceContext
    protected EntityManager entityManager;

    public abstract void save(List<T> list);

}
