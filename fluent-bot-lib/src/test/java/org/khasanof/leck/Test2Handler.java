package org.khasanof.leck;

import org.khasanof.core.enums.MatchScope;
import org.khasanof.main.annotation.methods.HandleMessage;
import org.khasanof.main.annotation.UpdateController;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.send.SendSticker;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;

/**
 * Author: Nurislom
 * <br/>
 * Date: 21.06.2023
 * <br/>
 * Time: 23:25
 * <br/>
 * Package: org.khasanof
 */
@UpdateController
public class Test2Handler {

    @HandleMessage(value = "hello", scope = MatchScope.CONTAINS)
    void handleAny(Update update, AbsSender sender) throws TelegramApiException {
        for (int i = 0; i < 5; i++) {
            SendMessage message = new SendMessage(update.getMessage().getChatId().toString(), "Hello Everyone! : " + i);
            sender.execute(message);
        }

        File file = new File("C:\\Nurislom\\Projects\\DVPS-TSKS\\fluent-bot-lib\\src\\main\\resources\\carbon.png");
        SendPhoto sendPhoto = new SendPhoto(update.getMessage().getChatId().toString(), new InputFile(file));

        SendSticker sendSticker = new SendSticker(update.getMessage().getChatId().toString(), new InputFile(file));

        sender.execute(sendPhoto);
        sender.execute(sendSticker);
    }

}
