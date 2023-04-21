package org.khasanof.ratelimitingwithspring.core.strategy.limit;

import org.khasanof.ratelimitingwithspring.core.repository.ApiRepository;
import org.khasanof.ratelimitingwithspring.core.strategy.AbstractUpdateStrategy;
import org.khasanof.ratelimitingwithspring.core.strategy.limit.classes.RSLimit;

/**
 * Author: Nurislom
 * <br/>
 * Date: 4/21/2023
 * <br/>
 * Time: 8:24 PM
 * <br/>
 * Package: org.khasanof.ratelimitingwithspring.core.strategy.limit
 */
public abstract class LimitUpdateStrategy extends AbstractUpdateStrategy<RSLimit, ApiRepository> {

    public LimitUpdateStrategy(ApiRepository repository) {
        super(repository);
    }
}
