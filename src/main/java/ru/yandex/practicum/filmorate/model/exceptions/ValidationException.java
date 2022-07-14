package ru.yandex.practicum.filmorate.model.exceptions;

public class ValidationException extends RuntimeException {
    public ValidationException(String message) {
        super(message);
    }

    public ValidationException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
