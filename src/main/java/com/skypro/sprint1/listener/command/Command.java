package com.skypro.sprint1.listener.command;

import com.pengrad.telegrambot.TelegramBot;

public interface Command {

    void execute(TelegramBot bot, Long chatId);
}
