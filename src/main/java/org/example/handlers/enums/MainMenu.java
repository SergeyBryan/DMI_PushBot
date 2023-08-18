package org.example.handlers.enums;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public enum MainMenu {

    STOCK_INFO("Посмотреть сток", "Здесь информация"),
    HOW_TO_PUSH("Сделать пуш", "Здесь информация как сделать пуш"),
    SPORT_INFO("Информация по спорту", "Здесь информация по спорту");

    private final String key;
    private final String value;

    MainMenu(String key, String value) {
        this.key = key;
        this.value = value;
    }

}
