package com.skypro.sprint1.listener.command;

import com.pengrad.telegrambot.TelegramBot;

/**
 * Интерфейс для команд бота.
 * Команды используются для выполнения действий в ответ на сообщения от пользователей.
 * @author Nikita Malinkin
 * @version 1.0
 */
public interface Command {

    /**
     * Выполняет команду.
     *
     * @param bot    Экземпляр бота Telegram.
     * @param chatId Идентификатор чата, в котором была получена команда.
     */
    void execute(TelegramBot bot, Long chatId);
}
