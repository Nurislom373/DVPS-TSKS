package org.khasanof.core.utils;

import org.khasanof.core.enums.ExecutorType;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * Author: Nurislom
 * <br/>
 * Date: 18.06.2023
 * <br/>
 * Time: 16:32
 * <br/>
 * Package: org.khasanof.core.utils
 */
public abstract class ExecutorTypeUtil {

    public static String getChatId(Update update, ExecutorType type) {
        if (type.equals(ExecutorType.MESSAGE)) {
            return update.getMessage().getChatId().toString();
        } else if (type.equals(ExecutorType.CALLBACK)) {
            return update.getCallbackQuery().getMessage().getChatId().toString();
        }
        throw new RuntimeException("Invalid Type!");
    }

}
