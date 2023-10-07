package org.khasanof.springbootstarterfluent.core.state;

import java.util.EnumSet;

/**
 * @author Nurislom
 * @see org.khasanof.springbootstarterfluent.core.state
 * @since 10/7/2023 9:24 PM
 */
public interface StateTransmit {

    Enum getInitial();

    EnumSet getStates();

}
