package org.khasanof.springbootstarterfluent.core.enums;

import lombok.RequiredArgsConstructor;

/**
 * @author Nurislom
 * @see org.khasanof.core.enums
 * @since 09.07.2023 18:28
 */
@RequiredArgsConstructor
public enum ProcessType {
    BOTH,
    STATE,
    UPDATE;
    private Class<?> type;
}
