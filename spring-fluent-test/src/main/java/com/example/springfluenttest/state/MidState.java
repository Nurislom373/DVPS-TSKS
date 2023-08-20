package com.example.springfluenttest.state;

import com.example.springfluenttest.State;
import org.khasanof.springbootstarterfluent.core.state.StateActions;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;

/**
 * @author Nurislom
 * @see com.example.springfluenttest.state
 * @since 8/20/2023 7:23 PM
 */
@Component
public class MidState implements StateActions<State> {

    @Override
    public void onReceive(Update update, AbsSender sender, org.khasanof.springbootstarterfluent.main.inferaces.state.State state) throws Exception {
        String text = "I'm mid state method : " + update.getMessage().getText();
        SendMessage message = new SendMessage(update.getMessage().getChatId().toString(), text);
        sender.execute(message);
    }

    @Override
    public boolean updateHandlersProceed() {
        return true;
    }

    @Override
    public State state() {
        return State.MID;
    }
}
