package org.example.handlers;

import com.pengrad.telegrambot.model.Update;
import org.example.messenger.Messenger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Collections;


@Component
public class DepartmentHandler extends AbstractHandler {

    private final Logger LOG = LoggerFactory.getLogger(DepartmentHandler.class);

    @Override
    public boolean appliesTo(Update update) {
        if (update.message() != null) {
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
