package org.khasanof.springbootstarterfluent.core.custom;

/**
 * @author Nurislom
 * @see org.khasanof.core.custom
 * @since 05.07.2023 0:14
 */
public class FluentContext {

    public static ThreadLocal<Boolean> updateExecutorBoolean = ThreadLocal.withInitial(() -> false);

    public static ThreadLocal<Boolean> determinationServiceBoolean = ThreadLocal.withInitial(() -> false);

}
