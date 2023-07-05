package org.khasanof.core.utils;

import org.khasanof.core.collector.loader.HandleScannerLoader;
import org.reflections.Reflections;

/**
 * @author Nurislom
 * @see org.khasanof.core.utils
 * @since 05.07.2023 22:33
 */
public abstract class ReflectionUtils {

    private static final HandleScannerLoader loader = new HandleScannerLoader();
    private static final Reflections reflections = new Reflections(loader.getBasePackage());

    public static Reflections getReflections() {
        return reflections;
    }
}
