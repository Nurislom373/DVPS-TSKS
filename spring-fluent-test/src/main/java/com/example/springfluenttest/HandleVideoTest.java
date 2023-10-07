package com.example.springfluenttest;

import org.khasanof.springbootstarterfluent.core.enums.MatchScope;
import org.khasanof.springbootstarterfluent.core.enums.MultiMatchScope;
import org.khasanof.springbootstarterfluent.core.enums.scopes.PhotoScope;
import org.khasanof.springbootstarterfluent.core.enums.scopes.VideoScope;
import org.khasanof.springbootstarterfluent.main.annotation.UpdateController;
import org.khasanof.springbootstarterfluent.main.annotation.methods.HandlePhoto;
import org.khasanof.springbootstarterfluent.main.annotation.methods.HandleVideo;
import org.khasanof.springbootstarterfluent.main.annotation.methods.HandleVideos;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.InputStream;

/**
 * @author Nurislom
 * @see com.example.springfluenttest
 * @since 8/9/2023 9:35 PM
 */
@UpdateController
public class HandleVideoTest {

    @HandleVideos(values = {
            @HandleVideo(value = ".mp4", match = MatchScope.END_WITH, scope = VideoScope.FILE_NAME),
            @HandleVideo(value = "BETWEEN(value, 1030240, 1330240)", match = MatchScope.EXPRESSION,
                    scope = VideoScope.FILE_SIZE),
            @HandleVideo(value = "(Watch", match = MatchScope.START_WITH, scope = VideoScope.FILE_NAME)
    }, match = MultiMatchScope.ALL_MATCH)
    public void handleVideo(Update update, AbsSender sender) throws TelegramApiException {
        String text = "I'm handle this video : \n" + update.getMessage().getVideo().getFileName();
        SendMessage message = new SendMessage(update.getMessage().getChatId().toString(), text);
        sender.execute(message);
    }

    @HandlePhoto(value = "old", match = MatchScope.START_WITH,scope = PhotoScope.CAPTION)
    public void handlePhoto(Update update, AbsSender sender) throws TelegramApiException {
        String text = "I'm handle this photo start with 'old' : \n" + update.getMessage().getPhoto();
        SendMessage message = new SendMessage(update.getMessage().getChatId().toString(), text);
        sender.execute(message);
    }

}
