package ru.yandex.practicum.filmorate.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FilmValidationTests {

    private static final String VALID_NAME = "Titanic";
    private static final String VALID_RELEASE_DATE = "1997-12-19";
    private static final int VALID_DURATION = 220;

    private static void runFailTest(String message, Film film) {
        var ex = assertThrows(ValidationException.class, film::validate);
        assertEquals(message, ex.getMessage());
    }

    private static void runSuccessTest(Film film) {
        assertDoesNotThrow(film::validate);
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
        runFailTest("Описание не может быть длиннее 200 символов",
                new Film(VALID_NAME, "a".repeat(201), VALID_RELEASE_DATE, VALID_DURATION));

        runFailTest("Описание не может быть длиннее 200 символов",
                new Film(VALID_NAME, "a".repeat(220), VALID_RELEASE_DATE, VALID_DURATION));

        runSuccessTest(new Film(VALID_NAME, "a".repeat(200), VALID_RELEASE_DATE, VALID_DURATION));
        runSuccessTest(new Film(VALID_NAME, "a".repeat(199), VALID_RELEASE_DATE, VALID_DURATION));
        runSuccessTest(new Film(VALID_NAME, "", VALID_RELEASE_DATE, VALID_DURATION));
    }

    @Test
    void testReleaseDate() {
        runFailTest("Дата выхода не может быть раньше чем 1895-12-28",
                new Film(VALID_NAME, "", "1895-12-27", VALID_DURATION));
        runFailTest("Дата выхода не может быть раньше чем 1895-12-28",
                new Film(VALID_NAME, "", "1800-12-27", VALID_DURATION));

        runSuccessTest(new Film(VALID_NAME, "", "1895-12-29", VALID_DURATION));
        runSuccessTest(new Film(VALID_NAME, "", "1895-12-30", VALID_DURATION));
        runSuccessTest(new Film(VALID_NAME, "", "2020-12-30", VALID_DURATION));
    }

    @Test
    void testDuration() {
        runFailTest("Продолжительность не может быть отрицательной",
                new Film(VALID_NAME, "", VALID_RELEASE_DATE, -1));

        runFailTest("Продолжительность не может быть отрицательной",
                new Film(VALID_NAME, "", VALID_RELEASE_DATE, -2));

        runSuccessTest(new Film(VALID_NAME, "", VALID_RELEASE_DATE, 0));
        runSuccessTest(new Film(VALID_NAME, "", VALID_RELEASE_DATE, 1));
        runSuccessTest(new Film(VALID_NAME, "", VALID_RELEASE_DATE, 100));
        runSuccessTest(new Film(VALID_NAME, "", VALID_RELEASE_DATE, 1200));
    }
}
