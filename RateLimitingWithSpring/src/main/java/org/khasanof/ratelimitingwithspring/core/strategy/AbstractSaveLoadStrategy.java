package org.khasanof.ratelimitingwithspring.core.strategy;

import java.util.List;

/**
 * Author: Nurislom
 * <br/>
 * Date: 4/16/2023
 * <br/>
 * Time: 9:43 PM
 * <br/>
 * Package: org.khasanof.ratelimitingwithspring.core.strategy
 */
public abstract class AbstractSaveLoadStrategy<T> {

    public abstract void save(T t);

    public abstract List<T> findAll();

}
