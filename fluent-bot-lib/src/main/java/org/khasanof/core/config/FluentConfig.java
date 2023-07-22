package org.khasanof.core.config;

import java.util.Objects;

/**
 * @author Nurislom
 * @see org.khasanof.core.config
 * @since 19.07.2023 20:31
 */
public interface FluentConfig {

    FluentConfig instance = new CommonFluentConfig();

    void start();

    Configs getConfig();

    static FluentConfig getInstance() {
        return instance;
    }

}
