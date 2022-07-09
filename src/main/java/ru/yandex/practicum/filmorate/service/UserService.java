package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.*;

@Service
public class UserService {
    private final UserStorage userStorage;

    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public void addFriend(int userId, int friendId) throws UserNotFoundException {
        User user = getUserOrThrow(userId);
        User friend = getUserOrThrow(friendId);

        user.addFriend(friendId);
        friend.addFriend(userId);
    }

    public void removeFriend(int userId, int friendId) throws UserNotFoundException {
        User user = getUserOrThrow(userId);
        User friend = getUserOrThrow(friendId);
        user.removeFriend(friendId);
        friend.removeFriend(userId);
    }

    public Collection<User> getFriends(int userId) throws UserNotFoundException {
        User user = getUserOrThrow(userId);
        return getUsers(user.getFriends());
    }

    public Collection<User> getCommonFriends(int userId, int otherUserId) throws UserNotFoundException {
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

    private User getUserOrThrow(int userId) throws UserNotFoundException {
        User user = userStorage.find(userId);
        if (user == null) {
            throw new UserNotFoundException(userId);
        }
        return user;
    }
}
