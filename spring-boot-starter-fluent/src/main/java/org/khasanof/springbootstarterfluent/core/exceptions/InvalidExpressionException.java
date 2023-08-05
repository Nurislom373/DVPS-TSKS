package org.khasanof.springbootstarterfluent.core.exceptions;

/**
 * @author Nurislom
 * @see org.khasanof.core.exceptions
 * @since 01.08.2023 21:58
 */
public class InvalidExpressionException extends RuntimeException {

    public InvalidExpressionException() {
    }

    public InvalidExpressionException(String message) {
        super(message);
    }

    public InvalidExpressionException(String message, Throwable cause) {
        super(message, cause);
    }
}
