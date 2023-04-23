package org.khasanof.ratelimitingwithspring.core.validator;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

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
    private Exception exception;

    public ValidatorResult success(boolean success) {
        this.success = success;
        return this;
    }

    public ValidatorResult failed(Exception e) {
        this.success = false;
        this.exception = e;
        return this;
    }
}
