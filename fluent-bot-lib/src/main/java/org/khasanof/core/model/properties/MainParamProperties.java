package org.khasanof.core.model.properties;

import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.List;

/**
 * @author Nurislom
 * @see org.khasanof.core.model.properties
 * @since 29.07.2023 16:18
 */
public class MainParamProperties {

    public static final List<Class<?>> MAIN_PARAMS_LIST = List.of(Update.class, AbsSender.class);
    public static final Class<?>[] MAIN_PARAMS_ARRAY = new Class[]{Update.class, AbsSender.class};


}
