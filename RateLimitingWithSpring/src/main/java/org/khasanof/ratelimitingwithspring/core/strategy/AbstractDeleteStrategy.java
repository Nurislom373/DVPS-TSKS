package org.khasanof.ratelimitingwithspring.core.strategy;

import lombok.RequiredArgsConstructor;
import org.khasanof.ratelimitingwithspring.core.RateLimiting;
import org.khasanof.ratelimitingwithspring.core.common.search.classes.PTA;

import java.util.Map;
import java.util.Set;

/**
 * Author: Nurislom
 * <br/>
 * Date: 4/23/2023
 * <br/>
 * Time: 8:14 PM
 * <br/>
 * Package: org.khasanof.ratelimitingwithspring.core.strategy
 */
@RequiredArgsConstructor
public abstract class AbstractDeleteStrategy<R> {

    protected final R repository;

    public abstract void delete(Map.Entry<String, PTA> setEntry);

}
