package org.khasanof.core.exceptions;

/**
 * @author Nurislom
 * @see org.khasanof.core.exceptions
 * @since 19.07.2023 12:12
 */
public class NotImplementedException extends RuntimeException {

    public NotImplementedException() {
    }

    public NotImplementedException(String message) {
        super(message);
    }

    public NotImplementedException(String message, Throwable cause) {
        super(message, cause);
    }
}
