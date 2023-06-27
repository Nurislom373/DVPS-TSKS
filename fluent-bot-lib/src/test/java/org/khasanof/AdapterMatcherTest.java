package org.khasanof;

import org.junit.jupiter.api.Test;
import org.khasanof.core.executors.matcher.AdapterMatcher;
import org.khasanof.core.executors.matcher.GenericMatcher;
import org.khasanof.main.annotation.methods.HandleMessages;

/**
 * @author <a href="https://github.com/Nurislom373">Nurislom</a>
 * @see org.khasanof
 * @since 24.06.2023 1:24
 */
public class AdapterMatcherTest {

    final AdapterMatcher adapterMatcher = new AdapterMatcher();

    @Test
    void test() {
        GenericMatcher matcher = adapterMatcher.findMatcher(HandleMessages.class);
        System.out.println("matcher = " + matcher);
    }

}
