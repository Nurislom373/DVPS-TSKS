package org.khasanof.core.sender;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;

/**
 * Author: Nurislom
 * <br/>
 * Date: 18.06.2023
 * <br/>
 * Time: 15:53
 * <br/>
 * Package: org.khasanof.core.sender
 */
@Getter
public class MessageBuilder {

    private String parseMode;
    private String message;
    private Integer replyMessageId;
    private ReplyKeyboard replyKeyboard;

    public MessageBuilder parseMode(@NonNull String parseMode) {
        this.parseMode = parseMode;
        return this;
    }

    public MessageBuilder message(@NonNull String message) {
        this.message = message;
        return this;
    }

    public MessageBuilder replyMessageId(@NonNull Integer replyMessageId) {
        this.replyMessageId = replyMessageId;
        return this;
    }

    public MessageBuilder replyKeyboard(@NonNull ReplyKeyboard replyKeyboard) {
        this.replyKeyboard = replyKeyboard;
        return this;
    }

}
