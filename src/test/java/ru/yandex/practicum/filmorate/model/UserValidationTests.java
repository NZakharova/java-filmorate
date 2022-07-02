package ru.yandex.practicum.filmorate.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserValidationTests {
    private static final String VALID_EMAIL = "user@ya.ru";
    private static final String VALID_LOGIN = "user";
    private static final String VALID_BIRTHDAY = "1970-11-30";

    private static void runFailTest(String message, User user) {
        var ex = assertThrows(ValidationException.class, user::validate);
        assertEquals(message, ex.getMessage());
    }

    private static void runSuccessTest(User user) {
        assertDoesNotThrow(user::validate);
    }

    @Test
    void testDoesNotThrowWithValidInput() {
        runSuccessTest(new User(VALID_EMAIL, VALID_LOGIN, VALID_BIRTHDAY));
    }

    @Test
    void testEmail() {
        runFailTest("Электронная почта не может быть пустой",
                new User("", VALID_LOGIN, VALID_BIRTHDAY));

        runFailTest("Электронная почта должна содержать символ '@'",
                new User("a", VALID_LOGIN, VALID_BIRTHDAY));

        runSuccessTest(new User("asd@zxc", VALID_LOGIN, VALID_BIRTHDAY));
    }

    @Test
    void testLogin() {
        runFailTest("Логин не может быть пустым",
                new User(VALID_EMAIL, "", VALID_BIRTHDAY));

        runFailTest("Логин не может содержать пробелы",
                new User(VALID_EMAIL, "User user", VALID_BIRTHDAY));

        runFailTest("Логин не может содержать пробелы",
                new User(VALID_EMAIL, " User", VALID_BIRTHDAY));

        runFailTest("Логин не может содержать пробелы",
                new User(VALID_EMAIL, "User ", VALID_BIRTHDAY));
    }

    @Test
    void testBirthday() {
        runFailTest("День рождения не может быть после текущей даты",
                new User(VALID_EMAIL, VALID_LOGIN, "2300-01-01"));

        runSuccessTest(new User(VALID_EMAIL, VALID_LOGIN, "2022-01-01"));
    }
}
