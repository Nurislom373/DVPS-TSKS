package org.khasanof.core.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.checkerframework.checker.units.qual.A;
import org.khasanof.main.annotation.*;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Author: Nurislom
 * <br/>
 * Date: 20.06.2023
 * <br/>
 * Time: 22:35
 * <br/>
 * Package: org.khasanof.core.enums
 */
@Getter
@RequiredArgsConstructor
public enum HandleClasses {

    HANDLE_ANY(HandleAny.class),
    HANDLE_CALLBACKS(HandleCallbacks.class),
    HANDLE_MESSAGES(HandleMessages.class),
    HANDLE_CALLBACK(HandleCallback.class),
    HANDLE_MESSAGE(HandleMessage.class);

    private final Class<? extends Annotation> type;

    public static Set<Class<? extends Annotation>> getAllAnnotations() {
        return Arrays.stream(values()).map(an -> an.type)
                .collect(Collectors.toSet());
    }

    public static HandleClasses getHandleWithType(Class<? extends Annotation> annotation) {
        return Arrays.stream(values()).filter(handle -> handle.type.equals(annotation))
                .findFirst().orElseThrow(() -> new RuntimeException("Match type not found!"));
    }

}
