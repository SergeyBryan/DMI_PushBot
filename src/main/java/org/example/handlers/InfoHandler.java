package org.example.handlers;

import com.pengrad.telegrambot.model.Update;
import org.example.handlers.enums.Departments;
import org.example.handlers.enums.Info;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class InfoHandler extends AbstractHandler {

    private final Logger logger = LoggerFactory.getLogger(InfoHandler.class);


    @Override
    public boolean appliesTo(Update update) {
        if (update.message() != null) {
            return false;
        }
        if (update.callbackQuery() != null) {
            logger.info("Иттерация по листу...");
            if (infoMenu.containsKey((update.callbackQuery().data().substring(1))) || update.callbackQuery().data().substring(1).equals(BACK_TO_SPORT)) {
                logger.info("Успешно");
                return true;
            }
        }
        return false;
//        return count==1;
    }

    @Override
    public void handle(Update update) {
        long chatId = update.callbackQuery().message().chat().id();
        String text = update.callbackQuery().data().substring(1);
        if (Info.SPORT_INFO.getKey().equals(text) || text.equals(BACK_TO_SPORT)) {
            List<String> list = new ArrayList<>(getDepartmentList());
            list.add(BACK_TO_INFO);
            logger.info("Проверяем в инфоХендлере что отправить");
            Messenger.sendButtonMessage(chatId, "Выберите спорт", telegramBot,
//                    new ArrayList<>(departmentMenu.keySet()));
                    new ArrayList<>(list));
//                    List.of("Назад в информационное меню")
        }
    }
}
