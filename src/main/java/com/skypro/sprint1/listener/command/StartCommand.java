package com.skypro.sprint1.listener.command;

import com.pengrad.telegrambot.TelegramBot;
import com.skypro.sprint1.util.TelegramBotExecutionMessage;

public class StartCommand implements Command {

    @Override
    public void execute(TelegramBot bot, Long chatId) {
        TelegramBotExecutionMessage.execute(bot, chatId, getDescription());
    }

    private String getDescription() {
        return "Добрый день";
    }
}
