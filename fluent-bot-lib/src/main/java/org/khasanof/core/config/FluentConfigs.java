package org.khasanof.core.config;

import org.khasanof.core.enums.ProcessType;

import java.util.ResourceBundle;

/**
 * @author Nurislom
 * @see org.khasanof.core.config
 * @since 05.07.2023 0:29
 */
public class FluentConfigs {

    public static final ResourceBundle settings = ResourceBundle.getBundle("application");

    public static ProcessType getProcessType() {
        return ProcessType.valueOf(settings.getString("bot.process.type"));
    }

}
