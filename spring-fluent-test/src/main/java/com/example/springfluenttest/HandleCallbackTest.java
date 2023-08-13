package com.example.springfluenttest;

import org.khasanof.springbootstarterfluent.core.enums.MatchScope;
import org.khasanof.springbootstarterfluent.main.annotation.UpdateController;
import org.khasanof.springbootstarterfluent.main.annotation.methods.HandleCallback;
import org.khasanof.springbootstarterfluent.main.annotation.methods.HandleCallbacks;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.AnswerInlineQuery;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.inlinequery.result.InlineQueryResult;
import org.telegram.telegrambots.meta.api.objects.inlinequery.result.InlineQueryResultContact;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

/**
 * @author Nurislom
 * @see com.example.springfluenttest
 * @since 8/9/2023 9:41 PM
 */
@UpdateController
public class HandleCallbackTest {

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

}
