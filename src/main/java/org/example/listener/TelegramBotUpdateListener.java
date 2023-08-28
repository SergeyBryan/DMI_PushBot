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
    private final Logger logger = LoggerFactory.getLogger(TelegramBotUpdateListener.class);

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
            updates.stream()
                    .filter(update -> update.message() != null
                            || update.callbackQuery() != null)
                    .forEach(this::handle);
        } catch (Exception e) {
            e.getStackTrace();
        }
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    public void handle(Update update) {
        for (Handler handler : handlers) {
            logger.warn("Processing handler " + handler.getClass());
            if (handler.appliesTo(update)) {
                handler.handle(update);
                break;
            }
        }
    }
}