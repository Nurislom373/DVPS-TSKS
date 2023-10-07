package org.khasanof.springbootstarterfluent.core.state;

import org.khasanof.springbootstarterfluent.core.state.configurer.StateConfigurer;

import java.util.Set;

/**
 * @author Nurislom
 * @see org.khasanof.springbootstarterfluent.core.state
 * @since 8/15/2023 9:09 PM
 */
public interface StateConfigurerAdapter<T extends Enum> {

    void configure(StateConfigurer<T> state) throws Exception;

}
