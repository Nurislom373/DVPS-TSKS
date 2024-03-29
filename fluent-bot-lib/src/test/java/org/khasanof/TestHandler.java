package org.khasanof;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.khasanof.core.enums.*;
import org.khasanof.core.enums.scopes.DocumentScope;
import org.khasanof.core.enums.scopes.PhotoScope;
import org.khasanof.main.annotation.UpdateController;
import org.khasanof.main.annotation.methods.*;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.AnswerInlineQuery;
import org.telegram.telegrambots.meta.api.methods.send.SendDice;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.inlinequery.result.InlineQueryResult;
import org.telegram.telegrambots.meta.api.objects.inlinequery.result.InlineQueryResultContact;
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
@UpdateController
public class TestHandler {

    private static final InlineKeyboardMarkup INLINE_KEYBOARD_MARKUP = new InlineKeyboardMarkup();
    private static final ReplyKeyboardMarkup REPLY_KEYBOARD_MARKUP = new ReplyKeyboardMarkup();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @HandleMessage(value = "/start", scope = MatchScope.EQUALS)
    private void start(Update update, AbsSender sender) throws TelegramApiException, JsonProcessingException {
        String text = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(update.getMessage().getChat());
        SendMessage message = new SendMessage(update.getMessage().getChatId().toString(), text);
        message.setReplyMarkup(language());
        SendDice dice = new SendDice(update.getMessage().getChatId().toString());
//        dice.setEmoji("\uD83C\uDFB2"); // 1
//        dice.setEmoji("\uD83C\uDFAF"); // 2
//        dice.setEmoji("\uD83C\uDFC0"); // 3
        dice.setEmoji("⚽"); // 4
//        dice.setEmoji("\uD83C\uDFB0"); // 5
        sender.execute(message);
        sender.execute(dice);
    }

    @HandleMessage(value = "/translate {var1:[0-9]}", scope = MatchScope.EXPRESSION_VARIABLE)
    private void updateVariableTest(Update update, AbsSender sender) {

    }

    @HandleAny(type = HandleType.MESSAGE, proceed = Proceed.PROCEED)
    private void handleAnyMessages(Update update, AbsSender sender) throws TelegramApiException {
        String text = "I'm handle this message : " + update.getMessage().getText();
        SendMessage message = new SendMessage(update.getMessage().getChatId().toString(), text);
        sender.execute(message);
    }

    @HandleAny(type = HandleType.AUDIO, proceed = Proceed.PROCEED)
    private void handleAnyCallbacks(Update update, AbsSender sender) throws TelegramApiException, JsonProcessingException {
        String value = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(update.getMessage().getAudio());
        String text = "I'm handle this audio : \n" + value;
        SendMessage message = new SendMessage(update.getMessage().getChatId().toString(), text);
        sender.execute(message);
    }

    @HandleAny(type = HandleType.VIDEO_NOTE, proceed = Proceed.PROCEED)
    private void handleAnyVideoNote(Update update, AbsSender sender) throws TelegramApiException, JsonProcessingException {
        String value = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(update.getMessage().getVideoNote());
        String text = "I'm handle this video note : \n" + value;
        SendMessage message = new SendMessage(update.getMessage().getChatId().toString(), text);
        sender.execute(message);
    }

    @HandleAny(type = HandleType.VIDEO, proceed = Proceed.PROCEED)
    private void handleAnyPhotos(Update update, AbsSender sender) throws TelegramApiException, JsonProcessingException {
        String value = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(update.getMessage().getVideo());
        String text = "I'm handle this video : \n" + value;
        SendMessage message = new SendMessage(update.getMessage().getChatId().toString(), text);
        sender.execute(message);
    }

    @HandlePhotos(values = {
            @HandlePhoto(
                    value = "value >= 190000 && value <= 207000",
                    match = MatchScope.EXPRESSION,
                    scope = PhotoScope.FILE_SIZE
            )
    })
    private void handlePhotos(Update update, AbsSender sender) throws TelegramApiException, JsonProcessingException {
        String text = "I'm handle this expression {value >= 200000 && value <= 206000}";
        SendMessage message = new SendMessage(update.getMessage().getChatId().toString(), text);
        sender.execute(message);
    }

    @HandleMessage(value = "START_WITH('javohir', value) && END_WITH('abdulloh', value)",
            scope = MatchScope.EXPRESSION)
    private void messageExp(Update update, AbsSender sender) throws TelegramApiException {
        String text = "I'm handle this expression {value >= 200000 && value <= 206000}";
        SendMessage message = new SendMessage(update.getMessage().getChatId().toString(), text);
        sender.execute(message);
    }

    @HandleAny(type = HandleType.STICKER, proceed = Proceed.NOT_PROCEED)
    private void handleAnyStickers(Update update, AbsSender sender) throws TelegramApiException, JsonProcessingException {
        String value = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(update.getMessage().getSticker());
        String text = "I'm handle this sticker : \n" + value;
        SendMessage message = new SendMessage(update.getMessage().getChatId().toString(), text);
        sender.execute(message);
    }

    @HandleAny(type = HandleType.DOCUMENT, proceed = Proceed.PROCEED)
    private void handleAnyDocument(Update update, AbsSender sender) throws TelegramApiException, JsonProcessingException {
        String value = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(update.getMessage().getDocument());
        String text = "I'm handle this document : \n" + value;
        SendMessage message = new SendMessage(update.getMessage().getChatId().toString(), text);
        sender.execute(message);
    }

