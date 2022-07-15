package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {
    int add(User user);

    void remove(int id);

    void update(User user);

    List<User> getAll();

    User find(int id);

    void addFriend(int userId, int friendId);
    void removeFriend(int userId, int friendId);
    List<Integer> getFriends(int userId);
}