package org.khasanof.core.sender;

import org.khasanof.core.enums.ExecutorType;
import org.khasanof.main.inferaces.sender.Sender;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.MessageEntity;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.Serializable;

/**
 * Author: Nurislom
 * <br/>
 * Date: 18.06.2023
 * <br/>
 * Time: 15:43
 * <br/>
 * Package: org.khasanof.core.sender
 */
public class SimpleSender extends AbstractSender implements Sender {

    public SimpleSender(Update update, AbsSender absSender, ExecutorType type) {
        super(type, update, absSender, new SimpleSenderUtil(type));
    }

    @Override
    public void execute(MessageBuilder messageBuilder) {
        try {
            sender.execute(util.builderToSendMessage(update, messageBuilder));
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void execute(String message) {
        try {
            sender.execute(util.createMessage(update, message));
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void execute(String message, String parseMode) {
        try {
            sender.execute(util.createMessage(update, message, parseMode));
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void execute(AnswerCallbackQuery query) {
        try {
            query.setCallbackQueryId(update.getCallbackQuery().getId());
            sender.execute(query);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void execute(SendMessage message) {
        try {
            message.setChatId(update.getMessage().getChatId());
            sender.execute(message);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void execute(DeleteMessage message) {
        try {
            message.setChatId(update.getMessage().getChatId());
            sender.execute(message);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void execute(EditMessageText message) {
        try {
            message.setChatId(update.getMessage().getChatId());
            message.setMessageId(update.getMessage().getMessageId());
            sender.execute(message);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}
