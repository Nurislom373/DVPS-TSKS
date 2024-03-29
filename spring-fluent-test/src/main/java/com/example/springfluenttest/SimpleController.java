package com.example.springfluenttest;

import lombok.extern.slf4j.Slf4j;
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
 * @since 9/30/2023 11:26 PM
 */
@Slf4j
@UpdateController
public class SimpleController {

    @HandleAny(proceed = Proceed.PROCEED)
    void handleAnyMessage(Update update, AbsSender sender) throws TelegramApiException {
        log.info("Handle Any Message!");
        String chatId = update.getMessage().getChatId().toString();
        SendMessage message = new SendMessage(chatId, "Handler Any Message😎");
        sender.execute(message);
    }

    @HandleMessage(value = "abs", scope = MatchScope.START_WITH)
    void startWithAbsHandler(Update update, AbsSender sender) throws TelegramApiException {
        String chatId = update.getMessage().getChatId().toString();
        String text = update.getMessage().getText();
        log.info("Handle Start With 'abs' : {}", text);
        SendMessage message = new SendMessage(chatId, "Start With 'abs' : " + text);
        sender.execute(message);
    }

}
