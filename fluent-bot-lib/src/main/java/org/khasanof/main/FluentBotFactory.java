package org.khasanof.main;

import org.khasanof.main.inferaces.MainHandler;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;

import java.util.ResourceBundle;

/**
 * Author: Nurislom
 * <br/>
 * Date: 18.06.2023
 * <br/>
 * Time: 10:31
 * <br/>
 * Package: org.khasanof.main
 */
public abstract class FluentBotFactory extends TelegramLongPollingBot {

    protected final MainHandler handler;
    protected final ResourceBundle settings;

    protected FluentBotFactory() {
        this.handler = MainHandler.getInstance();
        this.settings = ResourceBundle.getBundle("application");
    }

}
