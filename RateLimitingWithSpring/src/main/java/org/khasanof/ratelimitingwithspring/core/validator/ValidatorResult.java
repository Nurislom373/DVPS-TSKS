package org.khasanof.ratelimitingwithspring.core.validator;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.khasanof.ratelimitingwithspring.core.exceptions.InvalidValidationException;

/**
 * Author: Nurislom
 * <br/>
 * Date: 4/23/2023
 * <br/>
 * Time: 12:59 AM
 * <br/>
 * Package: org.khasanof.ratelimitingwithspring.core.common.register.validator
 */
@Getter
@Setter
@ToString
public class ValidatorResult {

    private boolean success;
    private String message;
    private Object data;

    public ValidatorResult success(boolean success) {
        this.success = success;
        this.data = null;
        return this;
    }

    public ValidatorResult success(boolean success, String message) {
        this.success = success;
        if (!success) {
            this.message = message;
        }
        return this;
    }

    public ValidatorResult failed(String message) {
        this.success = false;
        this.message = message;
        return this;
    }

    public ValidatorResult data(Object data) {
        this.data = data;
        return this;
    }
}
