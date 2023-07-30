package org.khasanof;

import org.khasanof.core.enums.MatchScope;
import org.khasanof.core.enums.MultiMatchScope;
import org.khasanof.core.enums.scopes.VideoScope;
import org.khasanof.main.annotation.UpdateController;
import org.khasanof.main.annotation.methods.HandleVideo;
import org.khasanof.main.annotation.methods.HandleVideos;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.InputStream;

/**
 * @author Nurislom
 * @see org.khasanof
 * @since 07.07.2023 21:55
 */
@UpdateController
public interface VideoHandler {

    @HandleVideos(values = {
            @HandleVideo(value = ".mp4", match = MatchScope.END_WITH, scope = VideoScope.FILE_NAME),
            @HandleVideo(value = "BETWEEN(value, 1030240, 1330240)", match = MatchScope.EXPRESSION,
                    scope = VideoScope.FILE_SIZE),
            @HandleVideo(value = "(Watch", match = MatchScope.START_WITH, scope = VideoScope.FILE_NAME)
    }, match = MultiMatchScope.ALL_MATCH)
    void testHandle(Update update, AbsSender sender) throws TelegramApiException;

}
