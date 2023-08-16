package org.example.handlers;

import com.pengrad.telegrambot.model.Update;
import org.example.handlers.enums.Departments;
import org.example.handlers.enums.Info;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class TestCheckStockHandler extends AbstractHandler {
    Logger logger = LoggerFactory.getLogger(TestCheckStockHandler.class);

    @Override
    public boolean appliesTo(Update update) {
        logger.info(Info.INFORMATION.getKey());
        if (update.callbackQuery() != null && update.callbackQuery().data().substring(1).equals(Info.INFORMATION.getKey())) {
            count = 1;
            logger.info("count has been changed");
            return true;
        }
        if (count == 1) {
            logger.info("count save");
            return true;
        }
        logger.info("false");
        return false;
    }

    @Override
    public void handle(Update update) {
        logger.info("Получилось");
    }
}
