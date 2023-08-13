package org.khasanof.springbootstarterfluent.core.enums.additional;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.InputStream;
import java.util.HashMap;

/**
 * @author Nurislom
 * @see org.khasanof.springbootstarterfluent.core.enums.additional
 * @since 8/13/2023 6:53 PM
 */
@Getter
@RequiredArgsConstructor
public enum AdditionalParamType {

    VAR_EXPRESSION_PARAM(HashMap.class),
    PROCESS_FILE_PARAM(InputStream.class);

    private final Class<?> parmaType;
}
