package org.khasanof.core.custom.classLevelController;

import org.khasanof.main.annotation.ExceptionController;
import org.khasanof.main.annotation.UpdateController;

import java.lang.annotation.Annotation;
import java.util.*;

/**
 * @author Nurislom
 * @see org.khasanof.core.custom
 * @since 22.07.2023 10:50
 */
public class CommonClassLevelController implements ClassLevelController {

    private final Set<Class<? extends Annotation>> annotations = new HashSet<>();

    {
        annotations.addAll(List.of(UpdateController.class, ExceptionController.class));
    }

    public void add(Class<? extends Annotation> classType) {
        if (Objects.nonNull(classType)) {
            annotations.add(classType);
        }
    }

    public Set<Class<? extends Annotation>> getAnnotations() {
        return this.annotations;
    }

}
