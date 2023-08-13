package com.example.springfluenttest;

import org.khasanof.springbootstarterfluent.core.enums.MatchScope;
import org.khasanof.springbootstarterfluent.main.annotation.methods.HandleMessage;
import org.khasanof.springbootstarterfluent.main.annotation.methods.HandleMessages;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButtonPollType;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Nurislom
 * @see com.example.springfluenttest
 * @since 8/8/2023 9:26 PM
 */
public interface UpdateResource {

    InlineKeyboardMarkup INLINE_KEYBOARD_MARKUP = new InlineKeyboardMarkup();
    ReplyKeyboardMarkup REPLY_KEYBOARD_MARKUP = new ReplyKeyboardMarkup();

    @HandleMessage("/end")
    void simple(Update update, AbsSender sender) throws TelegramApiException;

    @HandleMessage(value = "op", scope = MatchScope.START_WITH)
    default void defualtSet(Update update, AbsSender sender) throws Exception {
        String text = "I'm default method : " + update.getMessage().getText();
        SendMessage message = new SendMessage(update.getMessage().getChatId().toString(), text);
        message.setReplyMarkup(language());
        sender.execute(message);
    }

    @HandleMessages(values = {
            @HandleMessage(value = "may", scope = MatchScope.START_WITH),
            @HandleMessage(value = "yam", scope = MatchScope.END_WITH),
            @HandleMessage(value = "boom", scope = MatchScope.EQUALS_IGNORE_CASE)
    })
    default void multiMessageHandler(Update update, AbsSender sender) throws TelegramApiException {
        SendMessage message = new SendMessage(update.getMessage().getChatId().toString(), "Hello Everyone! MultiHandler");
        message.setReplyMarkup(enterMenu());
        sender.execute(message);
    }

    static InlineKeyboardMarkup language() {
        InlineKeyboardButton uz = new InlineKeyboardButton("Uzbek");
        uz.setCallbackData("UZ");

        InlineKeyboardButton ru = new InlineKeyboardButton("Russia");
        ru.setCallbackData("RU");

        InlineKeyboardButton en = new InlineKeyboardButton("English");
        en.setCallbackData("EN");

        List<InlineKeyboardButton> row1 = new ArrayList<>();
        row1.add(en);
        row1.add(ru);

        List<InlineKeyboardButton> row2 = new ArrayList<>();
        row2.add(uz);

        INLINE_KEYBOARD_MARKUP.setKeyboard(Arrays.asList(row1, row2));
        return INLINE_KEYBOARD_MARKUP;
    }

    static ReplyKeyboardMarkup enterMenu() {
        REPLY_KEYBOARD_MARKUP.setResizeKeyboard(true);
        KeyboardRow row1 = new KeyboardRow();
        KeyboardRow row2 = new KeyboardRow();
        KeyboardButton menu = new KeyboardButton("Hello World!");
        menu.setRequestPoll(KeyboardButtonPollType.builder()
                .type("Boom")
                .build());
        KeyboardButton discount = new KeyboardButton("DEVOPS");
        KeyboardButton settings = new KeyboardButton("JECK");
        row1.add(menu);
        row2.add(discount);
        row2.add(settings);
        REPLY_KEYBOARD_MARKUP.setKeyboard(List.of(row1, row2));
        return REPLY_KEYBOARD_MARKUP;
    }

}
