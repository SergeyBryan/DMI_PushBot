package org.example.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import jakarta.annotation.PostConstruct;
import org.example.handlers.Handler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TelegramBotUpdateListener implements UpdatesListener {
    Logger logger = LoggerFactory.getLogger(TelegramBotUpdateListener.class);

    private final List<Handler> handlers;


    public TelegramBotUpdateListener(List<Handler> handlers, TelegramBot telegramBot) {
        this.handlers = handlers;
        this.telegramBot = telegramBot;
    }

    @Autowired
    private final TelegramBot telegramBot;

    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }

    @Override
    public int process(List<Update> updates) {
        try {
            //                if (update.callbackQuery() != null) {
            //
            //                } else if (handlers.get(1).appliesTo(update)) {
            //                    handlers.get(1).handle(chatId, update);
            //                }
            updates.stream().filter(update -> update.message() != null || update.callbackQuery() != null)
                    .forEach(this::handle);
        } catch (
                Exception e) {
            e.getStackTrace();
        }
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    //Проблема в телеграмм боте, нужно создать в абстрактном классе нормального телеграмм бота
    public void handle(Update update) {
        for (Handler handler : handlers) {
            logger.warn("Хэндлер " + handler.getClass() + " в работе");
            if (handler.appliesTo(update)) {
                handler.handle(update);
                break;
            }
        }
    }


}

