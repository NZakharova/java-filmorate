package ru.yandex.practicum.filmorate.model;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class Film {
    private final int id;

    @NonNull
    private final String name;
    private final String description;

    @NonNull
    private final LocalDate releaseDate;
    private final int duration;

    private final int rate;

    private final Rating mpa;

    private final List<Genre> genres;
}
