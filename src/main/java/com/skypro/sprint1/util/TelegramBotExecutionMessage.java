package com.skypro.sprint1.util;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.experimental.UtilityClass;

@UtilityClass
public class TelegramBotExecutionMessage {

    public static void execute(TelegramBot bot, Long chatId, String message) {
        SendMessage sendMessage = new SendMessage(chatId, message);
        bot.execute(sendMessage);
    }
}
