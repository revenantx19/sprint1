package com.skypro.sprint1.listener.command;

import com.skypro.sprint1.repository.UserRepository;
import com.skypro.sprint1.service.UserRecommendationService;
import org.springframework.stereotype.Component;

@Component
public class CommandFactory {

    private final UserRepository userRepository;
    private final UserRecommendationService userRecommendationService;

    public CommandFactory(UserRepository userRepository, UserRecommendationService userRecommendationService) {
        this.userRepository = userRepository;
        this.userRecommendationService = userRecommendationService;
    }

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

    private String[] getArgs(String[] commandNameAndArgs) {
        String[] args = new String[commandNameAndArgs.length - 1];
        System.arraycopy(commandNameAndArgs, 1, args, 0, args.length);
        return args;
    }
}
