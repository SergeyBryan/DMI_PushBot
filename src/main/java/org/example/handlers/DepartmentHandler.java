package org.example.handlers;

import com.pengrad.telegrambot.model.Update;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


@Component
public class DepartmentHandler extends AbstractHandler {

    private final Logger LOG = LoggerFactory.getLogger(DepartmentHandler.class);

    @Override
    public boolean appliesTo(Update update) {
        if (update.message() != null) {
            return false;
        }
        if (update.callbackQuery() != null) {
            LOG.info("Иттерация по листу");
//            if (departmentList.contains(update.callbackQuery().data().substring(1))) {
            if (departmentList.contains(update.callbackQuery().data().substring(1))) {
//            if (Departments.HIKING.getKey().equals(update.callbackQuery().data().substring(1))) {
                LOG.info("Успешно");
                return true;
            }
        }
        return false;
    }


    @Override
    public void handle(Update update) {
        long chatId = update.callbackQuery().message().chat().id();
        String text = update.callbackQuery().data().substring(1);
        List<String> list = new ArrayList<>(departmentList);
        list.add(BACK_TO_SPORT);
        if (departmentMenu.containsKey(text)) {
            Messenger.sendButtonMessage(chatId, departmentMenu.get(text), telegramBot,
//                    List.of("Назад в меню")
                    Collections.singletonList(BACK_TO_SPORT)
            );
        }
//        LOG.info("{}", text.substring(1));
//        LOG.info("Метод дошёл до сюда");
    }
}
