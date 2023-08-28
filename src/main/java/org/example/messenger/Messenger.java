package org.example.messenger;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.*;
import com.pengrad.telegrambot.request.DeleteMessage;
import com.pengrad.telegrambot.request.SendDocument;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SendPhoto;
import com.pengrad.telegrambot.response.SendResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.List;

public class Messenger {

    private final static Logger logger = LoggerFactory.getLogger(Messenger.class);

    public static void sendMessage(long chatId, String text, TelegramBot telegramBot) {
        SendMessage sendMessage = new SendMessage(chatId, text).parseMode(ParseMode.HTML);
        SendResponse response = telegramBot.execute(sendMessage);
        responseStatus(chatId, response);
    }

    public static void sendButtonMessage(long chatId, String message, TelegramBot telegramBot, List<String> list) {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        list.forEach(s -> markup.addRow(new InlineKeyboardButton(s).callbackData("/" + s)));
        SendMessage sendMessage = new SendMessage(chatId, message).replyMarkup(markup);
        SendResponse response = telegramBot.execute(sendMessage);
        responseStatus(chatId, response);
    }

    private static void responseStatus(long chatId, SendResponse response) {
        if (response.isOk()) {
            logger.info("Message successfully sent to user " + chatId);
        } else {
            logger.error("Message to user " + chatId + " was not sent");
        }
    }

    public static void editPast(long chatId, TelegramBot telegramBot, Update update) {
        DeleteMessage deleteMessage = new DeleteMessage(chatId, update.callbackQuery().message().messageId());
        telegramBot.execute(deleteMessage);
    }

    public static void sendFile(long chatId, TelegramBot telegramBot, File file) {
        SendDocument sendDocument = new SendDocument(chatId, file);
        SendResponse response = telegramBot.execute(sendDocument);
        responseStatus(chatId, response);
    }

    public static void sendPhoto(long chatId, TelegramBot telegramBot, File file) {
        SendPhoto sendPhoto = new SendPhoto(chatId, file);
        SendResponse response = telegramBot.execute(sendPhoto);
        responseStatus(chatId, response);
    }
}
