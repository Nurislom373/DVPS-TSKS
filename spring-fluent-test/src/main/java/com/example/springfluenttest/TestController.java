package com.example.springfluenttest;

import lombok.RequiredArgsConstructor;
import org.khasanof.springbootstarterfluent.core.enums.MatchScope;
import org.khasanof.springbootstarterfluent.main.annotation.UpdateController;
import org.khasanof.springbootstarterfluent.main.annotation.extra.BotVariable;
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
    }

    @HandleMessage(value = "START_WITH('abs', value)", scope = MatchScope.EQUALS)
    public void testExp(Update update, AbsSender sender) throws TelegramApiException {
        String text = "start regex : " + update.getMessage().getText();
        SendMessage message = new SendMessage(update.getMessage().getChatId().toString(), text);
        sender.execute(message);
    }

    @HandleMessage(value = "START_WITH('c', value) && END_WITH('z', value)", scope = MatchScope.EXPRESSION)
    public void testExp2(Update update, AbsSender sender) throws TelegramApiException {
        String text = "testExp2 : " + update.getMessage().getText();
        SendMessage message = new SendMessage(update.getMessage().getChatId().toString(), text);
        sender.execute(message);
    }

    @HandleMessage(value = "/start {name:[a-z]} -> {look:[1-5]}", scope = MatchScope.VAR_EXPRESSION)
    public void testExp3(Update update, AbsSender sender, @BotVariable("name") String name, @BotVariable("look") String look) throws TelegramApiException {
        System.out.println("name = " + name);
        System.out.println("look = " + look);
        String text = "testExp3 : " + update.getMessage().getText();
        SendMessage message = new SendMessage(update.getMessage().getChatId().toString(), text);
        sender.execute(message);
    }

}
