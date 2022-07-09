package ru.yandex.practicum.filmorate.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import static ru.yandex.practicum.filmorate.model.ValidationHelper.parseDate;

import static org.junit.jupiter.api.Assertions.*;

class FilmValidationTests {
    private static final String VALID_NAME = "Titanic";
    private static final LocalDate VALID_RELEASE_DATE = parseDate("1997-12-19");
    private static final int VALID_DURATION = 220;

    private static final FilmValidator VALIDATOR = new FilmValidator();

    private static void runFailTest(String message, Film film) {
        var ex = assertThrows(ValidationException.class, () -> VALIDATOR.validate(film));
        assertEquals(message, ex.getMessage());
    }

    private static void runSuccessTest(Film film) {
        assertDoesNotThrow(() -> VALIDATOR.validate((film)));
    }

    @Test
    void testDoesNotThrowWithValidInput() {
        runSuccessTest(new Film(VALID_NAME, "", VALID_RELEASE_DATE, VALID_DURATION));
    }

    @Test
    void testName() {
        runFailTest("Имя не может быть пустым",
                new Film("", "", VALID_RELEASE_DATE, VALID_DURATION));
    }

    @Test
    void testDescription() {
        runFailTest("Описание не может быть длиннее 200 символов. Длина: 201",
                new Film(VALID_NAME, "a".repeat(201), VALID_RELEASE_DATE, VALID_DURATION));

        runFailTest("Описание не может быть длиннее 200 символов. Длина: 220",
                new Film(VALID_NAME, "a".repeat(220), VALID_RELEASE_DATE, VALID_DURATION));

        runSuccessTest(new Film(VALID_NAME, "a".repeat(200), VALID_RELEASE_DATE, VALID_DURATION));
        runSuccessTest(new Film(VALID_NAME, "a".repeat(199), VALID_RELEASE_DATE, VALID_DURATION));
        runSuccessTest(new Film(VALID_NAME, "", VALID_RELEASE_DATE, VALID_DURATION));
    }

    @Test
    void testReleaseDate() {
        runFailTest("Дата выхода не может быть раньше чем 1895-12-28. Значение: 1895-12-27",
                new Film(VALID_NAME, "", parseDate("1895-12-27"), VALID_DURATION));
        runFailTest("Дата выхода не может быть раньше чем 1895-12-28. Значение: 1800-12-27",
                new Film(VALID_NAME, "", parseDate("1800-12-27"), VALID_DURATION));

        runSuccessTest(new Film(VALID_NAME, "", parseDate("1895-12-29"), VALID_DURATION));
        runSuccessTest(new Film(VALID_NAME, "", parseDate("1895-12-30"), VALID_DURATION));
        runSuccessTest(new Film(VALID_NAME, "", parseDate("2020-12-30"), VALID_DURATION));
    }

    @Test
    void testDuration() {
        runFailTest("Продолжительность не может быть отрицательной. Значение: -1",
                new Film(VALID_NAME, "", VALID_RELEASE_DATE, -1));

        runFailTest("Продолжительность не может быть отрицательной. Значение: -2",
                new Film(VALID_NAME, "", VALID_RELEASE_DATE, -2));

        runSuccessTest(new Film(VALID_NAME, "", VALID_RELEASE_DATE, 0));
        runSuccessTest(new Film(VALID_NAME, "", VALID_RELEASE_DATE, 1));
        runSuccessTest(new Film(VALID_NAME, "", VALID_RELEASE_DATE, 100));
        runSuccessTest(new Film(VALID_NAME, "", VALID_RELEASE_DATE, 1200));
    }
}
