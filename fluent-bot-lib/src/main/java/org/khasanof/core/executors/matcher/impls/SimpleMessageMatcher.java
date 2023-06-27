package org.khasanof.core.executors.matcher.impls;

import org.khasanof.core.enums.MessageScope;
import org.khasanof.core.executors.matcher.GenericMatcher;
import org.khasanof.main.annotation.methods.HandleMessage;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

/**
 * Author: Nurislom
 * <br/>
 * Date: 20.06.2023
 * <br/>
 * Time: 21:36
 * <br/>
 * Package: org.khasanof.core.executors.matcher
 */
public class SimpleMessageMatcher extends GenericMatcher<HandleMessage, String> {

    private final Map<MessageScope, BiFunction<HandleMessage, String, Boolean>> functionMap = new HashMap<>();

    {
      setFunctionMap();
    }

    @Override
    public boolean matcher(HandleMessage handleMessage, String value) {
        return functionMap.get(handleMessage.scope())
                .apply(handleMessage, value);
    }

    @Override
    public Class<HandleMessage> getType() {
        return HandleMessage.class;
    }

    private void setFunctionMap() {
        functionMap.put(MessageScope.EQUALS,
                ((handleMessage, value) -> value.equals(handleMessage.value())));
        functionMap.put(MessageScope.CONTAINS,
                ((handleMessage, value) -> value.contains(handleMessage.value())));
        functionMap.put(MessageScope.START_WITH,
                ((handleMessage, value) -> value.startsWith(handleMessage.value())));
        functionMap.put(MessageScope.END_WITH,
                ((handleMessage, value) -> value.endsWith(handleMessage.value())));
        functionMap.put(MessageScope.EQUALS_IGNORE_CASE,
                ((handleMessage, value) -> value.equalsIgnoreCase(handleMessage.value())));
    }

}
