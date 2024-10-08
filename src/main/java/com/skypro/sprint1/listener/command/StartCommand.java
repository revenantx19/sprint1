package com.skypro.sprint1.listener.command;

import com.pengrad.telegrambot.TelegramBot;
import com.skypro.sprint1.util.TelegramBotExecutionMessage;

/**
 * Команда "Старт".
 * Выполняет отправку приветственного сообщения пользователю.
 *
 * @author Nikita Malinkin
 * @version 1.0
 */
public class StartCommand implements Command {

    @Override
    public void execute(TelegramBot bot, Long chatId) {
        TelegramBotExecutionMessage.execute(bot, chatId, getDescription());
    }

    private String getDescription() {
        return "Добрый день";
    }
}
