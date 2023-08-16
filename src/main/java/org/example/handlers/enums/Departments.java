package org.example.handlers.enums;

import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public enum Departments {

    INLINE("Ролики, коньки, самокаты", "Здесь собрана вся необходимая информация по спортам ролики самокаты скейтборды, \n" +
            "ДАО планы можно найти здесь: \n" +
            "\nhttps://docs.google.com/spreadsheets/d/15NDuT-8R9UEJyZFDg1FAFDCD3fydavEKExM9LrjCwlE/edit#gid=932408156\n" +
            "\nИнформацию по выбору гамм можно найти здесь: \n" +
            "<\nhttps://yandex.com"
    ),
    HIKING(
            "Походы", "Здесь собрана вся необходимая информация по спортам походы, кемпинг\n" +
            "ДАО планы можно найти здесь: \n" +
            "\nссылка\n" +
            "\nИнформацию по выбору гамм можно найти здесь: \n" +
            "<\nhttps://yandex.com"
    ),
    NATURE("Охота, рыбалка, конный спорт", "Здесь собрана вся необходимая информация по спортам охота, рыбалка, конный спорт\n" +
            "ДАО планы можно найти здесь: \n" +
            "\nссылка\n" +
            "\nИнформацию по выбору гамм можно найти здесь: \n" +
            "<\nhttps://yandex.com"),
    TEAM_SPORT("Командные виды спорта", "Здесь собрана вся необходимая информация по командным видам спорта\n" +
            "ДАО планы можно найти здесь: \n" +
            "\nссылка\n" +
            "\nИнформацию по выбору гамм можно найти здесь: \n" +
            "<\nhttps://yandex.com"),
    RACKET_SPORT("Спорт с ракетками", "Здесь собрана вся необходимая информация по спорту с ракетками\n" +
            "ДАО планы можно найти здесь: \n" +
            "\nссылка\n" +
            "\nИнформацию по выбору гамм можно найти здесь: \n" +
            "<\nhttps://yandex.com"),
    WATER_SPORT("Водные виды спорта", "Здесь собрана вся необходимая информация по водным видам спорта\n" +
            "ДАО планы можно найти здесь: \n" +
            "\nссылка\n" +
            "\nИнформацию по выбору гамм можно найти здесь: \n" +
            "<\nhttps://yandex.com"),
    FITNESS("Фитнес", "Здесь собрана вся необходимая информация по фитнесу\n" +
            "ДАО планы можно найти здесь: \n" +
            "\nссылка\n" +
            "\nИнформацию по выбору гамм можно найти здесь: \n" +
            "<\nhttps://yandex.com"),
    RUNNING("Бег, ходьба", "Здесь собрана вся необходимая информация по спортам бег, ходьба\n" +
            "ДАО планы можно найти здесь: \n" +
            "\nссылка\n" +
            "\nИнформацию по выбору гамм можно найти здесь: \n" +
            "<\nhttps://yandex.com");

    private final String key;
    private final String value;

    Departments(String key, String value) {
        this.key = key;
        this.value = value;
    }

}
