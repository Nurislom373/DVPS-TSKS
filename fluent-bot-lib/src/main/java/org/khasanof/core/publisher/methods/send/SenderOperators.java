package org.khasanof.core.publisher.methods.send;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;

/**
 * Author: Nurislom
 * <br/>
 * Date: 18.06.2023
 * <br/>
 * Time: 22:15
 * <br/>
 * Package: org.khasanof.core.executors.methods.send
 */
public class SenderOperators {

    public MessageSenderOperator message() {
        return new MessageSenderOperator();
    }

    public static class MessageSenderOperator {

        private SendMessage instance = new SendMessage();

        public MessageSenderOperator text(String text) {
            this.instance.setText(text);
            return this;
        }

        public MessageSenderOperator parseMode(String parseMode) {
            this.instance.setParseMode(parseMode);
            return this;
        }

        public MessageSenderOperator replyToMessageId(Integer replyToMessageId) {
            this.instance.setReplyToMessageId(replyToMessageId);
            return this;
        }

        public MessageSenderOperator replyMarkup(ReplyKeyboard replyKeyboard) {
            this.instance.setReplyMarkup(replyKeyboard);
            return this;
        }

        public SendMessage getInstance() {
            return instance;
        }
    }

}
