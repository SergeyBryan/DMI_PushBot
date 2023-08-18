package org.example.handlers;

import com.pengrad.telegrambot.model.Update;
import org.example.entity.User;
import org.example.listener.TelegramBotUpdateListener;
import org.example.messenger.Messenger;
import org.example.repository.UserRepository;
import org.example.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Map;


@Component
@Order(1)
public class StartHandler extends AbstractHandler {

    UserService userService;

    public StartHandler(UserService userService) {
        this.userService = userService;
    }

    private String START_TEXT = "Привет!\n" +
            "Данный бот поможет тебе найти нужную информацию по спорту либо сделать запрос на пуш";
    private final Logger logger = LoggerFactory.getLogger(StartHandler.class);

    @Override
    public boolean appliesTo(Update update) {
        return (update.message() != null && update.message().text().equals("/start")
                || (update.callbackQuery() != null && update.callbackQuery().data().equals("/" + BACK_TO_INFO)));
    }

    @Override
    public void handle(Update update) {
//        if (count == 1) {
//            logger.debug("flag has been changed");
//            count = 0;
//        }
        long chatId = update.message() != null ? update.message().chat().id() :
                update.callbackQuery() != null ? update.callbackQuery().message().chat().id() : 0;
        createUser(update);
        if (!userService.isUserServiceIsZero(chatId)) {
            userService.userChangeStatus(chatId, 0);
        }
        logger.info("{}", statusList.values());
        if (update.callbackQuery() != null) {
            if (update.callbackQuery() != null && update.callbackQuery().data().equals("/" + BACK_TO_INFO)) {
                Messenger.editPast(chatId, telegramBot, update);
            }
        }
        Messenger.sendButtonMessage(chatId, START_TEXT, telegramBot,
                new ArrayList<>(infoMenu.keySet()));
    }

    private void createUser(Update update) {
        if (update.message() != null) {
            long chatId = update.message().chat().id();
            User user = userService.findByChatId(chatId);
            if (user == null) {
                logger.warn("Проверили, что юзер пустой = {}", user);
                User newUser = new User();
                newUser.setName(update.message().from().firstName());
                newUser.setChatId(chatId);
                newUser.setStatus(0);
                userService.create((newUser));
            }
        }
    }

    private void userStatus(long chatId) {
        logger.info("Проверяем юзера");
        if (!statusList.isEmpty()) {
            for (Map.Entry<Long, Integer> integerMap : statusList.entrySet()) {
                logger.info("Проверяем ключ");
                if (integerMap.getKey().equals(chatId)) {
                    logger.info("Проверяем значение");
                    if (integerMap.getValue().equals(1)) {
                        logger.info("устанавливаем значение если оно 1");
                        integerMap.setValue(0);
                    } else {
                        logger.info("устанавливаем значение если значение не сходится ");
                        integerMap.setValue(0);
                    }
                } else {
                    logger.info("Добавляем юзера если его нет");
                    statusList.put(chatId, 0);
                }
            }
        } else {
            statusList.put(chatId, 0);
        }
    }
}
