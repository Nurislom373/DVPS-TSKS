package com.example.springfluenttest

import org.khasanof.springbootstarterfluent.core.enums.MatchScope
import org.khasanof.springbootstarterfluent.main.annotation.UpdateController
import org.khasanof.springbootstarterfluent.main.annotation.extra.BotVariable
import org.khasanof.springbootstarterfluent.main.annotation.methods.HandleMessage
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.bots.AbsSender

@UpdateController
class TestKtController {

    @HandleMessage(value = "result: {result:[a-z]}", scope = MatchScope.VAR_EXPRESSION)
    fun simpleExpression(update: Update, sender: AbsSender, @BotVariable("result") result: String) {
        println("result : $result")
        val message = SendMessage(update.message.chatId.toString(), "I'm kotlin handler 😎")
        sender.execute(message)
    }

}
