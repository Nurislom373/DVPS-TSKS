package org.khasanof;

import org.khasanof.core.exceptions.InvalidParamsException;
import org.khasanof.core.state.SimpleStateService;
import org.khasanof.core.utils.UpdateUtils;
import org.khasanof.main.inferaces.state.State;
import org.khasanof.main.annotation.StateController;
import org.khasanof.main.annotation.extra.HandleState;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

/**
 * @author Nurislom
 * @see org.khasanof
 * @since 09.07.2023 19:08
 */
@StateController
public class HandlStateController {

    void startState(Update update, AbsSender sender, State state) throws TelegramApiException {
        String text = "I'm Start STATE";
        SendMessage message = new SendMessage(UpdateUtils.getUserId(update).toString(), text);
        sender.execute(message);
    }

    void logState(Update update, AbsSender sender, State state) throws TelegramApiException {
        String text = "I'm LOG state";
        SendMessage message = new SendMessage(UpdateUtils.getUserId(update).toString(), text);
        sender.execute(message);
    }


}
