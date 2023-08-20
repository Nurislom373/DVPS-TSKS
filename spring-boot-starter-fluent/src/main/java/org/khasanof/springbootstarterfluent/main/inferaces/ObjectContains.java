package org.khasanof.springbootstarterfluent.main.inferaces;

/**
 * @author Nurislom
 * @see org.khasanof.springbootstarterfluent.main.inferaces
 * @since 8/19/2023 12:34 AM
 */
public interface ObjectContains<T> {

    boolean containsEnum(Class<? extends T> clazz);

}
