package org.khasanof.springbootstarterfluent.main;

import org.khasanof.springbootstarterfluent.core.config.ApplicationProperties;
import org.khasanof.springbootstarterfluent.main.inferaces.MainHandler;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * Author: Nurislom
 * <br/>
 * Date: 18.06.2023
 * <br/>
 * Time: 10:30
 * <br/>
 * Package: org.khasanof.main
 */
@Service
public class FluentBot extends FluentBotFactory {

    public FluentBot(MainHandler handler, ApplicationProperties properties) {
        super(handler, properties.getBot());
    }

    @Override
    public String getBotUsername() {
        return bot.getUsername();
    }

    @Override
    public String getBotToken() {
        return bot.getToken();
    }

    @Override
    public void onUpdateReceived(Update update) {
        handler.process(update);
    }
}
