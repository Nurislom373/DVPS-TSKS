package org.khasanof.ratelimitingwithspring.core.strategy.limit;

import lombok.RequiredArgsConstructor;
import org.khasanof.ratelimitingwithspring.core.strategy.AbstractSaveStrategy;
import org.khasanof.ratelimitingwithspring.core.strategy.limit.classes.RSLimit;
import org.khasanof.ratelimitingwithspring.core.validator.limit.save.LimitSaveValidator;

import java.util.List;

/**
 * Author: Nurislom
 * <br/>
 * Date: 4/18/2023
 * <br/>
 * Time: 5:24 PM
 * <br/>
 * Package: org.khasanof.ratelimitingwithspring.core.strategy.limit
 */
@RequiredArgsConstructor
public abstract class LimitSaveStrategy extends AbstractSaveStrategy<RSLimit> {

    protected final LimitSaveValidator validator;

}
