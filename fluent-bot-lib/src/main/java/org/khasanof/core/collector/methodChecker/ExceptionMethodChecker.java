package org.khasanof.core.collector.methodChecker;

import java.lang.reflect.Method;

/**
 * @author Nurislom
 * @see org.khasanof.core.collector.methodChecker
 * @since 04.07.2023 22:45
 */
public class ExceptionMethodChecker extends AbstractMethodChecker {

    // TODO will finish!
    @Override
    public boolean valid(Method method) {
        Class<?>[] parameterTypes = method.getParameterTypes();

        return false;
    }


}
