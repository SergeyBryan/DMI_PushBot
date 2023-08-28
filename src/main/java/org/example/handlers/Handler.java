package org.example.handlers;

import com.pengrad.telegrambot.model.Update;

public interface Handler {


    boolean appliesTo(Update update);

    void handle(Update update);

}
