package org.khasanof;

import org.khasanof.core.enums.HandleType;
import org.khasanof.core.enums.Proceed;
import org.khasanof.main.FluentStarter;
import org.khasanof.main.annotation.ExceptionController;
import org.khasanof.main.annotation.HandlerScanner;
import org.khasanof.main.annotation.UpdateController;
import org.khasanof.main.annotation.exception.HandleException;
import org.khasanof.main.annotation.methods.HandleAny;
import org.khasanof.main.annotation.methods.HandleMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@ExceptionController
@UpdateController
@HandlerScanner("org.khasanof")
public class Main {

    public static void main(String[] args) {
        FluentStarter.start();
    }

    @HandleMessage(value = "/hello")
    private void simpleSend(Update update, AbsSender sender) throws TelegramApiException {
        String text = "Hello World! 😎";
        SendMessage message = new SendMessage(update.getMessage().getChatId().toString(), text);
        sender.execute(message);
    }

    @HandleAny(type = HandleType.MESSAGE, proceed = Proceed.PROCEED)
    public void simpleHandleAny(Update update, AbsSender sender) throws TelegramApiException {
        String text = "Handle Any Message : " + update.getMessage().getText();
        SendMessage message = new SendMessage(update.getMessage().getChatId().toString(), text);
        sender.execute(message);
        throw new RuntimeException("Hi guys");
    }

    @HandleException({RuntimeException.class})
    void simpleExceptionHandler(Throwable throwable, Update update, AbsSender sender) throws TelegramApiException {
        String text = "I handle exception : message " + throwable.getMessage();
        SendMessage message = new SendMessage(update.getMessage().getChatId().toString(), text);
        sender.execute(message);
    }
}