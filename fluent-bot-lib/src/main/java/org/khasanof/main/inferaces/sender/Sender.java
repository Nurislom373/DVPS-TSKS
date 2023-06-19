package org.khasanof.main.inferaces.sender;

import org.khasanof.core.sender.MessageBuilder;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;

/**
 * Author: Nurislom
 * <br/>
 * Date: 18.06.2023
 * <br/>
 * Time: 13:04
 * <br/>
 * Package: org.khasanof.main.inferaces.sender
 */
public interface Sender extends SendMethods {

    void execute(MessageBuilder messageBuilder);

    void execute(String message);

    void execute(String message, String parseMode);

    void execute(AnswerCallbackQuery query);

    void execute(DeleteMessage message);

    void execute(EditMessageText message);

}
