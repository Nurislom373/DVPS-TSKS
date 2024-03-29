package org.khasanof.core.collector.loader;

import com.google.common.reflect.ClassPath;
import org.khasanof.main.annotation.HandlerScan;

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

    private HandlerScan scanner;

    {
        setScanner();
    }

    // TODO write get annotation value method!
    private HandlerScan findAllClassesWithHandlerScannerClass() {
        try {
            return ClassPath.from(ClassLoader.getSystemClassLoader())
                    .getAllClasses()
                    .stream().map(ClassPath.ClassInfo::load)
                    .filter(this::hasAnnotationClassLevel)
                    .findFirst().orElseThrow(() -> new RuntimeException("HandlerScanner annotated class not found!"))
                    .getAnnotation(HandlerScan.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean hasAnnotationClassLevel(Class clazz) {
        return clazz.isAnnotationPresent(HandlerScan.class);
    }

    public void setScanner() {
        this.scanner = findAllClassesWithHandlerScannerClass();
    }

    public String getBasePackage() {
        return scanner.value();
    }
}
