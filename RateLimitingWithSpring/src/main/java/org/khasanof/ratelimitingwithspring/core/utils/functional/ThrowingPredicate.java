package org.khasanof.ratelimitingwithspring.core.utils.functional;

import org.khasanof.ratelimitingwithspring.core.exceptions.InvalidValidationException;
import org.khasanof.ratelimitingwithspring.core.validator.ValidatorResult;

/**
 * Author: Nurislom
 * <br/>
 * Date: 10.05.2023
 * <br/>
 * Time: 22:24
 * <br/>
 * Package: org.khasanof.ratelimitingwithspring.core.validator
 */
public interface ThrowingPredicate {

    static boolean isTrue(ValidatorResult result) {
        if (!result.isSuccess()) {
            throw new InvalidValidationException(result.getMessage());
        }
        return true;
    }

}
