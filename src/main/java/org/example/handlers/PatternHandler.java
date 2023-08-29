package org.example.handlers;

import com.pengrad.telegrambot.model.Update;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import org.example.entity.Request;
import org.example.entity.User;
import org.example.messenger.Messenger;
import org.example.service.ModelService;
import org.example.service.RequestService;
import org.example.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Pattern Handler class for handling requests based on regular expressions.
 * This is Spring boot component and extends from AbstractHandler.
 */

@Component
@Getter
public class PatternHandler extends AbstractHandler {
    /**
     * Regular expression for full text message.
     */
    private final Pattern fullMessagePattern = Pattern.compile("(\\d{7}) (\\d{1,4}) (\\p{L}+) ([\\p{L}\\d]{1,3})");
    /**
     * Regular expression for comment message only.
     */
    private final Pattern commentMessagePattern = Pattern.compile("(\\d{7}) (\\d{1,4}) ([\\p{L}+\\d]{1,9})");
    /**
     * Regular expression for message only.
     */
    private final Pattern messagePattern = Pattern.compile("(\\d{7}) (\\d{1,4}$)");
    /**
     * There are list of regular expressions .
     */
    private final List<Pattern> patternList = List.of(fullMessagePattern, messagePattern, commentMessagePattern);
    private final Logger logger = LoggerFactory.getLogger(PatternHandler.class);
    private final RequestService requestService;
    private final ModelService modelService;

    private final UserService userService;


    public PatternHandler(RequestService requestService, ModelService modelService, UserService userService) {
        this.requestService = requestService;
        this.modelService = modelService;
        this.userService = userService;
    }

    @Setter
    private List<Integer> models = new ArrayList<>();

    /**
     * A method called after creating an instance of the class.
     * Loads all models and displays a warning in the log.
     */
    @PostConstruct
    @Scheduled(fixedDelay = 1, timeUnit = TimeUnit.DAYS)
    public void loadAll() {
        logger.warn("Load all models");
        models = modelService.getModelCodes();
    }

    /**
     * Checks if this handler is applicable to the given update.
     *
     * @param update The update to check.
     * @return true if the handler is applicable, false otherwise.
     */
    @Override
    public boolean appliesTo(Update update) {
        if (update.callbackQuery() != null) {
            return false;
        }

        long chatId = update.message().chat().id();

        if (!userService.isUserStatusIsZero(chatId)) {
            return false;
        }

        return !update.message().text().startsWith("/start");
    }

    /**
     * Handles the given update.
     *
     * @param update The update to handle.
     */
    @Override
    public void handle(Update update) {
        Matcher matcher = null;
        long chatId = update.message().chat().id();
        if (isValidMessage(update)) {
            matcher = patternList.stream().map(pattern1 -> pattern1.matcher(update.message().text()))
                    .filter(Matcher::find)
                    .findFirst().orElse(null);
        }

        if (matcher != null) {
            createRequest(matcher, chatId);
            Messenger.sendMessage(chatId, "Запрос на пуш создан", telegramBot);
        }
    }

    /**
     * Checks whether a given message is valid.
     *
     * @param update The update containing the message.
     * @return true if the message is valid, false otherwise.
     */
    private boolean isValidMessage(Update update) {
        String message = update.message().text();
        long chatId = update.message().chat().id();
        if (!isValidArticle(message)) {
            Messenger.sendMessage(chatId, "Артикул не найден", telegramBot);
            return false;
        }
        if (isValidTextLength(message)) {
            Messenger.sendMessage(chatId, "В сообщении не указано требуемое количество штук", telegramBot);
            return false;
        }
        if (!isValidQuantity(message)) {
            Messenger.sendMessage(chatId, "Не верно указано количество, вторая часть сообщения должна состоять от 1 до 3 цифр", telegramBot);
            return false;
        }
        if (isValidMaxTextLength(message)) {
            Messenger.sendMessage(chatId, "Ваше сообщение не соответствует примеру выше", telegramBot);
            return false;
        }
        return true;
    }

    /**
     * A method is responsible for creating a new request.
     *
     * @param matcher find appropriate text message.
     */
    private void createRequest(Matcher matcher, long chatId) {
        Request request = new Request();
        request.setModelCode(Integer.parseInt(matcher.group(1)));
        request.setQty(Integer.parseInt(matcher.group(2)));
        User user = userService.findByChatId(chatId);
        request.setUser(user);
        if (matcher.groupCount() > 2) {
            request.setComment(matcher.group(3));
        }
        if (matcher.groupCount() > 3) {
            request.setSize(matcher.group(4));
        }
        requestService.create(request);
    }

    private boolean isValidArticle(String message) {
        String article = message.split(" ")[0];
        return models.contains(Integer.parseInt(article));
    }

    private boolean isValidTextLength(String text) {
        return text.split(" ").length <= 1;
    }

    private boolean isValidMaxTextLength(String text) {
        return text.split(" ").length > 4;
    }

    private boolean isValidQuantity(String quantity) {
        Pattern pattern = Pattern.compile("^\\d{1,3}$");
        Matcher matcher = pattern.matcher(quantity.split(" ")[1]);
        return matcher.matches();
    }

}