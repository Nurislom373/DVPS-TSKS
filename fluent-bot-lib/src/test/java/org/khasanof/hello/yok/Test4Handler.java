package org.khasanof.hello.yok;

import org.khasanof.core.enums.MessageScope;
import org.khasanof.main.annotation.HandleMessage;
import org.khasanof.main.annotation.HandleUpdate;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

/**
 * Author: Nurislom
 * <br/>
 * Date: 22.06.2023
 * <br/>
 * Time: 23:14
 * <br/>
 * Package: org.khasanof.hello.yok
 */
@HandleUpdate
public class Test4Handler {

    @HandleMessage(value = "jeck", scope = MessageScope.START_WITH)
    void jectMethod(Update update, AbsSender sender) throws TelegramApiException {
        SendMessage message = new SendMessage(update.getMessage().getChatId().toString(), "Hello Everyone! : ");
        sender.execute(message);
    }

}
