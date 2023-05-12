package org.khasanof.ratelimitingwithspring.core.exceptions;

/**
 * Author: Nurislom
 * <br/>
 * Date: 12.05.2023
 * <br/>
 * Time: 23:22
 * <br/>
 * Package: org.khasanof.ratelimitingwithspring.core.exceptions
 */
public class InvalidParamException extends RuntimeException {

    public InvalidParamException() {
    }

    public InvalidParamException(String message) {
        super(message);
    }

    public InvalidParamException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidParamException(Throwable cause) {
        super(cause);
    }
}
