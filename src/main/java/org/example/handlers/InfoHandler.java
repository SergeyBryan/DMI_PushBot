package org.example.handlers;

import com.pengrad.telegrambot.model.Update;
import org.example.handlers.enums.Info;
import org.example.messenger.Messenger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class InfoHandler extends AbstractHandler {

    private final Logger logger = LoggerFactory.getLogger(InfoHandler.class);

    @Override
    public boolean appliesTo(Update update) {
        if (update.message() != null) {
            return false;
        }
        if (update.callbackQuery() != null) {
            return Info.SPORT_INFO.getKey().equals(update.callbackQuery().data().substring(1))
                    || update.callbackQuery().data().substring(1).equals(BACK_TO_SPORT);
        }
        return false;
    }

    @Override
    public void handle(Update update) {
        long chatId = update.callbackQuery().message().chat().id();
        String text = update.callbackQuery().data().substring(1);
        if (Info.SPORT_INFO.getKey().equals(text) || text.equals(BACK_TO_SPORT)) {
            List<String> list = new ArrayList<>(getDepartmentList());
            list.add(BACK_TO_INFO);
            Messenger.editPast(chatId, telegramBot, update);
            Messenger.sendButtonMessage(chatId, "Выберите спорт", telegramBot,
                    new ArrayList<>(list));
        }
    }
}
