package org.khasanof.core.state.processor;

import com.cc.jcg.*;
import lombok.SneakyThrows;
import org.khasanof.core.config.Config;
import org.khasanof.core.config.FluentConfig;
import org.khasanof.core.enums.ProcessType;
import org.khasanof.core.state.SimpleStateService;
import org.khasanof.main.annotation.process.ProcessState;
import org.khasanof.main.inferaces.state.StateService;

import java.lang.annotation.ElementType;

/**
 * @author Nurislom
 * @see org.khasanof.core.state.processor
 * @since 23.07.2023 23:20
 */
public class HandleStateProcessor {

    public void generateMAnnotation(Class<? extends Enum> enumType) throws Exception {
        String property = System.getProperty("user.dir");
        MBundle mBundle = new MBundle(property
                .concat("\\target\\generated-sources\\annotations\\"), "generate");
        MPackage mPackage = mBundle.newPackage("generate");
        MAnnotation handleStateV2 = mPackage.newAnnotationRuntime("HandleState", ElementType.METHOD);
        handleStateV2.addPreDefinitionLine("@ProcessState \n", ProcessState.class);
        handleStateV2.addMethod("value", enumType);
        handleStateV2.addMethod("proceedHandleMethods", new MTypeRefJava(boolean.class));
        handleStateV2.setCodeBlock((MCodeBlock::addEmptyLine));
        mBundle.generateCode(false);
    }
}
