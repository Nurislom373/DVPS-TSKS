package org.khasanof.trycatch;

import java.util.function.Supplier;

/**
 * @author Nurislom
 * @see org.khasanof.trycatch
 * @since 9/16/2023 11:38 PM
 */
public class TryWithResources {

    final private AutoCloseable[] resources;

    public TryWithResources(final AutoCloseable[] resources) {
        this.resources = resources;
    }

    public TryToCall toCall(final Callable callable) {
        return new TryToCall(callable, this.resources);
    }

    public <T> TryToGet<T> toGet(Supplier<T> supplier) {
        return new TryToGet<>(supplier, this.resources);
    }

}
