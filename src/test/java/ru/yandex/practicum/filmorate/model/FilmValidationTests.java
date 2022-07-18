package ru.yandex.practicum.filmorate.model;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.exceptions.ValidationException;

import java.time.LocalDate;
import java.util.Collections;

import static ru.yandex.practicum.filmorate.model.ValidationHelper.parseDate;

import static org.junit.jupiter.api.Assertions.*;

class FilmValidationTests {
    private static final String VALID_NAME = "Titanic";
    private static final LocalDate VALID_RELEASE_DATE = parseDate("1997-12-19");
    private static final int VALID_DURATION = 220;

    private final FilmValidator validator = new FilmValidator();

    @Test
    void testDoesNotThrowWithValidInput() {
        runSuccessTest(newFilm(VALID_NAME, "", VALID_RELEASE_DATE, VALID_DURATION));
    }

    @Test
    void testName() {
        runFailTest("Имя не может быть пустым",
                newFilm("", "", VALID_RELEASE_DATE, VALID_DURATION));
    }

    @Test
    void testDescription() {
        runFailTest("Описание не может быть длиннее 200 символов. Длина: 201",
                newFilm(VALID_NAME, "a".repeat(201), VALID_RELEASE_DATE, VALID_DURATION));

        runFailTest("Описание не может быть длиннее 200 символов. Длина: 220",
                newFilm(VALID_NAME, "a".repeat(220), VALID_RELEASE_DATE, VALID_DURATION));

        runSuccessTest(newFilm(VALID_NAME, "a".repeat(200), VALID_RELEASE_DATE, VALID_DURATION));
        runSuccessTest(newFilm(VALID_NAME, "a".repeat(199), VALID_RELEASE_DATE, VALID_DURATION));
        runSuccessTest(newFilm(VALID_NAME, "", VALID_RELEASE_DATE, VALID_DURATION));
    }

    @Test
    void testReleaseDate() {
        runFailTest("Дата выхода не может быть раньше чем 1895-12-28. Значение: 1895-12-27",
                newFilm(VALID_NAME, "", parseDate("1895-12-27"), VALID_DURATION));
        runFailTest("Дата выхода не может быть раньше чем 1895-12-28. Значение: 1800-12-27",
                newFilm(VALID_NAME, "", parseDate("1800-12-27"), VALID_DURATION));

        runSuccessTest(newFilm(VALID_NAME, "", parseDate("1895-12-29"), VALID_DURATION));
        runSuccessTest(newFilm(VALID_NAME, "", parseDate("1895-12-30"), VALID_DURATION));
        runSuccessTest(newFilm(VALID_NAME, "", parseDate("2020-12-30"), VALID_DURATION));
    }

    @Test
    void testDuration() {
        runFailTest("Продолжительность не может быть отрицательной. Значение: -1",
                newFilm(VALID_NAME, "", VALID_RELEASE_DATE, -1));

        runFailTest("Продолжительность не может быть отрицательной. Значение: -2",
                newFilm(VALID_NAME, "", VALID_RELEASE_DATE, -2));

        runSuccessTest(newFilm(VALID_NAME, "", VALID_RELEASE_DATE, 0));
        runSuccessTest(newFilm(VALID_NAME, "", VALID_RELEASE_DATE, 1));
        runSuccessTest(newFilm(VALID_NAME, "", VALID_RELEASE_DATE, 100));
        runSuccessTest(newFilm(VALID_NAME, "", VALID_RELEASE_DATE, 1200));
    }

    private void runFailTest(String message, Film film) {
        var ex = assertThrows(ValidationException.class, () -> validator.validate(film));
        assertEquals(message, ex.getMessage());
    }

    private void runSuccessTest(Film film) {
        assertDoesNotThrow(() -> validator.validate((film)));
    }

    private Film newFilm(String name, String description, LocalDate releaseDate, int duration) {
        return new Film(0, name, description, releaseDate, duration, 0, null, Collections.emptyList());
    }
}
