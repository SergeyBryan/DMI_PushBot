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
/**
 * The Messenger class provides utility methods for sending messages and files using the Telegram Bot API.
 */
public class Messenger {

    private final static Logger logger = LoggerFactory.getLogger(Messenger.class);
    /**
     * Sends a text message to a specified chat.
     * @param chatId The ID of the chat to send the message to.
     * @param text The text of the message.
     * @param telegramBot The TelegramBot instance to use for sending the message.
     */
    public static void sendMessage(long chatId, String text, TelegramBot telegramBot) {
        SendMessage sendMessage = new SendMessage(chatId, text).parseMode(ParseMode.HTML);
        SendResponse response = telegramBot.execute(sendMessage);
        responseStatus(chatId, response);
    }
    /**
     * Sends a message with inline buttons to a specified chat.
     * @param chatId The ID of the chat to send the message to.
     * @param message The message text.
     * @param telegramBot The TelegramBot instance to use for sending the message.
     * @param list The list of button labels.
     */
    public static void sendButtonMessage(long chatId, String message, TelegramBot telegramBot, List<String> list) {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        list.forEach(s -> markup.addRow(new InlineKeyboardButton(s).callbackData("/" + s)));
        SendMessage sendMessage = new SendMessage(chatId, message).replyMarkup(markup);
        SendResponse response = telegramBot.execute(sendMessage);
        responseStatus(chatId, response);
    }
    /**
     * Checks the response status of a sent message and logs the appropriate message.
     * @param chatId The ID of the chat the message was sent to.
     * @param response The SendResponse object containing information about the message sending status.
     */
    private static void responseStatus(long chatId, SendResponse response) {
        if (response.isOk()) {
            logger.info("Message successfully sent to user " + chatId);
        } else {
            logger.error("Message to user " + chatId + " was not sent");
        }
    }
    /**
     * Deletes a previous message in the chat.
     * @param chatId The ID of the chat.
     * @param telegramBot The TelegramBot instance to use for deleting the message.
     * @param update The update containing the callbackQuery used to identify the message to delete.
     */
    public static void editPast(long chatId, TelegramBot telegramBot, Update update) {
        DeleteMessage deleteMessage = new DeleteMessage(chatId, update.callbackQuery().message().messageId());
        telegramBot.execute(deleteMessage);
    }
    /**
     * Sends a file to the chat.
     * @param chatId The ID of the chat to send the file to.
     * @param telegramBot The TelegramBot instance to use for sending the file.
     * @param file The file to send.
     */
    public static void sendFile(long chatId, TelegramBot telegramBot, File file) {
        SendDocument sendDocument = new SendDocument(chatId, file);
        SendResponse response = telegramBot.execute(sendDocument);
        responseStatus(chatId, response);
    }
    /**
     * Sends a photo to the chat.
     * @param chatId The ID of the chat to send the photo to.
     * @param telegramBot The TelegramBot instance to use for sending the photo.
     * @param file The photo file to send.
     */
    public static void sendPhoto(long chatId, TelegramBot telegramBot, File file) {
        SendPhoto sendPhoto = new SendPhoto(chatId, file);
        SendResponse response = telegramBot.execute(sendPhoto);
        responseStatus(chatId, response);
    }
}
