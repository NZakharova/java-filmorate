package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.ValidationException;

import java.util.List;

public interface UserStorage {
    User add(User film) throws ValidationException;

    User remove(int id);

    User update(User film) throws ValidationException;

    List<User> getAll();

    User find(int id);
}