package org.khasanof.core.executors.matcher.impls;

import org.khasanof.core.enums.MatchScope;
import org.khasanof.core.executors.matcher.GenericMatcher;
import org.khasanof.main.annotation.methods.HandleMessage;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.regex.Pattern;

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

    @Override
    public boolean matcher(HandleMessage handleMessage, String value) {
        return matchFunctions.get(Map.entry(handleMessage.scope(), getScopeType(value, handleMessage.scope())))
                .apply(handleMessage.value(), value);
    }

    @Override
    public Class<HandleMessage> getType() {
        return HandleMessage.class;
    }

}
