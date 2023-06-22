package org.khasanof.hello.subhello.boom;

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
 * Time: 23:11
 * <br/>
 * Package: org.khasanof.hello.subhello.boom
 */
@HandleUpdate
public class Test3Handler {

    @HandleMessage(value = "/menu")
    private void sendMenu(AbsSender sender, Update update) throws TelegramApiException {
        SendMessage message = new SendMessage(update.getMessage().getChatId().toString(), "This is my menu!");
        sender.execute(message);
    }

}
