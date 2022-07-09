package ru.yandex.practicum.filmorate.service.exceptions;

public class UserNotFoundException extends ObjectNotFoundException {
    public UserNotFoundException(int id) {
        super("Пользователь не найден, id=" + id);
    }
}
