package com.example.springfluenttest.state;

import com.example.springfluenttest.State;
import org.khasanof.springbootstarterfluent.core.state.StateActions;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

/**
 * @author Nurislom
 * @see com.example.springfluenttest
 * @since 8/15/2023 9:30 PM
 */
@Component
public class StartState implements StateActions<State> {

    @Override
    public State state() {
        return State.START;
    }

    @Override
    public void onReceive(Update update, AbsSender sender, org.khasanof.springbootstarterfluent.main.inferaces.state.State state) throws TelegramApiException {
        String text = "I'm simple method : " + update.getMessage().getText();
        SendMessage message = new SendMessage(update.getMessage().getChatId().toString(), text);
        sender.execute(message);
        state.nextState();
    }
}
