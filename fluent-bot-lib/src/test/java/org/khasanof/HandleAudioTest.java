package org.khasanof;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.khasanof.core.enums.HandleType;
import org.khasanof.core.enums.MatchScope;
import org.khasanof.core.enums.MultiMatchScope;
import org.khasanof.core.enums.Proceed;
import org.khasanof.core.enums.scopes.AudioScope;
import org.khasanof.main.annotation.UpdateController;
import org.khasanof.main.annotation.methods.HandleAny;
import org.khasanof.main.annotation.methods.HandleAudio;
import org.khasanof.main.annotation.methods.HandleAudios;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

/**
 * @author Nurislom
 * @see org.khasanof
 * @since 09.07.2023 16:18
 */
@UpdateController
public class HandleAudioTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @HandleAudios(values = {
            @HandleAudio(value = "BETWEEN_UNIT(value, 1.5, 3.9, 'MB')",
                    match = MatchScope.EXPRESSION, scope = AudioScope.FILE_SIZE)
    }, match = MultiMatchScope.ANY_MATCH)
    private void audioTest(Update update, AbsSender sender) throws TelegramApiException {
        String value = update.getMessage().getAudio().getMimeType();
        String text = "I'm between_unit expression match and handle : \n" + value;
        SendMessage message = new SendMessage(update.getMessage().getChatId().toString(), text);
        sender.execute(message);
    }

    @HandleAny(type = HandleType.ANIMATION, proceed = Proceed.NOT_PROCEED)
    void animationHandleTest(Update update, AbsSender sender) throws TelegramApiException, JsonProcessingException {
        String value = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(update.getMessage().getAnimation());
        String text = "I'm handle animation : \n" + value;
        SendMessage message = new SendMessage(update.getMessage().getChatId().toString(), text);
        sender.execute(message);
    }

}
