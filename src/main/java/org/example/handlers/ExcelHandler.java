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

@Component
@Order(100)
public class ExcelHandler extends AbstractHandler {
    private final RequestService requestService;
    private final Logger logger = LoggerFactory.getLogger(ExcelHandler.class);

    public ExcelHandler(RequestService requestService) {
        this.requestService = requestService;
    }

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
