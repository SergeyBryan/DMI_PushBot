package org.example.handlers;

import com.pengrad.telegrambot.model.Update;
import org.example.messenger.Messenger;
import org.example.service.RequestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
/**
 * Handler class for handling requests related to Excel processing.
 */
@Component
@Order(100)
public class ExcelHandler extends AbstractHandler {
    private final RequestService requestService;
    private final Logger logger = LoggerFactory.getLogger(ExcelHandler.class);
    /**
     * Constructs an ExcelHandler instance with the given RequestService dependency.
     *
     * @param requestService The RequestService instance to use for request-related operations.
     */
    public ExcelHandler(RequestService requestService) {
        this.requestService = requestService;
    }
    /**
     * Checks if this handler is applicable to the given update.
     *
     * @param update The update to check.
     * @return true if the handler is applicable, false otherwise.
     */
    @Override
    public boolean appliesTo(Update update) {
        if (update.callbackQuery() != null) {
            return false;
        }
        if (update.message() != null) {
            return update.message().text().equals("тест эксель");
        }
        return false;
    }
    /**
     * Handles the given update.
     *
     * @param update The update to handle.
     */
    @Override
    public void handle(Update update) {
        try {
            long chatId = update.message().chat().id();
            File file = requestService.requestsToExcel();
            Messenger.sendFile(chatId, telegramBot, file);
        } catch (IOException e) {
            logger.error("Failed to create Excel file");
            e.getStackTrace();
        }
    }
}
