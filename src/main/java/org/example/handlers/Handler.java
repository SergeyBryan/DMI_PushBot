package org.example.handlers;

import com.pengrad.telegrambot.model.Update;
/**
 * Interface for implementing a handler.
 */
public interface Handler {


    boolean appliesTo(Update update);

    void handle(Update update);

}
