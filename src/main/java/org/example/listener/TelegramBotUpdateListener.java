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

/**
 * The TelegramBotUpdateListener class is a service that listens for updates from the Telegram Bot API.
 * Implements the UpdatesListener interface.
 */
@Service
public class TelegramBotUpdateListener implements UpdatesListener {
    private final Logger logger = LoggerFactory.getLogger(TelegramBotUpdateListener.class);

    private final List<Handler> handlers;

    /**
     * Constructor for the TelegramBotUpdateListener class.
     *
     * @param handlers    A list of handlers to process the updates.
     * @param telegramBot The TelegramBot instance to use for sending and receiving messages.
     */
    public TelegramBotUpdateListener(List<Handler> handlers, TelegramBot telegramBot) {
        this.handlers = handlers;
        this.telegramBot = telegramBot;
    }

    @Autowired
    private final TelegramBot telegramBot;

    /**
     * Initialization method called after creating an instance of the class.
     * Sets the updates listener of the TelegramBot instance to this listener.
     */
    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }

    /**
     * Method for processing a list of updates.
     *
     * @param updates The updates to process.
     * @return The result of the update processing.
     */
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

    /**
     * Method for handling an individual update.
     * Iterates through the list of handlers and calls their appliesTo and handle methods.
     *
     * @param update The update to handle.
     */
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