    @HandleMessages(values = {
            @HandleMessage(value = "start", scope = MatchScope.START_WITH),
            @HandleMessage(value = "end", scope = MatchScope.END_WITH),
            @HandleMessage(value = "boom", scope = MatchScope.EQUALS_IGNORE_CASE)
    })
    void multiMessageHandler(Update update, AbsSender sender) throws TelegramApiException {
        SendMessage message = new SendMessage(update.getMessage().getChatId().toString(), "Hello Everyone! MultiHandler");
        message.setReplyMarkup(enterMenu());
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

    @HandleCallbacks(values = {
            @HandleCallback(values = {"NEXT", "PREV"}),
            @HandleCallback(values = {"TOP", "BOTTOM"}),
            @HandleCallback(values = {"LST"}, scope = MatchScope.START_WITH)
    })
    private void multiCallback(Update update, AbsSender sender) throws TelegramApiException {
        String text = "NPTB one handle \uD83D\uDE0E";

        AnswerCallbackQuery answerCallbackQuery = new AnswerCallbackQuery();
        answerCallbackQuery.setCallbackQueryId(update.getCallbackQuery().getId());
        answerCallbackQuery.setText("Nurislom");
        answerCallbackQuery.setShowAlert(true);
        sender.execute(answerCallbackQuery);

        SendMessage sendMessage = new SendMessage(update.getCallbackQuery().getMessage().getChatId().toString(), text);
        InlineQueryResult result = new InlineQueryResultContact("1", "993733273", "Nurislom");
        AnswerInlineQuery answerInlineQuery = new AnswerInlineQuery(answerCallbackQuery.getCallbackQueryId(),
                List.of(result));
        sender.execute(sendMessage);
        sender.execute(answerInlineQuery);
    }

    @HandleCallback(values = {"EN"})
    private void callBackTwoParam(Update update, AbsSender sender) throws TelegramApiException {
        System.out.println("sender = " + sender);
        System.out.println("Enter Method");
        String text = "I Got it!";
        SendMessage sendMessage = new SendMessage(update.getCallbackQuery().getMessage().getChatId().toString(), text);
        sender.execute(sendMessage);
    }

    @HandleDocument(
            value = "([a-zA-Z0-9\\s_\\\\.\\-\\(\\):])+(.jpeg|.png|.pdf)$",
            match = MatchScope.REGEX,
            scope = DocumentScope.FILE_NAME
    )
    private void handleDocumentOne(AbsSender sender, Update update) throws TelegramApiException {
        String text = "I Handle 1 File \uD83D\uDE02";
        SendMessage sendMessage = new SendMessage(update.getMessage().getChatId().toString(), text);
        sender.execute(sendMessage);
    }

    @HandleDocument(
            value = "photo",
            match = MatchScope.START_WITH,
            scope = DocumentScope.FILE_NAME
    )
    private void handleDocumentTwo(AbsSender sender, Update update) throws TelegramApiException {
        String text = "I Handle 2 File \uD83D\uDE02";
        SendMessage sendMessage = new SendMessage(update.getMessage().getChatId().toString(), text);
        sender.execute(sendMessage);
    }

    @HandleDocument(
            value = ".jpeg",
            match = MatchScope.END_WITH,
            scope = DocumentScope.FILE_NAME
    )
    private void handleDocumentThree(AbsSender sender, Update update) throws TelegramApiException {
        String text = "I Handle 3 File \uD83D\uDE02";
        SendMessage sendMessage = new SendMessage(update.getMessage().getChatId().toString(), text);
        sender.execute(sendMessage);
    }

//    @DocumentExpression(value = "END_WITH(value, '.jpeg') || START_WITH(value, 'photossssss')", scope = DocumentScope.FILE_NAME)
//    @DocumentExpression(value = "value >= 100 && value <= 205967", scope = DocumentScope.FILE_SIZE)
    private void handleDocumentExpression(AbsSender sender, Update update) throws TelegramApiException {
        String text = "I Handle File Nurislom\uD83D\uDE02";
        SendMessage sendMessage = new SendMessage(update.getMessage().getChatId().toString(), text);
        sender.execute(sendMessage);
    }

    @HandleMessage(value = "[1-5]", scope = MatchScope.REGEX)
    public void world(AbsSender sender, Update update) throws TelegramApiException {
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

    public static InlineKeyboardMarkup multiInline() {
        InlineKeyboardButton next = new InlineKeyboardButton("NEXT");
        next.setCallbackData("NEXT");

        InlineKeyboardButton prev = new InlineKeyboardButton("PREV");
        prev.setCallbackData("PREV");

        InlineKeyboardButton top = new InlineKeyboardButton("TOP");
        top.setCallbackData("TOP");

        InlineKeyboardButton bottom = new InlineKeyboardButton("BOTTOM");
        bottom.setCallbackData("BOTTOM");

        InlineKeyboardButton lst1 = new InlineKeyboardButton("LST-1");
        lst1.setCallbackData("LST-1");

        InlineKeyboardButton lst2 = new InlineKeyboardButton("LST-2");
        lst2.setCallbackData("LST-5");

        List<InlineKeyboardButton> row1 = new ArrayList<>();
        row1.add(next);
        row1.add(prev);

        List<InlineKeyboardButton> row2 = new ArrayList<>();
        row2.add(top);
        row2.add(bottom);

        List<InlineKeyboardButton> row3 = new ArrayList<>();
        row3.add(lst1);
        row3.add(lst2);

        INLINE_KEYBOARD_MARKUP.setKeyboard(Arrays.asList(row1, row2, row3));
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
