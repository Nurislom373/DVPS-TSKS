package org.khasanof.core.custom;

/**
 * @author Nurislom
 * @see org.khasanof.core.custom
 * @since 05.07.2023 0:14
 */
public class FluentContext {

    public static ThreadLocal<Boolean> booleanLocal = ThreadLocal.withInitial(() -> false);

}
