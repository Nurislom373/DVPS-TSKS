package org.khasanof;

import org.khasanof.main.annotation.UpdateController;
import org.khasanof.main.annotation.methods.HandleMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

/**
 * @author Nurislom
 * @see org.khasanof
 * @since 04.07.2023 23:17
 */
@UpdateController
public abstract class AbstractController {

    @HandleMessage(value = "/abstract")
    public abstract void abstractMethod(Update update, AbsSender sender) throws TelegramApiException;


}
