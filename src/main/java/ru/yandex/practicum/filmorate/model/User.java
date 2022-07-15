package ru.yandex.practicum.filmorate.model;

import lombok.*;

import java.time.LocalDate;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class User {
    private final int id;
    @NonNull
    private final String email;
    @NonNull
    private final String login;
    private final String name;
    @NonNull
    private final LocalDate birthday;
}
