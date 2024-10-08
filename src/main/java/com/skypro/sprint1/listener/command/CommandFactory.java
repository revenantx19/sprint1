package com.skypro.sprint1.listener.command;

import com.skypro.sprint1.repository.UserRepository;
import com.skypro.sprint1.service.UserRecommendationService;
import org.springframework.stereotype.Component;

/**
 * «Фабрика» для создания команд.
 *
 * @author Nikita Malinkin
 * @version 1.0
 */
@Component
public class CommandFactory {

    private final UserRepository userRepository;
    private final UserRecommendationService userRecommendationService;

    public CommandFactory(UserRepository userRepository, UserRecommendationService userRecommendationService) {
        this.userRepository = userRepository;
        this.userRecommendationService = userRecommendationService;
    }

    /**
     * Создание команды на основе текстового запроса.
     *
     * @param text текстовый запрос
     * @return команда, соответствующая запросу, или null, если команда не распознана
     */
    public Command createCommand(String text) {
        String[] commandNameAndArgs = text.split(" ");

        String commandName = commandNameAndArgs[0];
        String[] args = getArgs(commandNameAndArgs);

        return switch (commandName) {
            case "/start" -> new StartCommand();
            case "/recommend" -> new RecommendCommand(args, userRepository, userRecommendationService);
            default -> null;
        };
    }

    /**
     * Извлекает аргументы из массива строк, исключая первое значение (имя команды).
     *
     * @param commandNameAndArgs массив строк с именем команды и аргументами
     * @return массив аргументов
     */
    private String[] getArgs(String[] commandNameAndArgs) {
        String[] args = new String[commandNameAndArgs.length - 1];
        System.arraycopy(commandNameAndArgs, 1, args, 0, args.length);
        return args;
    }
}
