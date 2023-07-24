package org.khasanof.core.state.processor;

import com.cc.jcg.*;
import org.khasanof.core.config.Config;
import org.khasanof.core.enums.ProcessType;
import org.khasanof.core.exceptions.NotImplementedException;
import org.khasanof.core.state.InitializingStateEnum;
import org.khasanof.core.utils.ReflectionUtils;

import java.lang.annotation.ElementType;
import java.util.List;
import java.util.Objects;

/**
 * @author Nurislom
 * @see org.khasanof.core.state.processor
 * @since 23.07.2023 23:20
 */

public class HandleStateProcessor implements Config {

    public void generateMAnnotation(InitializingStateEnum initializingStateEnum) {
        try {
            MBundle mBundle = new MBundle("target/generated-sources/annotations", "generate");
            MPackage mPackage = mBundle.newPackage("generate");
            MAnnotation handleStateV2 = mPackage.newAnnotationRuntime("HandleStateV2", ElementType.METHOD);
            handleStateV2.addPreDefinitionLine("@GenerateAnnotation \n", GenerateAnnotation.class);
            handleStateV2.addMethod("value", initializingStateEnum.getType());
            handleStateV2.addMethod("proceedHandleMethods", new MTypeRefJava(boolean.class));
            handleStateV2.setCodeBlock((MCodeBlock::addEmptyLine));
            mBundle.generateCode(false);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private InitializingStateEnum getType() {
        List<InitializingStateEnum> list = ReflectionUtils.getSubTypesObject(InitializingStateEnum.class);
        if (Objects.isNull(list)) {
            throw new NotImplementedException(InitializingStateEnum.NAME + " interface implement not found!");
        } else if (list.size() != 1) {
            throw new RuntimeException(InitializingStateEnum.NAME + " interface must be one implement!");
        }
        return list.get(0);
    }

    @Override
    public void runnable() {
        generateMAnnotation(getType());
    }

    @Override
    public ProcessType processType() {
        return ProcessType.STATE;
    }
}
