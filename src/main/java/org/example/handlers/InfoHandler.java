package org.example.handlers;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import org.example.handlers.enums.MainMenu;
import org.example.messenger.Messenger;
import org.example.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class InfoHandler extends AbstractHandler {

    UserService userService;

    public InfoHandler(UserService userService) {
        this.userService = userService;
    }

    private final Logger logger = LoggerFactory.getLogger(InfoHandler.class);


    @Override
    public boolean appliesTo(Update update) {
//        if (count == 1) {
//            logger.debug("flag = {}", count);
//            return false;
//        }
        if (update.message() != null) {
            return false;
        }
        if (!userService.isUserServiceIsZero(update.callbackQuery().message().chat().id())) {
            logger.debug("flag = {}", count);
            return false;
        }
        if (update.callbackQuery() != null) {
            return MainMenu.SPORT_INFO.getKey().equals(update.callbackQuery().data().substring(1))
                    || update.callbackQuery().data().substring(1).equals(BACK_TO_SPORT);
        }
        return false;
    }

    @Override
    public void handle(Update update) {
        long chatId = update.callbackQuery().message().chat().id();
        String text = update.callbackQuery().data().substring(1);
        if (MainMenu.SPORT_INFO.getKey().equals(text) || text.equals(BACK_TO_SPORT)) {
            List<String> list = new ArrayList<>(getDepartmentList());
            list.add(BACK_TO_INFO);
            Messenger.editPast(chatId, telegramBot, update);
            Messenger.sendButtonMessage(chatId, "Выберите спорт", telegramBot,
                    new ArrayList<>(list));
        }
    }
}
