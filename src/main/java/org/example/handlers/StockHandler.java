package org.example.handlers;

import com.pengrad.telegrambot.model.Update;
import org.example.handlers.enums.MainMenu;
import org.example.messenger.Messenger;
import org.example.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class StockHandler extends AbstractHandler {

    String HOW_TO_CHECK = "В данный момент вы переключились на функцию просмотра стока. \n" +
            "Введите любой артикул в чате, чтобы посмотреть наличие стока.\n" +
            "Чтобы выйти из функции просмотра стока введите /start.";

    Logger logger = LoggerFactory.getLogger(StockHandler.class);
    private StockModeHandler checkStockHandler;
    UserService userService;

    public StockHandler(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean appliesTo(Update update) {
        if (update.message() != null) {
            return false;
        }
        return update.callbackQuery() != null && update.callbackQuery().data().substring(1).equals(MainMenu.STOCK_INFO.getKey());
    }

    @Override
    public void handle(Update update) {
        logger.info("Схватывает");
        long chatId = update.callbackQuery().message().chat().id();
        Messenger.editPast(chatId, telegramBot, update);
        Messenger.sendMessage(chatId, HOW_TO_CHECK, telegramBot);
//        count = 1;
        userService.userChangeStatus(chatId, 1);
        checkStockHandler.handle(update);
    }
}
