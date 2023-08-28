package org.example.handlers;

import com.pengrad.telegrambot.TelegramBot;
import lombok.Data;
import org.example.handlers.enums.Departments;
import org.example.handlers.enums.MainMenu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
@Data
public abstract class AbstractHandler implements Handler {

    protected String BACK_TO_SPORT = "Назад к спортам";

    protected String BACK_TO_INFO = "Назад в меню";

    protected Map<String, String> departmentMenu = Arrays.stream(Departments.values())
            .filter(departments -> !departments.getValue().isEmpty())
            .collect(Collectors.toMap(Departments::getKey, Departments::getValue));
    protected List<String> departmentList = new ArrayList<>(departmentMenu.keySet());


    protected Map<String, String> infoMenu = Arrays.stream(MainMenu.values())
            .filter(info -> !info.getValue().isEmpty())
            .collect(Collectors.toMap(MainMenu::getKey, MainMenu::getValue));
    protected List<String> infoList = new ArrayList<>(infoMenu.keySet());

    @Autowired
    protected TelegramBot telegramBot;

}
