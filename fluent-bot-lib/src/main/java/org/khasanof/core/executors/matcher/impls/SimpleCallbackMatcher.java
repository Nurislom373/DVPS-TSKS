package org.khasanof.core.executors.matcher.impls;

import org.khasanof.core.executors.matcher.GenericMatcher;
import org.khasanof.main.annotation.methods.HandleCallback;

import java.util.Arrays;
import java.util.Map;

/**
 * Author: Nurislom
 * <br/>
 * Date: 20.06.2023
 * <br/>
 * Time: 21:55
 * <br/>
 * Package: org.khasanof.core.executors.matcher
 */
public class SimpleCallbackMatcher extends GenericMatcher<HandleCallback, String> {

    @Override
    public boolean matcher(HandleCallback handleCallback, String value) {
        return Arrays.stream(handleCallback.values())
                .anyMatch(any -> matchFunctions.get(Map.entry(handleCallback.scope(),
                        getScopeType(value, handleCallback.scope()))).apply(any, value));
    }

    @Override
    public Class<HandleCallback> getType() {
        return HandleCallback.class;
    }

}
