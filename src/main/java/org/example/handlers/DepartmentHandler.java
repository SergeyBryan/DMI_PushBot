package org.example.handlers;

import com.pengrad.telegrambot.model.Update;
import lombok.extern.java.Log;
import org.example.messenger.Messenger;
import org.example.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Collections;


@Component
public class DepartmentHandler extends AbstractHandler {
    UserService userService;

    public DepartmentHandler(UserService userService) {
        this.userService = userService;
    }

    private final Logger LOG = LoggerFactory.getLogger(DepartmentHandler.class);

    @Override
    public boolean appliesTo(Update update) {
//        if (count == 1) {
//            LOG.debug("flag = {}", count);
//            return false;
//        }

        if (update.message() != null) {
            return false;
        }
        if (!userService.isUserServiceIsZero(update.callbackQuery().message().chat().id())) {
            LOG.debug("flag = {}", count);
            return false;
        }
        if (update.callbackQuery() != null) {
            return departmentList.contains(update.callbackQuery().data().substring(1));
        }
        return false;
    }


    @Override
    public void handle(Update update) {
        long chatId = update.callbackQuery().message().chat().id();
        String text = update.callbackQuery().data().substring(1);
        if (departmentMenu.containsKey(text)) {
            Messenger.editPast(chatId, telegramBot, update);
            Messenger.sendButtonMessage(chatId, departmentMenu.get(text), telegramBot,
                    Collections.singletonList(BACK_TO_SPORT)
            );
        }
    }

}
