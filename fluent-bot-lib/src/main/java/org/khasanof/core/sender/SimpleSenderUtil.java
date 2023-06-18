package org.khasanof.core.sender;

import lombok.RequiredArgsConstructor;
import org.khasanof.core.enums.ExecutorType;
import org.khasanof.core.utils.ExecutorTypeUtil;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * Author: Nurislom
 * <br/>
 * Date: 18.06.2023
 * <br/>
 * Time: 15:46
 * <br/>
 * Package: org.khasanof.core.sender
 */
@RequiredArgsConstructor
public class SimpleSenderUtil {

    private final ExecutorType type;

    public SendMessage createMessage(Update update, String text) {
        return new SendMessage(ExecutorTypeUtil.getChatId(update, type), text);
    }

    public SendMessage createMessage(Update update, String text, String parseMode) {
        return new SendMessage(ExecutorTypeUtil.getChatId(update, type), null, text, parseMode,
                null, null, null, null, null, null, null);
    }

    public SendMessage builderToSendMessage(Update update, MessageBuilder builder) {
        return new SendMessage(ExecutorTypeUtil.getChatId(update, type), null, builder.getMessage(),
                builder.getParseMode(), null, null, builder.getReplyMessageId(),
                builder.getReplyKeyboard(), null, null, null);
    }

}
