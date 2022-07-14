package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {
    User add(User film);

    User remove(int id);

    User update(User film);

    List<User> getAll();

    User find(int id);
}