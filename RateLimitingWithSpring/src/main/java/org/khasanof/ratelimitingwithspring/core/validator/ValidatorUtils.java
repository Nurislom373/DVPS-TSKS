package org.khasanof.ratelimitingwithspring.core.validator;

import org.khasanof.ratelimitingwithspring.core.exceptions.InvalidValidationException;

import java.util.Collection;
import java.util.Objects;

/**
 * Author: Nurislom
 * <br/>
 * Date: 4/23/2023
 * <br/>
 * Time: 5:28 PM
 * <br/>
 * Package: org.khasanof.ratelimitingwithspring.core.validator
 */
public interface ValidatorUtils {

    static void nonNull(String key, Collection<?> collection) {
        if (Objects.isNull(key)) {
            throw new InvalidValidationException("key param is null!");
        }
        if (Objects.isNull(collection)) {
            throw new InvalidValidationException("any collection param is null!");
        }
        if (collection.isEmpty()) {
            throw new InvalidValidationException("any collection is empty! there must be at least one object!");
        }
    }

}
