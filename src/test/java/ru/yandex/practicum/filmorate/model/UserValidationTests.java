package ru.yandex.practicum.filmorate.model;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.exceptions.ValidationException;

import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;
import static ru.yandex.practicum.filmorate.model.ValidationHelper.parseDate;

class UserValidationTests {
    private static final String VALID_EMAIL = "user@ya.ru";
    private static final String VALID_LOGIN = "user";
    private static final LocalDate VALID_BIRTHDAY = parseDate("1970-11-30");

    private final UserValidator validator = new UserValidator();

    private void runFailTest(String message, User user) {
        var ex = assertThrows(ValidationException.class, () -> validator.validate(user));
        assertEquals(message, ex.getMessage());
    }

    private void runSuccessTest(User user) {
        assertDoesNotThrow(() -> validator.validate(user));
    }

    @Test
    void testDoesNotThrowWithValidInput() {
        runSuccessTest(new User(VALID_EMAIL, VALID_LOGIN, VALID_BIRTHDAY));
    }

    @Test
    void testEmail() {
        runFailTest("Электронная почта не может быть пустой",
                new User("", VALID_LOGIN, VALID_BIRTHDAY));

        runFailTest("Электронная почта должна содержать символ '@'. Значение: a",
                new User("a", VALID_LOGIN, VALID_BIRTHDAY));

        runSuccessTest(new User("asd@zxc", VALID_LOGIN, VALID_BIRTHDAY));
    }

    @Test
    void testLogin() {
        runFailTest("Логин не может быть пустым",
                new User(VALID_EMAIL, "", VALID_BIRTHDAY));

        runFailTest("Логин не может содержать пробелы. Значение: User user",
                new User(VALID_EMAIL, "User user", VALID_BIRTHDAY));

        runFailTest("Логин не может содержать пробелы. Значение:  User",
                new User(VALID_EMAIL, " User", VALID_BIRTHDAY));

        runFailTest("Логин не может содержать пробелы. Значение: User ",
                new User(VALID_EMAIL, "User ", VALID_BIRTHDAY));
    }

    @Test
    void testBirthday() {
        runFailTest("День рождения не может быть после текущей даты. Значение: 2300-01-01",
                new User(VALID_EMAIL, VALID_LOGIN, parseDate("2300-01-01")));

        runSuccessTest(new User(VALID_EMAIL, VALID_LOGIN, parseDate("2022-01-01")));
    }
}
