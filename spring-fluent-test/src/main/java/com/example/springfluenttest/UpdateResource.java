package com.example.springfluenttest;

import org.khasanof.springbootstarterfluent.main.annotation.methods.HandleMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

/**
 * @author Nurislom
 * @see com.example.springfluenttest
 * @since 8/8/2023 9:26 PM
 */
public interface UpdateResource {

    @HandleMessage("/end")
    void simple(Update update, AbsSender sender) throws TelegramApiException;

}
