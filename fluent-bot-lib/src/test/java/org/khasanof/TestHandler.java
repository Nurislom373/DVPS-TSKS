package org.khasanof;

import lombok.SneakyThrows;
import org.khasanof.core.enums.HandleType;
import org.khasanof.core.enums.MessageScope;
import org.khasanof.core.publisher.Publisher;
import org.khasanof.core.sender.MessageBuilder;
import org.khasanof.main.annotation.*;
import org.khasanof.main.inferaces.sender.Sender;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
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
 * Author: Nurislom
 * <br/>
 * Date: 18.06.2023
 * <br/>
 * Time: 14:18
 * <br/>
 * Package: org.khasanof
 */
@HandleUpdate
public class TestHandler {

    private static final InlineKeyboardMarkup INLINE_KEYBOARD_MARKUP = new InlineKeyboardMarkup();
    private static final ReplyKeyboardMarkup REPLY_KEYBOARD_MARKUP = new ReplyKeyboardMarkup();

    @HandleMessage(value = "/start")
    private void start(Update update, AbsSender sender) throws TelegramApiException {
        String text = "Hello World!";
        SendMessage message = new SendMessage(update.getMessage().getChatId().toString(), text);
        message.setReplyMarkup(language());
        sender.execute(message);
    }

    @HandleMessages(values = {
            @HandleMessage(value = "may", scope = MessageScope.START_WITH),
            @HandleMessage(value = "yam", scope = MessageScope.END_WITH)
    })
    void multiMessageHandler(Update update, AbsSender sender) throws TelegramApiException {
        SendMessage message = new SendMessage(update.getMessage().getChatId().toString(), "Hello Everyone! MultiHandler");
        sender.execute(message);
    }

    @HandleCallback(values = {"RU", "UZ"})
    private void callBack(Update update, AbsSender sender) throws TelegramApiException {
        System.out.println("Enter Sender !");

        String text = "<b> Choose bot language: </b>";
        SendMessage message = new SendMessage(update.getCallbackQuery().getMessage().getChatId().toString(), text);
        sender.execute(message);

        AnswerCallbackQuery answerCallbackQuery = new AnswerCallbackQuery();
        answerCallbackQuery.setCallbackQueryId(update.getCallbackQuery().getId());
        answerCallbackQuery.setText("Nurislom");
        answerCallbackQuery.setShowAlert(true);
        sender.execute(answerCallbackQuery);
    }

    @HandleCallback(values = {"EN"})
    private void callBackTwoParam(Update update, AbsSender sender) throws TelegramApiException {
        System.out.println("sender = " + sender);
        System.out.println("Enter Method");
        String text = "I Got it!";
        SendMessage sendMessage = new SendMessage(update.getCallbackQuery().getMessage().getChatId().toString(), text);
        sender.execute(sendMessage);
    }

    @HandleMessage(value = "wow", scope = MessageScope.START_WITH)
    public void world(Update update, AbsSender sender) throws TelegramApiException {
        String text = """
                <b> What is Lorem Ipsum? </b> \s
                Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the\s
                industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type\s
                and scrambled it to make a type specimen book. It has survived not only five centuries, but also the\s
                leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with\s
                the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop\s
                publishing software like Aldus PageMaker including versions of Lorem Ipsum.
                """;

        SendMessage sendMessage = new SendMessage(update.getMessage().getChatId().toString(), text);

        String text2 = "I Got it!";
        SendMessage sendMessage2 = new SendMessage(update.getMessage().getChatId().toString(), text2);

        sender.execute(sendMessage);
        sender.execute(sendMessage2);
    }

    public static InlineKeyboardMarkup language() {
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

    public static ReplyKeyboardMarkup enterMenu() {
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
