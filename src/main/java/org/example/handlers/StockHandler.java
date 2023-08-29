package org.example.handlers;

import com.pengrad.telegrambot.model.Update;
import org.example.handlers.enums.MainMenu;
import org.example.messenger.Messenger;
import org.example.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
/**
 * Stock Handler class for handling stock-related requests.
 * This is Spring boot component and extends from AbstractHandler.
 */
@Component
public class StockHandler extends AbstractHandler {

    private final String HOW_TO_CHECK = "В данный момент вы переключились на функцию просмотра стока. \n" +
            "Введите любой артикул в чате, чтобы посмотреть наличие стока.\n" +
            "Чтобы выйти из функции просмотра стока введите /start.";

    private final Logger logger = LoggerFactory.getLogger(StockHandler.class);
    private final StockModeHandler checkStockHandler;
    private final UserService userService;

    public StockHandler(StockModeHandler checkStockHandler, UserService userService) {
        this.checkStockHandler = checkStockHandler;
        this.userService = userService;
    }
    /**
     * Checks if this handler is applicable to the given update.
     *
     * @param update The update to check.
     * @return true if the handler is applicable, false otherwise.
     */
    @Override
    public boolean appliesTo(Update update) {
        if (update.message() != null) {
            return false;
        }
        String message = update.callbackQuery().data().substring(1);
        return update.callbackQuery() != null &&
                message.equals(MainMenu.STOCK_INFO.getKey());
    }
    /**
     * Handles the given update.
     *
     * @param update The update to handle.
     */
    @Override
    public void handle(Update update) {
        long chatId = update.callbackQuery().message().chat().id();

        Messenger.editPast(chatId, telegramBot, update);
        Messenger.sendMessage(chatId, HOW_TO_CHECK, telegramBot);
        userService.userChangeStatus(chatId, 1);
        checkStockHandler.handle(update);
    }
}
