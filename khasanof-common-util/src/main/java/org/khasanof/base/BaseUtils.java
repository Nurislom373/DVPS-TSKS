package org.khasanof.base;

import java.util.Arrays;
import java.util.Objects;

/**
 * @author Nurislom
 * @see org.khasanof.base
 * @since 8/30/2023 11:58 PM
 */
public abstract class BaseUtils {

    public static void checkArgsIsNonNull(Object... args) {
        var anyMatch = Arrays.stream(args).anyMatch(Objects::isNull);
        if (anyMatch) {
            throw new NullPointerException("one or more param is null!");
        }
    }

}
