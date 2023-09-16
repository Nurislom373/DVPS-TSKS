package org.khasanof.trycatch;

/**
 * @author Nurislom
 * @see org.khasanof.trycatch
 * @since 9/16/2023 11:26 PM
 */
public class SneakyThrower {

    @SuppressWarnings("unchecked")
    public static <E extends Throwable> RuntimeException sneakyThrow(Throwable exception) throws E {
        throw (E) exception;
    }

}
