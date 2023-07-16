package org.khasanof.converter;

import lombok.SneakyThrows;

/**
 * @author Nurislom
 * @see org.khasanof.converter
 * @since 09.07.2023 0:10
 */
public class ComponentFactory<C> {

    private C clazz;

    @SneakyThrows
    private void setClazz() {
        this.clazz = (C) clazz.getClass().newInstance();
    }

    public C getClazz() {
        if (clazz == null) {
            setClazz();
        }
        return clazz;
    }
}
