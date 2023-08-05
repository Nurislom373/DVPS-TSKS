package org.khasanof.springbootstarterfluent;

import org.junit.jupiter.api.Test;
import org.khasanof.springbootstarterfluent.main.annotation.extra.HandleState;
import org.khasanof.springbootstarterfluent.main.annotation.methods.HandleMessage;
import org.springframework.boot.test.context.SpringBootTest;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@SpringBootTest
class SpringBootStarterFluentApplicationTests {

    @HandleMessage("/start")
    void handleMessage(Update update, AbsSender sender) throws TelegramApiException {
        String text = "I'm handle this message : " + update.getMessage().getText();
        SendMessage message = new SendMessage(update.getMessage().getChatId().toString(), text);
        sender.execute(message);
    }

    @HandleState(value = "", proceedHandleMethods = false)
    void handleState(Update update, AbsSender sender) throws TelegramApiException {
        String text = "I'm start state : " + update.getMessage().getText();
        SendMessage message = new SendMessage(update.getMessage().getChatId().toString(), text);
        sender.execute(message);
    }

    public enum State {
        START, LOG, COMPLETE
    }

    @Test
    void contextLoads() {
    }

}
