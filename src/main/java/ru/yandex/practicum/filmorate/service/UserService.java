package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.*;

@Service
public class UserService {
    private final UserStorage userStorage;

    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public void addFriend(int userId, int friendId) {
        User user = getUserOrThrow(userId);
        User friend = getUserOrThrow(friendId);

        user.addFriend(friendId);
        friend.addFriend(userId);
    }

    public void removeFriend(int userId, int friendId) {
        User user = getUserOrThrow(userId);
        User friend = getUserOrThrow(friendId);
        user.removeFriend(friendId);
        friend.removeFriend(userId);
    }

    public Collection<User> getFriends(int userId) {
        User user = getUserOrThrow(userId);
        return getUsers(user.getFriends());
    }

    public Collection<User> getCommonFriends(int userId, int otherUserId) {
        var friends = new HashSet<>(getUserOrThrow(userId).getFriends());
        friends.retainAll(getUserOrThrow(otherUserId).getFriends());

        return getUsers(friends);
    }

    private Collection<User> getUsers(Collection<Integer> ids) {
        var result = new ArrayList<User>();
        for (int id : ids) {
            result.add(userStorage.find(id));
        }

        return result;
    }

    private User getUserOrThrow(int userId) {
        User user = userStorage.find(userId);
        if (user == null) {
            throw new UserNotFoundException(userId);
        }
        return user;
    }

    public List<User> getAll() {
        return userStorage.getAll();
    }

    public User find(int id) {
        return userStorage.find(id);
    }

    public User add(User user) {
        return userStorage.add(user);
    }

    public User update(User user) {
        return userStorage.update(user);
    }
}
