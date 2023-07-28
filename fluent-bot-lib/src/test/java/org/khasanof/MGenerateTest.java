package org.khasanof;

import com.cc.jcg.*;
import org.junit.jupiter.api.Test;
import org.khasanof.State;
import org.khasanof.core.state.processor.HandleStateProcessor;
import org.khasanof.main.annotation.process.ProcessState;

import java.lang.annotation.ElementType;

/**
 * @author Nurislom
 * @see org.khasanof.generate
 * @since 24.07.2023 23:36
 */
public class MGenerateTest {

    @Test
    void test() throws Exception {
        HandleStateProcessor handleStateProcessor = new HandleStateProcessor();
        handleStateProcessor.generateMAnnotation(State.class);
    }

    @Test
    void generate() throws Exception {
        String property1 = System.getProperty("user.dir");
        System.out.println("property1 = " + property1);
        String property2 = System.getProperty("user.home");
        System.out.println("property2 = " + property2);
        String property3 = System.getProperty("user.name");
        System.out.println("property3 = " + property3);
        String property4 = System.getProperty("java.class.path");
        System.out.println("property4 = " + property4);
    }

}
