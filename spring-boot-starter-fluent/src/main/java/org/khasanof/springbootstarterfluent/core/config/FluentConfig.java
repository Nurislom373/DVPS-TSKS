package org.khasanof.springbootstarterfluent.core.config;

/**
 * @author Nurislom
 * @see org.khasanof.core.config
 * @since 19.07.2023 20:31
 */
public interface FluentConfig {

    FluentConfig instance = new CommonFluentConfig();

    Configs getConfig();

    static FluentConfig getInstance() {
        return instance;
    }

}
