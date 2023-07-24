package org.khasanof.core.executors.expression;

import com.ezylang.evalex.functions.FunctionIfc;
import org.khasanof.core.utils.ReflectionUtils;
import org.reflections.Reflections;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Nurislom
 * @see org.khasanof.core.executors.expression
 * @since 05.07.2023 22:32
 */
public class EvalFunctionsAdapter {

    private final Reflections reflections = ReflectionUtils.getReflections(true);

    public Map.Entry<String, FunctionIfc> getFunctions() {
        Set<Class<? extends NameFunction>> subTypesOf = reflections.getSubTypesOf(NameFunction.class);
        return null;
    }

}
