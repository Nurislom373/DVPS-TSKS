package com.example.springfluenttest;

import lombok.RequiredArgsConstructor;
import org.khasanof.springbootstarterfluent.core.enums.HandleType;
import org.khasanof.springbootstarterfluent.core.enums.MatchScope;
import org.khasanof.springbootstarterfluent.core.enums.Proceed;
import org.khasanof.springbootstarterfluent.main.annotation.UpdateController;
import org.khasanof.springbootstarterfluent.main.annotation.methods.HandleAny;
import org.khasanof.springbootstarterfluent.main.annotation.methods.HandleMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

/**
 * @author Nurislom
 * @see com.example.springfluenttest
 * @since 8/8/2023 9:14 PM
 */
@UpdateController
@RequiredArgsConstructor
public class TestController {

    private final UpdateResourceImpl updateResource;

    @HandleMessage("/start")
    public void test(Update update, AbsSender sender) throws TelegramApiException {
        String text = "start : " + update.getMessage().getText();
        SendMessage message = new SendMessage(update.getMessage().getChatId().toString(), text);
        sender.execute(message);
        throw new RuntimeException("Jeck pot");
    }

    @HandleMessage(value = "[1-9]", scope = MatchScope.REGEX)
    public void testExp(Update update, AbsSender sender) throws TelegramApiException {
        String text = "start regex : " + update.getMessage().getText();
        SendMessage message = new SendMessage(update.getMessage().getChatId().toString(), text);
        sender.execute(message);
        throw new RuntimeException("Jeck pot");
    }

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


}
