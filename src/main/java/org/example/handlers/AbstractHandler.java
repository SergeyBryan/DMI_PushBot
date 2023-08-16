package org.example.handlers;

import com.pengrad.telegrambot.TelegramBot;
import lombok.Data;
import org.example.handlers.enums.Departments;
import org.example.handlers.enums.Info;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Data
public abstract class AbstractHandler implements Handler {

    protected static int count = 0;

    protected String BACK_TO_SPORT = "Назад к спортам";

    protected String BACK_TO_INFO = "Назад в меню";

    protected String PUSH_TEXT = "Чтобы сделать пуш отправь сообщение в формате: \n" +
            "\n<b>8371234 40 размер m</b>\n" +
            "\n<b>8371234</b>  ---->   код модели\n" +
            "<b>40</b>       ---->   количество штук\n" +
            "<b>размер m</b> ---->   комментарий о каком размере идёт речь.";

    protected Map<String, String> departmentMenu = Arrays.stream(Departments.values())
            .filter(departments -> !departments.getValue().isEmpty())
            .collect(Collectors.toMap(Departments::getKey, Departments::getValue));
    protected List<String> departmentList = new ArrayList<>(departmentMenu.keySet());


    protected Map<String, String> infoMenu = Arrays.stream(Info.values())
            .filter(info -> !info.getValue().isEmpty())
            .collect(Collectors.toMap(Info::getKey, Info::getValue));
    protected List<String> infoList = new ArrayList<>(infoMenu.keySet());

    @Autowired
    protected TelegramBot telegramBot;

}
