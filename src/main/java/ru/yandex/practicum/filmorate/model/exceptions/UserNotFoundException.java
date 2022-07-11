package ru.yandex.practicum.filmorate.model.exceptions;

public class UserNotFoundException extends ObjectNotFoundException {
    public UserNotFoundException(int id) {
        super("Пользователь не найден, id=" + id);
    }
}
