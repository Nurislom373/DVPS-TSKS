package com.example.springfluenttest;

import org.khasanof.springbootstarterfluent.core.utils.UpdateUtils;
import org.khasanof.springbootstarterfluent.main.annotation.ExceptionController;
import org.khasanof.springbootstarterfluent.main.annotation.exception.HandleException;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

/**
 * @author Nurislom
 * @see com.example.springfluenttest
 * @since 8/8/2023 9:27 PM
 */
@ExceptionController
public class UpdateResourceImpl  {

    @HandleException({RuntimeException.class})
    void test(Update update, Throwable throwable, AbsSender sender) throws TelegramApiException {
        System.out.println("throwable = " + throwable);
        System.out.println("I'm enter this method!");
        String text = "I'm handle Exception : " + throwable.getMessage();
        SendMessage message = new SendMessage(UpdateUtils.getUserId(update).toString(), text);
        sender.execute(message);
    }

}
