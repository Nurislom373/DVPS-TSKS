package com.example.springfluenttest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.khasanof.springbootstarterfluent.core.enums.HandleType;
import org.khasanof.springbootstarterfluent.core.enums.Proceed;
import org.khasanof.springbootstarterfluent.main.annotation.UpdateController;
import org.khasanof.springbootstarterfluent.main.annotation.methods.HandleAny;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

/**
 * @author Nurislom
 * @see com.example.springfluenttest
 * @since 8/9/2023 9:08 PM
 */
@UpdateController
public class ResourceSimple implements UpdateResource {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void simple(Update update, AbsSender sender) throws TelegramApiException {
        String text = "I'm simple method : " + update.getMessage().getText();
        SendMessage message = new SendMessage(update.getMessage().getChatId().toString(), text);
        sender.execute(message);
    }

    @HandleAny(type = HandleType.AUDIO, proceed = Proceed.PROCEED)
    private void handleAnyCallbacks(Update update, AbsSender sender) throws TelegramApiException, JsonProcessingException {
        String value = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(update.getMessage().getAudio());
        String text = "I'm handle this audio : \n" + value;
        SendMessage message = new SendMessage(update.getMessage().getChatId().toString(), text);
        sender.execute(message);
    }

}
