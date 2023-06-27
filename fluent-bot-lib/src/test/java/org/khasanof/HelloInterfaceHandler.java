package org.khasanof;

import org.khasanof.core.enums.MessageScope;
import org.khasanof.main.annotation.HandleUpdate;
import org.khasanof.main.annotation.methods.HandleMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

/**
 * @author Nurislom
 * @see org.khasanof
 * @since 26.06.2023 12:17
 */
@HandleUpdate
public interface HelloInterfaceHandler {

    @HandleMessage(value = "/inter", scope = MessageScope.EQUALS)
    void interfaceMethod(Update UPDATE, AbsSender sender) throws TelegramApiException;

}
