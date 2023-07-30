package org.khasanof;

import org.junit.jupiter.api.Test;
import org.khasanof.core.enums.MatchScope;
import org.khasanof.main.annotation.UpdateController;
import org.khasanof.main.annotation.methods.inline.HandleInlineQuery;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

/**
 * @author Nurislom
 * @see org.khasanof
 * @since 24.07.2023 23:36
 */
@UpdateController
public class MGenerateTest {

    @HandleInlineQuery(value = {"DEVOPS", "RU", "EN"}, match = MatchScope.EQUALS)
    void inlineQuery(Update update, AbsSender sender) throws TelegramApiException {
        String text = "I Handle Inline Query New Feature!";
        SendMessage sendMessage = new SendMessage(update.getMessage().getChatId().toString(), text);
        sender.execute(sendMessage);
    }

}
