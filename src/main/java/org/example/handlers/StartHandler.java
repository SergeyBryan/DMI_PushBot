package org.example.handlers;

import com.pengrad.telegrambot.model.Update;
import org.example.entity.User;
import org.example.messenger.Messenger;
import org.example.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

/**
 * Start Handler class for handling start-related requests.
 * This is Spring boot component and extends from AbstractHandler.
 */
@Component
@Order(1)
public class StartHandler extends AbstractHandler {

    private final UserService userService;

    private String START_TEXT = "Привет!\n" +
            "Данный бот поможет тебе найти нужную информацию по спорту либо сделать запрос на пуш";

    private final Logger logger = LoggerFactory.getLogger(StartHandler.class);

    public StartHandler(UserService userService) {
        this.userService = userService;
    }

    /**
     * Checks if this handler is applicable to the given update.
     *
     * @param update The update to check.
     * @return true if the handler is applicable, false otherwise.
     */
    @Override
    public boolean appliesTo(Update update) {
        return (update.message() != null && update.message().text().equals("/start")
                || (update.callbackQuery() != null && update.callbackQuery().data().equals("/" + BACK_TO_INFO)));
    }

    /**
     * Handles the given update.
     *
     * @param update The update to handle.
     */
    @Override
    public void handle(Update update) {
        long chatId = update.message() != null ? update.message().chat().id() :
                update.callbackQuery() != null ? update.callbackQuery().message().chat().id() : 0;
        createUser(update);
        if (!userService.isUserStatusIsZero(chatId)) {
            userService.userChangeStatus(chatId, 0);
        }

        if (update.callbackQuery() != null) {
            if (update.callbackQuery() != null && update.callbackQuery().data().equals("/" + BACK_TO_INFO)) {
                Messenger.editPast(chatId, telegramBot, update);
            }
        }

        Messenger.sendButtonMessage(chatId, START_TEXT, telegramBot,
                new ArrayList<>(infoMenu.keySet()));
    }

    /**
     * A method for creating user based on update.
     *
     * @param update which contains user data.
     */
    private void createUser(Update update) {
        if (update.message() != null) {
            long chatId = update.message().chat().id();
            User user = userService.findByChatId(chatId);
            if (user == null) {
                User newUser = new User();
                newUser.setName(update.message().from().firstName());
                newUser.setChatId(chatId);
                newUser.setStatus(0);
                userService.create((newUser));
            }
        }
    }
}
