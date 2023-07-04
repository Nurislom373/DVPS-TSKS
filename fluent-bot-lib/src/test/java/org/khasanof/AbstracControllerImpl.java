package org.khasanof;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

/**
 * @author Nurislom
 * @see org.khasanof
 * @since 04.07.2023 23:20
 */
public class AbstracControllerImpl extends AbstractController {

    @Override
    public void abstractMethod(Update update, AbsSender sender) throws TelegramApiException {
        String text = "I'm Abstract Class Impl \uD83E\uDD23 : " + update.getMessage().getText();
        SendMessage message = new SendMessage(update.getMessage().getChatId().toString(), text);
        sender.execute(message);
    }
}
