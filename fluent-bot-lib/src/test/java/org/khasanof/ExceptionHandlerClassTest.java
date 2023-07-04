package org.khasanof;

import org.khasanof.core.exceptions.NotFoundException;
import org.khasanof.main.annotation.ExceptionController;
import org.khasanof.main.annotation.exception.HandleException;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

/**
 * @author Nurislom
 * @see org.khasanof
 * @since 04.07.2023 21:45
 */
@ExceptionController
public class ExceptionHandlerClassTest {

    @HandleException({RuntimeException.class})
    void test(Throwable throwable, Update update, AbsSender sender) throws TelegramApiException {
        System.out.println("throwable = " + throwable);
        System.out.println("I'm enter this method!");
        String text = "I'm handle Exception : " + throwable.getMessage();
        SendMessage message = new SendMessage(update.getMessage().getChatId().toString(), text);
        sender.execute(message);
    }


}
