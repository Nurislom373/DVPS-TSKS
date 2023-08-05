package org.khasanof.springbootstarterfluent.core.exceptions;

/**
 * @author Nurislom
 * @see org.khasanof.core.exceptions
 * @since 06.07.2023 20:06
 */
public class InvalidParamsException extends RuntimeException {

    public InvalidParamsException() {
    }

    public InvalidParamsException(String message) {
        super(message);
    }

    public InvalidParamsException(String message, Throwable cause) {
        super(message, cause);
    }
}
