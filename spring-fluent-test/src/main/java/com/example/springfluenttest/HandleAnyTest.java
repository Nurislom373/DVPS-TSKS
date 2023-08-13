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
 * @since 8/9/2023 9:44 PM
 */
@UpdateController
public class HandleAnyTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @HandleAny(type = HandleType.MESSAGE, proceed = Proceed.PROCEED)
    private void handleAnyMessages(Update update, AbsSender sender) throws TelegramApiException {
        String text = "I'm handle any this message : " + update.getMessage().getText();
        SendMessage message = new SendMessage(update.getMessage().getChatId().toString(), text);
        sender.execute(message);
    }

    @HandleAny(type = HandleType.MESSAGE, proceed = Proceed.PROCEED)
    private void handleAnyMessagesV2(Update update, AbsSender sender) throws TelegramApiException {
        String text = "I'm handle any v2 this message : " + update.getMessage().getText();
        SendMessage message = new SendMessage(update.getMessage().getChatId().toString(), text);
        sender.execute(message);
    }

    @HandleAny(type = HandleType.STICKER, proceed = Proceed.NOT_PROCEED)
    private void handleAnyStickers(Update update, AbsSender sender) throws TelegramApiException, JsonProcessingException {
        String value = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(update.getMessage().getSticker());
        String text = "I'm handle this sticker : \n" + value;
        SendMessage message = new SendMessage(update.getMessage().getChatId().toString(), text);
        sender.execute(message);
    }

    @HandleAny(type = HandleType.PHOTO, proceed = Proceed.NOT_PROCEED)
    private void handleAnyPhoto(Update update, AbsSender sender) throws TelegramApiException, JsonProcessingException {
        String value = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(update.getMessage().getPhoto());
        String text = "I'm handle this photo : \n" + value;
        SendMessage message = new SendMessage(update.getMessage().getChatId().toString(), text);
        sender.execute(message);
    }

    @HandleAny(type = HandleType.DOCUMENT, proceed = Proceed.PROCEED)
    private void handleAnyDocument(Update update, AbsSender sender) throws TelegramApiException, JsonProcessingException {
        String value = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(update.getMessage().getDocument());
        String text = "I'm handle this document : \n" + value;
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

    @HandleAny(type = HandleType.VIDEO_NOTE, proceed = Proceed.PROCEED)
    private void handleAnyVideoNote(Update update, AbsSender sender) throws TelegramApiException, JsonProcessingException {
        String value = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(update.getMessage().getVideoNote());
        String text = "I'm handle this video note : \n" + value;
        SendMessage message = new SendMessage(update.getMessage().getChatId().toString(), text);
        sender.execute(message);
    }

    @HandleAny(type = HandleType.VIDEO, proceed = Proceed.PROCEED)
    private void handleAnyPhotos(Update update, AbsSender sender) throws TelegramApiException, JsonProcessingException {
        String value = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(update.getMessage().getVideo());
        String text = "I'm handle this video : \n" + value;
        SendMessage message = new SendMessage(update.getMessage().getChatId().toString(), text);
        sender.execute(message);
    }

}
