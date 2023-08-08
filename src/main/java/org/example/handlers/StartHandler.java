package org.example.handlers;

import com.pengrad.telegrambot.model.Update;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;


@Component
public class StartHandler extends AbstractHandler {
    String START = "Приветсвую...";
    private final Logger LOG = LoggerFactory.getLogger(StartHandler.class);

    @Override
    public boolean appliesTo(Update update) {
        boolean result = (update.message() != null && update.message().text().equals("/start")
                || (update.callbackQuery() != null && update.callbackQuery().data().equals("/" + BACK_TO_INFO)));
        LOG.warn("{}", result);
        return result;
    }

    @Override
    public void handle(Update update) {
        long chatId = update.message() != null ? update.message().chat().id() :
                update.callbackQuery() != null ? update.callbackQuery().message().chat().id() : 0;

//        Messenger.sendMessage(chatId, START, telegramBot);
        Messenger.sendButtonMessage(chatId, START, telegramBot,
                new ArrayList<>(infoMenu.keySet()));
    }
}
