package org.example.handlers;

import com.pengrad.telegrambot.model.Update;
import org.example.handlers.enums.Info;
import org.example.messenger.Messenger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class PushHandler extends AbstractHandler {

    private final Logger logger = LoggerFactory.getLogger(PushHandler.class);

    @Override
    public boolean appliesTo(Update update) {
        if (count == 0) {
            if (update.message() != null) {
                return false;
            }
            if (update.callbackQuery() != null && update.callbackQuery().data().substring(1).equals(Info.HOW_TO_PUSH.getKey())) {
                logger.warn("Проходим проверку в хэгдлере... " + PushHandler.class);
                return true;
            }
            return false;
        }
        return false;
    }

    @Override
    public void handle(Update update) {
        long chatId = update.message() != null ? update.message().chat().id() :
                update.callbackQuery() != null ? update.callbackQuery().message().chat().id() : 0;
        Messenger.editPast(chatId, telegramBot, update);
        Messenger.sendMessage(chatId, PUSH_TEXT, telegramBot);
    }
}
