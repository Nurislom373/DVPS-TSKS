package org.khasanof.core.collector.loader;

import com.google.common.reflect.ClassPath;
import org.khasanof.main.annotation.HandlerScanner;

import java.io.IOException;

/**
 * Author: Nurislom
 * <br/>
 * Date: 22.06.2023
 * <br/>
 * Time: 22:45
 * <br/>
 * Package: org.khasanof.core.collector.flattenPackage
 */
public class HandleScannerLoader {

    private HandlerScanner scanner;

    {
        setScanner();
    }

    // TODO write get annotation value method!
    private HandlerScanner findAllClassesWithHandlerScannerClass() {
        try {
            return ClassPath.from(ClassLoader.getSystemClassLoader())
                    .getAllClasses()
                    .stream().map(clazz -> clazz.load())
                    .filter(this::hasAnnotationClassLevel)
                    .findFirst().orElseThrow(() -> new RuntimeException("HandlerScanner annotated class not found!"))
                    .getAnnotation(HandlerScanner.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean hasAnnotationClassLevel(Class clazz) {
        return clazz.isAnnotationPresent(HandlerScanner.class);
    }

    public void setScanner() {
        this.scanner = findAllClassesWithHandlerScannerClass();
    }

    public String getBasePackage() {
        return scanner.value();
    }
}
