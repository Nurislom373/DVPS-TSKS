package org.khasanof.springbootstarterfluent.core.state;

import org.khasanof.springbootstarterfluent.main.inferaces.state.State;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;

/**
 * @author Nurislom
 * @see org.khasanof.springbootstarterfluent.core.state
 * @since 8/15/2023 9:43 PM
 */
public interface Action {

    void onReceive(Update update, AbsSender sender, State state) throws Exception;

    boolean updateHandlersProceed();

}
