package org.khasanof;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

/**
 * @author Nurislom
 * @see org.khasanof
 * @since 26.06.2023 12:19
 */
public class HelloInterImpl implements HelloInterfaceHandler {

    @Override
    public void interfaceMethod(Update UPDATE, AbsSender sender) throws TelegramApiException {
        SendMessage message = new SendMessage(UPDATE.getMessage().getChatId().toString(), "Hello, Abror aka");
        sender.execute(message);
    }

}
