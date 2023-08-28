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

@Component
@Getter
public class PatternHandler extends AbstractHandler {

    private final Pattern fullMessagePattern = Pattern.compile("(\\d{7}) (\\d{1,4}) (\\p{L}+) ([\\p{L}\\d]{1,3})");
    private final Pattern commentMessagePattern = Pattern.compile("(\\d{7}) (\\d{1,4}) ([\\p{L}+\\d]{1,9})");
    private final Pattern messagePattern = Pattern.compile("(\\d{7}) (\\d{1,4}$)");
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

    @PostConstruct
    @Scheduled(fixedDelay = 1, timeUnit = TimeUnit.DAYS)
    public void loadAll() {
        logger.warn("Load all models");
        models = modelService.getModelCodes();
    }

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