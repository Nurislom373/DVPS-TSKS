package org.khasanof;

import org.khasanof.core.enums.HandleType;
import org.khasanof.core.enums.MessageScope;
import org.khasanof.main.annotation.HandleAny;
import org.khasanof.main.annotation.HandleMessage;
import org.khasanof.main.annotation.HandleUpdate;
import org.telegram.telegrambots.meta.api.methods.invoices.SendInvoice;
import org.telegram.telegrambots.meta.api.methods.send.SendAnimation;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.send.SendSticker;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.payments.LabeledPrice;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

/**
 * Author: Nurislom
 * <br/>
 * Date: 21.06.2023
 * <br/>
 * Time: 23:25
 * <br/>
 * Package: org.khasanof
 */
@HandleUpdate
public class Test2Handler {

    @HandleMessage(value = "hello", scope = MessageScope.CONTAINS)
    void handleAny(Update update, AbsSender sender) throws TelegramApiException {
        SendMessage message = new SendMessage(update.getMessage().getChatId().toString(), "Hello Everyone!");
        sender.execute(message);

        File file = new File("C:\\Nurislom\\Projects\\DVPS-TSKS\\fluent-bot-lib\\src\\main\\resources\\carbon.png");
        SendPhoto sendPhoto = new SendPhoto(update.getMessage().getChatId().toString(), new InputFile(file));

        SendSticker sendSticker = new SendSticker(update.getMessage().getChatId().toString(), new InputFile(file));

        sender.execute(sendPhoto);
        sender.execute(sendSticker);
    }

}
