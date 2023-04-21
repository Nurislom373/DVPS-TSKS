package org.khasanof.ratelimitingwithspring.core.strategy;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import java.util.List;

/**
 * Author: Nurislom
 * <br/>
 * Date: 4/21/2023
 * <br/>
 * Time: 7:53 PM
 * <br/>
 * Package: org.khasanof.ratelimitingwithspring.core.strategy
 */
@Transactional
@RequiredArgsConstructor
public abstract class AbstractUpdateStrategy<E extends BaseRS, R> {

    protected final R repository;

    public abstract void update(List<E> list);

}
