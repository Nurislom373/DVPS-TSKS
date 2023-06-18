package org.khasanof.main;

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
public class FluentBot extends FluentBotFactory {

    private static FluentBot instance;

    public static FluentBot getInstance() {
        if (instance == null) {
            instance = new FluentBot();
        }
        return instance;
    }

    @Override
    public String getBotUsername() {
        return settings.getString("bot.username");
    }

    @Override
    public String getBotToken() {
        return settings.getString("bot.token");
    }

    @Override
    public void onUpdateReceived(Update update) {
        handler.process(update);
    }
}
