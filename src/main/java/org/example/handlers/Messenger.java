package org.example.handlers;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.*;
import com.pengrad.telegrambot.request.EditMessageReplyMarkup;
import com.pengrad.telegrambot.request.EditMessageText;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class Messenger {

    private final static Logger logger = LoggerFactory.getLogger(Messenger.class);

    public static void sendMessage(long chatId, String text, TelegramBot telegramBot) {
        SendMessage sendMessage = new SendMessage(chatId, text);
        SendResponse response = telegramBot.execute(sendMessage);
        responseStatus(chatId, response);
    }

    public static void sendButtonMessage(long chatId, String message, TelegramBot telegramBot, List<String> list) {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        list.forEach(s -> markup.addRow(new InlineKeyboardButton(s).callbackData("/" + s)));
//        markup.addRow(buttons.toArray(new InlineKeyboardButton[0]));
        SendMessage sendMessage = new SendMessage(chatId, message).replyMarkup(markup);
        SendResponse response = telegramBot.execute(sendMessage);
        responseStatus(chatId, response);
    }

    private static void responseStatus(long chatId, SendResponse response) {
        if (response.isOk()) {
            logger.info("Сообщение успешно отправлено пользователю " + chatId);
        } else {
            logger.error("Сообщение пользователю " + chatId + " не было отправлено");
        }
    }

    public static void editButton(Update update, TelegramBot telegramBot) {
        int message = update.callbackQuery().message().messageId();
        long chatId = update.callbackQuery().message().chat().id();
        String text = update.callbackQuery().data();
//        EditMessageText editMessageText = new EditMessageText(chatId, message, text);
        EditMessageReplyMarkup edit = new EditMessageReplyMarkup(chatId, message);
//        telegramBot.execute(editMessageText);
        telegramBot.execute(edit);
    }
    public static void editAll(Update update, TelegramBot telegramBot, List<String> list) {
        int message = update.callbackQuery().message().messageId();
        long chatId = update.callbackQuery().message().chat().id();
        EditMessageText editMessageText = new EditMessageText(chatId, message, null);
        EditMessageReplyMarkup edit = new EditMessageReplyMarkup(chatId, message);
        telegramBot.execute(editMessageText);
        telegramBot.execute(edit);
    }
    public static void editMessage(Update update, TelegramBot telegramBot) {
        int message = update.callbackQuery().message().messageId();
        long chatId = update.callbackQuery().message().chat().id();
        String text = update.callbackQuery().data();
        EditMessageText editMessageText = new EditMessageText(chatId, message, null);
//        EditMessageReplyMarkup edit = new EditMessageReplyMarkup(chatId, message);
        telegramBot.execute(editMessageText);
//        telegramBot.execute(edit);
    }
}
