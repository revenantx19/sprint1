package com.skypro.sprint1.listener.command;

import com.pengrad.telegrambot.TelegramBot;
import com.skypro.sprint1.model.FullName;
import com.skypro.sprint1.pojo.Recommendation;
import com.skypro.sprint1.pojo.UserRecommendation;
import com.skypro.sprint1.repository.UserRepository;
import com.skypro.sprint1.service.UserRecommendationService;
import com.skypro.sprint1.util.TelegramBotExecutionMessage;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class RecommendCommand extends ArgumentCommand {

    private final UserRepository userRepository;
    private final UserRecommendationService userRecommendationService;

    public RecommendCommand(String[] args, UserRepository userRepository, UserRecommendationService userRecommendationService) {
        super(args);
        this.userRepository = userRepository;
        this.userRecommendationService = userRecommendationService;
    }

    @Override
    public void execute(TelegramBot bot, Long chatId) {

        String[] args = getArgs();
        if (args.length != 1) {
            TelegramBotExecutionMessage.execute(bot, chatId, "Invalid number of arguments");
            return;
        }
        String userName = args[0];
        UUID userId = userRepository.findIdByUserName(userName);

        if (userId == null) {
            userNotFoundMessage(bot, chatId, userName);

        } else {
            Optional<UserRecommendation> recommendation = userRecommendationService.getRecommendations(userId);

            if (recommendation.isPresent()) {
                recommendationFoundMessage(bot, chatId, recommendation.get(), userName);
            } else {
                recommendationNotFoundMessage(bot, chatId);
            }
        }
    }

    private void recommendationFoundMessage(TelegramBot bot, Long chatId,
                                UserRecommendation recommendation, String userName) {

        FullName fullName = userRepository.findFullNameByUserName(userName);
        String message = "Welcome, " + fullName.getFirstName() + " " + fullName.getLastName();
        TelegramBotExecutionMessage.execute(bot, chatId, message);

        List<Recommendation> recommendations = recommendation.getRecommendations();

        StringBuilder builder = new StringBuilder("New products for you:\n\n\n");
        for (Recommendation r : recommendations) {
            builder.append("Product Name: ").append(r.getProductName()).append("\n\n");
            builder.append("Description: ").append(r.getProductDescription()).append("\n\n\n");
        }

        TelegramBotExecutionMessage.execute(bot, chatId, builder.toString());
    }

    private void recommendationNotFoundMessage(TelegramBot bot, Long chatId) {
        TelegramBotExecutionMessage.execute(bot, chatId, "No available products");
    }

    private void userNotFoundMessage(TelegramBot bot, Long chatId, String userName) {
        String message = "No user found with name " + userName;
        TelegramBotExecutionMessage.execute(bot, chatId, message);
    }

}
