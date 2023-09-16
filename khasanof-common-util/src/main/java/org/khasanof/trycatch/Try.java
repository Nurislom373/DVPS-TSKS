package org.khasanof.trycatch;

import java.util.function.Supplier;

/**
 * @author Nurislom
 * @see org.khasanof.trycatch
 * @since 9/16/2023 11:31 PM
 */
public class Try {

    public static TryToCall toCall(Callable callable) {
        return new TryToCall(callable);
    }

    public static <T> TryToGet<T> toGet(Supplier<T> supplier) {
        return new TryToGet<>(supplier);
    }

    public static TryWithResources withResources(AutoCloseable... resources) {
        return new TryWithResources(resources);
    }

}
