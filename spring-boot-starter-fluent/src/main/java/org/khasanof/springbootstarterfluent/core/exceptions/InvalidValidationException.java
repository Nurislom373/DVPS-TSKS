package org.khasanof.springbootstarterfluent.core.exceptions;

/**
 * @author Nurislom
 * @see org.khasanof.springbootstarterfluent.core.exceptions
 * @since 8/19/2023 12:37 AM
 */
public class InvalidValidationException extends RuntimeException {

    public InvalidValidationException() {
    }

    public InvalidValidationException(String message) {
        super(message);
    }

    public InvalidValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
