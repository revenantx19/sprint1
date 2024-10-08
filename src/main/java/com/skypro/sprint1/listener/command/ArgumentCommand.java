package com.skypro.sprint1.listener.command;

import lombok.Getter;
import lombok.Setter;

/**
 * Абстрактный класс для команд, принимающих аргументы.
 *
 * @author Nikita Malinkin
 * @version 1.0
 */
@Setter
@Getter
public abstract class ArgumentCommand implements Command {

    /**
     * Массив аргументов, переданных команде.
     */
    private String[] args;

    /**
     * Конструктор класса ArgumentCommand.
     *
     * @param args Массив аргументов, переданных команде.
     */
    public ArgumentCommand(String[] args) {
        this.args = args;
    }
}
