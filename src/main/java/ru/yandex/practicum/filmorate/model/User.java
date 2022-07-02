package ru.yandex.practicum.filmorate.model;

import java.time.Instant;
import java.util.Date;

import lombok.*;

@Data
public class User {

    private int id;
    @NonNull
    private final String email;
    @NonNull
    private final String login;
    private String name;
    @NonNull
    private final String birthday;

    public void validate() throws ValidationException {
        if (email.length() == 0) {
            throw new ValidationException("Электронная почта не может быть пустой");
        }

        if (email.indexOf('@') == -1) {
            throw new ValidationException("Электронная почта должна содержать символ '@'");
        }

        if (login.length() == 0) {
            throw new ValidationException("Логин не может быть пустым");
        }

        if (login.indexOf(' ') != -1) {
            throw new ValidationException("Логин не может содержать пробелы");
        }

        if (ValidationHelper.parseDate(birthday).after(Date.from(Instant.now()))) {
            throw new ValidationException("День рождения не может быть после текущей даты");
        }
    }
}
