package org.khasanof.springbootstarterfluent.core.enums.scopes;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author Nurislom
 * @see org.khasanof.core.enums
 * @since 06.07.2023 22:32
 */
@Getter
@RequiredArgsConstructor
public enum PhotoScope {

    FILE_SIZE(false),
    WIDTH(false),
    HEIGHT(false),
    CAPTION(true);

    private final boolean isMessageType;

}
