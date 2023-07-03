package org.khasanof.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.khasanof.main.annotation.methods.*;

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
@AllArgsConstructor
@RequiredArgsConstructor
public enum HandleClasses {

    HANDLE_ANY(HandleAny.class, false),

    HANDLE_CALLBACKS(HandleCallbacks.class, false),
    HANDLE_MESSAGES(HandleMessages.class, false),
    HANDLE_DOCUMENTS(HandleDocuments.class, false),

    HANDLE_CALLBACK(HandleCallback.class, true, HandleClasses.HANDLE_CALLBACKS),
    HANDLE_MESSAGE(HandleMessage.class, true, HandleClasses.HANDLE_MESSAGES),
    HANDLE_DOCUMENT(HandleDocument.class, true, HandleClasses.HANDLE_DOCUMENTS);

    private final Class<? extends Annotation> type;
    private final boolean hasSubType;
    private HandleClasses subHandleClasses;

    public static Set<Class<? extends Annotation>> getAllAnnotations() {
        return Arrays.stream(values()).map(an -> an.type)
                .collect(Collectors.toSet());
    }

    public static HandleClasses getHandleWithType(Class<? extends Annotation> annotation) {
        return Arrays.stream(values()).filter(handle -> handle.type.equals(annotation))
                .findFirst().orElseThrow(() -> new RuntimeException("Match type not found!"));
    }

}
