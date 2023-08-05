package org.khasanof.springbootstarterfluent.core.config;

import org.khasanof.springbootstarterfluent.core.enums.ProcessType;

/**
 * @author Nurislom
 * @see org.khasanof.core.config
 * @since 19.07.2023 20:28
 */
public interface Config {

    void runnable();

    ProcessType processType();

}
