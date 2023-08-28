package org.example.handlers;

import com.pengrad.telegrambot.model.Update;
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

    private final UserService userService;

    private final Logger logger = LoggerFactory.getLogger(InfoHandler.class);

    public InfoHandler(UserService userService) {
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

        return MainMenu.SPORT_INFO.getKey().equals(message)
                || message.equals(BACK_TO_SPORT);
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
