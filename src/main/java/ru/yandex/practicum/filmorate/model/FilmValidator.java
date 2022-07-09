package ru.yandex.practicum.filmorate.model;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Slf4j
@Service
public class FilmValidator {
    private static final int MAX_DESCRIPTION_LENGTH = 200;
    private static final LocalDate MIN_DATE = ValidationHelper.parseDate("1895-12-28");

    public void validate(Film film) throws ValidationException {
        String message = getError(film);
        if (message != null) {
            log.error(message);
            throw new ValidationException(message);
        }
    }

    private static String getError(Film film) {
        if (film.getName().length() == 0) {
            return "Имя не может быть пустым";
        }

        if (film.getDescription() != null && film.getDescription().length() > MAX_DESCRIPTION_LENGTH) {
            return "Описание не может быть длиннее "
                    + MAX_DESCRIPTION_LENGTH
                    + " символов. Длина: "
                    + film.getDescription().length();
        }

        if (film.getReleaseDate().isBefore(MIN_DATE)) {
            return "Дата выхода не может быть раньше чем "
                    + ValidationHelper.formatDate(MIN_DATE)
                    + ". Значение: "
                    + ValidationHelper.formatDate(film.getReleaseDate());
        }

        if (film.getDuration() < 0) {
            return "Продолжительность не может быть отрицательной. Значение: " + film.getDuration();
        }

        return null;
    }
}
