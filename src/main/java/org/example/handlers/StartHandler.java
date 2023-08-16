package org.example.handlers;

import com.pengrad.telegrambot.model.Update;
import org.example.entity.User;
import org.example.messenger.Messenger;
import org.example.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;


@Component
public class StartHandler extends AbstractHandler {

    UserRepository userRepository;
    private String START_TEXT = "Привет!\n" +
            "Данный бот поможет тебе найти нужную информацию по спорту либо сделать запрос на пуш";
    private final Logger logger = LoggerFactory.getLogger(StartHandler.class);

    @Override
    public boolean appliesTo(Update update) {
        if (count == 1) {
            count = 0;
        }
        return (update.message() != null && update.message().text().equals("/start")
                || (update.callbackQuery() != null && update.callbackQuery().data().equals("/" + BACK_TO_INFO)));
    }

    @Override
    public void handle(Update update) {
        long chatId = update.message() != null ? update.message().chat().id() :
                update.callbackQuery() != null ? update.callbackQuery().message().chat().id() : 0;
        if (update.callbackQuery() != null) {
            if (update.callbackQuery() != null && update.callbackQuery().data().equals("/" + BACK_TO_INFO)) {
                Messenger.editPast(chatId, telegramBot, update);
            }
        }
//        createUser(update);
        Messenger.sendButtonMessage(chatId, START_TEXT, telegramBot,
                new ArrayList<>(infoMenu.keySet()));
    }

    private void createUser(Update update) {
        if (update.message() != null) {
            User user = new User();
            user.setName(update.message().from().firstName());
            user.setChatId(update.message().chat().id());
            userRepository.save(user);
        }
    }
}
