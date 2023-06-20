package org.khasanof.core.executors.matcher;

import lombok.NoArgsConstructor;
import org.khasanof.main.annotation.HandleCallback;

import java.util.Arrays;

/**
 * Author: Nurislom
 * <br/>
 * Date: 20.06.2023
 * <br/>
 * Time: 21:55
 * <br/>
 * Package: org.khasanof.core.executors.matcher
 */
@NoArgsConstructor
public class SimpleCallbackMatcher extends GenericMatcher<HandleCallback> {

    @Override
    public boolean matcher(HandleCallback handleCallback, String value) {
        return Arrays.asList(handleCallback.values())
                .contains(value);
    }

}
