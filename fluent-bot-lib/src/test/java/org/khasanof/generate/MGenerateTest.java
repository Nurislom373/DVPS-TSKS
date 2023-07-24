package org.khasanof.generate;

import org.junit.jupiter.api.Test;
import org.khasanof.State;
import org.khasanof.core.state.InitializingStateEnum;
import org.khasanof.core.state.RegEnumImpl;
import org.khasanof.core.state.processor.HandleStateProcessor;

/**
 * @author Nurislom
 * @see org.khasanof.generate
 * @since 24.07.2023 23:36
 */
public class MGenerateTest {

    @Test
    void test() throws Exception {
        HandleStateProcessor handleStateProcessor = new HandleStateProcessor();
        handleStateProcessor.generateMAnnotation(new InitializingStateEnum() {
            @Override
            public Class<? extends Enum> getType() {
                return State.class;
            }
        });
    }

}
