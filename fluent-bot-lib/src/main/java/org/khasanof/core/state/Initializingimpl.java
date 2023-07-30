package org.khasanof.core.state;

import org.khasanof.core.enums.ClassLevelTypes;

/**
 * @author Nurislom
 * @see org.khasanof.core.state
 * @since 29.07.2023 19:28
 */
public class Initializingimpl implements InitializingStateEnum {
    @Override
    public Class<? extends Enum> getType() {
        return ClassLevelTypes.class;
    }
}
