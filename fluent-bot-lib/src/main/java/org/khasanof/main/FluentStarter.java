package org.khasanof.main;

import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

/**
 * Author: Nurislom
 * <br/>
 * Date: 18.06.2023
 * <br/>
 * Time: 11:21
 * <br/>
 * Package: org.khasanof.main
 */
public abstract class FluentStarter {

    public static void start() {
        try {
            var registry = new TelegramBotsApi(DefaultBotSession.class);
            var bot = FluentBot.getInstance();
            registry.registerBot(bot);
        } catch ( TelegramApiException e ) {
            throw new RuntimeException(e);
        }
    }

}
