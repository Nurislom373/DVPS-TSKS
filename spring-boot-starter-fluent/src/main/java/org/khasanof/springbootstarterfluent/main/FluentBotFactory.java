package org.khasanof.springbootstarterfluent.main;

import lombok.RequiredArgsConstructor;
import org.khasanof.springbootstarterfluent.core.config.ApplicationProperties;
import org.khasanof.springbootstarterfluent.main.inferaces.MainHandler;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;

/**
 * Author: Nurislom
 * <br/>
 * Date: 18.06.2023
 * <br/>
 * Time: 10:31
 * <br/>
 * Package: org.khasanof.main
 */
@RequiredArgsConstructor
public abstract class FluentBotFactory extends TelegramLongPollingBot {

    protected final MainHandler handler;
    protected final ApplicationProperties.Bot bot;

}
