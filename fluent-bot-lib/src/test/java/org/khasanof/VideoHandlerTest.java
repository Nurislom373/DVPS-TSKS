package org.khasanof;

import org.khasanof.core.enums.MatchScope;
import org.khasanof.core.enums.scopes.VideoScope;
import org.khasanof.main.annotation.UpdateController;
import org.khasanof.main.annotation.methods.HandleVideo;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

/**
 * @author Nurislom
 * @see org.khasanof
 * @since 07.07.2023 21:53
 */
public class VideoHandlerTest implements VideoHandler {

    @Override
    public void testHandle(Update update, AbsSender sender) throws TelegramApiException {
        String text = "I'm handle this video : \n" + update.getMessage().getVideo().getFileName();
        SendMessage message = new SendMessage(update.getMessage().getChatId().toString(), text);
        sender.execute(message);
    }

}
