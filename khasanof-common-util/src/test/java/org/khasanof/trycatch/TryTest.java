package org.khasanof.trycatch;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Nurislom
 * @see org.khasanof.trycatch
 * @since 9/16/2023 11:39 PM
 */
public class TryTest {

    @Test
    @SuppressWarnings("unchecked")
    void toCallTest() {
        thrower();
        Try.toCall(this::thrower)
                .ifRaises(RuntimeException.class)
                .thenCall(throwable -> {
                    assertInstanceOf(RuntimeException.class, throwable);
                    System.out.println("Successfully Thrown RuntimeException");
                })
                .done();
    }

    private void thrower() throws RuntimeException {
        throw new RuntimeException("just test");
    }

}
