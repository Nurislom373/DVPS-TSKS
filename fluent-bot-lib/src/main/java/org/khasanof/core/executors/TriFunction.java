package org.khasanof.core.executors;

/**
 * @author Nurislom
 * @see org.khasanof.core.executors
 * @since 02.07.2023 15:33
 */
public interface TriFunction<T, R, TYPE> {

    R apply(T t1, T t2, TYPE type);

}
