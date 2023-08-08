package org.example.handlers.enums;

import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@ToString
@Getter
public enum Departments {

    INLINE("Ролики, коньки, самокаты", "Привет..."),
    HIKING("Походы", "Привет...походы"),
    NATURE("Охота, рыбалка, конный спорт", "Привет...охота"),
    TEAM_SPORT("Командные виды спорта", "Привет"),
    RACKET_SPORT("Спорт с ракетками", "Привет"),
    WATER_SPORT("Водные виды спорта", "Привет");

    private final String key;
    private final String value;

    Departments(String key, String value) {
        this.key = key;
        this.value = value;
    }

}
