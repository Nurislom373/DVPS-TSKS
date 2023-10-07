package org.khasanof.springbootstarterfluent.core.state.collector;

import org.khasanof.springbootstarterfluent.core.state.StateTransmit;
import org.khasanof.springbootstarterfluent.main.inferaces.ObjectContains;

import java.util.EnumSet;

/**
 * @author Nurislom
 * @see org.khasanof.springbootstarterfluent.core.state.configurer
 * @since 10/7/2023 9:21 PM
 */
public interface StateConfigCollector extends StateTransmit, ObjectContains<Enum> {

    void addInitial(Enum initial);

    void addStates(EnumSet states);

}
