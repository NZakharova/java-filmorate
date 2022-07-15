package ru.yandex.practicum.filmorate.model;

import lombok.Data;

@Data
public class Rating {
    private final int id;
    private final String name;
    private final String description;
}
