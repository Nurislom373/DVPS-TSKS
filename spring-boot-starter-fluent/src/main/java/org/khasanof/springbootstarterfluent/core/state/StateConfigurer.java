package org.khasanof.springbootstarterfluent.core.state;

import java.util.Set;

/**
 * @author Nurislom
 * @see org.khasanof.springbootstarterfluent.core.state
 * @since 8/15/2023 9:09 PM
 */
public interface StateConfigurer {

    Set<Class<? extends Enum>> stateEnums();

}
