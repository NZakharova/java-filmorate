package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.*;

@Service
public class UserService {
    private final UserStorage userStorage;

    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    private User replaceEmptyName(User user) {
        if (user.getName() == null || user.getName().length() == 0) {
            return user.toBuilder().name(user.getLogin()).build();
        } else {
            return user;
        }
    }

    public void addFriend(int userId, int friendId) {
        userStorage.find(userId);
        userStorage.find(friendId);

        userStorage.addFriend(userId, friendId);
    }

    public void removeFriend(int userId, int friendId) {
        userStorage.find(userId);
        userStorage.find(friendId);

        userStorage.removeFriend(userId, friendId);
    }

    public Collection<User> getFriends(int userId) {
        userStorage.find(userId);
        return getUsers(userStorage.getFriends(userId));
    }

    public Collection<User> getCommonFriends(int userId, int otherUserId) {
        var friends = new HashSet<>(userStorage.getFriends(userId));
        friends.retainAll(userStorage.getFriends(otherUserId));

        return getUsers(friends);
    }

    private Collection<User> getUsers(Collection<Integer> ids) {
        var result = new ArrayList<User>();
        for (int id : ids) {
            result.add(userStorage.find(id));
        }

        return result;
    }

    public List<User> getAll() {
        return userStorage.getAll();
    }

    public User find(int id) {
        return userStorage.find(id);
    }

    public int add(User user) {
        return userStorage.add(replaceEmptyName(user));
    }

    public void update(User user) {
        userStorage.update(replaceEmptyName(user));
    }
}
