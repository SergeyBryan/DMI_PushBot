package org.example.handlers;

import com.pengrad.telegrambot.model.Update;
import org.example.handlers.enums.MainMenu;
import org.example.messenger.Messenger;
import org.example.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
public class PushHandler extends AbstractHandler {

    private final Logger logger = LoggerFactory.getLogger(PushHandler.class);
    private final UserService userService;

    public PushHandler(UserService userService) {
        this.userService = userService;
    }


    @Override
    public boolean appliesTo(Update update) {
        if (update.callbackQuery() == null) {
            return false;
        }

        long chatId = update.callbackQuery().message().chat().id();
        String message = update.callbackQuery().data().substring(1);

        if (!userService.isUserStatusIsZero(chatId)) {
            return false;
        }

        return message.equals(MainMenu.HOW_TO_PUSH.getKey());
    }

    @Override
    public void handle(Update update) {
        long chatId = update.message() != null ? update.message().chat().id() :
                update.callbackQuery() != null ? update.callbackQuery().message().chat().id() : 0;

        Messenger.editPast(chatId, telegramBot, update);
        Messenger.sendPhoto(chatId, telegramBot, new File("src/main/resources/HOW_TO_PUSH.JPG"));
    }
}
