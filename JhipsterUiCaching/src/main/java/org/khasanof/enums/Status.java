package org.khasanof.enums;

import java.util.Arrays;
import java.util.Random;

/**
 * The Status enumeration.
 */
public enum Status {
    FAILED,
    SUCCESS;

    public static Status findAny() {
        return Arrays.asList(values())
            .get(new Random().nextInt(values().length));
    }
}
