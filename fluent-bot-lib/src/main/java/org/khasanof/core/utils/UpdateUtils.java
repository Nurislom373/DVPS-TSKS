package org.khasanof.core.utils;

import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

/**
 * @author Nurislom
 * @see org.khasanof.core.utils
 * @since 12.07.2023 23:45
 */
public abstract class UpdateUtils {

    public static Long getUserId(Update update) {
        if (update.hasMessage()) {
            return update.getMessage().getFrom().getId();
        } else {
            return update.getCallbackQuery().getFrom().getId();
        }
    }

    public static User getFrom(Update update) {
        if (update.hasMessage()) {
            return update.getMessage().getFrom();
        } else {
            return update.getCallbackQuery().getFrom();
        }
    }

}
