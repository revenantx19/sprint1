package com.skypro.sprint1.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.skypro.sprint1.listener.command.Command;
import com.skypro.sprint1.listener.command.CommandFactory;
import com.skypro.sprint1.util.TelegramBotExecutionMessage;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Класс обрабатывающий сообщения от Telegram бота.
 *
 * @author Nikita Malinkin
 * @version 1.0
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class TelegramBotUpdateListener implements UpdatesListener {

    private final TelegramBot bot;

    private final CommandFactory commandFactory;

    /**
     * Инициализация слушателя.
     */
    @PostConstruct
    public void init() {
        bot.setUpdatesListener(this);
    }


    /**
     * Обработка обновлений.
     *
     * @param updates Список обновлений.
     * @return Количество подтвержденных обновлений.
     */
    @Override
    public int process(List<Update> updates) {
        updates.forEach(update -> {
            log.info("Processing update: {}", update);

            Message message = update.message();
            if (message != null) {

                Long chatId = message.chat().id();

                Command command = commandFactory.createCommand(message.text());
                if (command != null) {
                    command.execute(bot, chatId);
                } else {
                    TelegramBotExecutionMessage.execute(bot, chatId, "Unknown command");
                }

            }

        });
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }
}