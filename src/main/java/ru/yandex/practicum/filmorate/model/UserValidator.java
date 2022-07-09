package ru.yandex.practicum.filmorate.model;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

@Slf4j
@Service
public class UserValidator {

    public void validate(User user) throws ValidationException {
        String message = getError(user);
        if (message != null) {
            log.error(message);
            throw new ValidationException(message);
        }
    }

    private static String getError(User user) {
        if (user.getEmail().length() == 0) {
            return "Электронная почта не может быть пустой";
        }

        if (user.getEmail().indexOf('@') == -1) {
            return "Электронная почта должна содержать символ '@'. Значение: " + user.getEmail();
        }

        if (user.getLogin().length() == 0) {
            return "Логин не может быть пустым";
        }

        if (user.getLogin().indexOf(' ') != -1) {
            return "Логин не может содержать пробелы. Значение: " + user.getLogin();
        }

        if (user.getBirthday().isAfter(LocalDate.ofInstant(Instant.now(), ZoneId.systemDefault()))) {
            return "День рождения не может быть после текущей даты. Значение: "
                    + ValidationHelper.formatDate(user.getBirthday());
        }

        return null;
    }
}
