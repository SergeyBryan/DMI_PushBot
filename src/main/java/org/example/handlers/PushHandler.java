package org.example.handlers;

import com.pengrad.telegrambot.model.Update;
import org.example.handlers.enums.MainMenu;
import org.example.messenger.Messenger;
import org.example.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class PushHandler extends AbstractHandler {

    private final Logger logger = LoggerFactory.getLogger(PushHandler.class);
    private final UserService userService;

    public PushHandler(UserService userService) {
        this.userService = userService;
    }


    @Override
    public boolean appliesTo(Update update) {
//        if (count == 1) {
//            logger.debug("flag = {}", count);
//            return false;
//        }
//        if (!userService.isUserServiceIsZero(update.message().chat().id())) {
//            logger.warn("{}", PushHandler.class);
//            return false;
//        }
        if (update.message() != null) {
            return false;
        }
        logger.info("check 30");
        logger.info("{}", update.callbackQuery().message().chat().id());
        if (!userService.isUserServiceIsZero(update.callbackQuery().message().chat().id())) {
            logger.debug("flag = {}", count);
            return false;
        }
        logger.info("checkv2 35");
        return update.callbackQuery() != null && update.callbackQuery().data().substring(1).equals(MainMenu.HOW_TO_PUSH.getKey());
    }

    @Override
    public void handle(Update update) {
        long chatId = update.message() != null ? update.message().chat().id() :
                update.callbackQuery() != null ? update.callbackQuery().message().chat().id() : 0;
        Messenger.editPast(chatId, telegramBot, update);
        Messenger.sendMessage(chatId, PUSH_TEXT, telegramBot);
    }
}
