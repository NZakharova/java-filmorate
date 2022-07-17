package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.IdGenerator;
import ru.yandex.practicum.filmorate.model.UserValidator;
import ru.yandex.practicum.filmorate.model.exceptions.UserNotFoundException;

import java.util.*;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {
    private final List<User> users = new ArrayList<>();
    private final IdGenerator idGenerator;
    private final UserValidator validator;
    private final Map<Integer, Set<Integer>> friends = new HashMap<>();

    public InMemoryUserStorage(UserValidator validator, IdGenerator idGenerator) {
        this.validator = validator;
        this.idGenerator = idGenerator;
    }

    @Override
    public int add(User user) {
        validator.validate(user);

        var userWithId = user.toBuilder().id(idGenerator.getNextId()).build();

        log.info("Добавлен пользователь: " + userWithId);
        users.add(userWithId);
        return userWithId.getId();
    }

    @Override
    public void remove(int id) {
        var user = find(id);
        users.remove(user);
    }

    @Override
    public void update(User user) {
        validator.validate(user);

        var removedUser = user.getId();
        users.remove(removedUser);
        users.add(user);
        log.info("Заменён пользователь: " + removedUser + " на " + user);
    }

    @Override
    public List<User> getAll() {
        return List.copyOf(users);
    }

    @Override
    public User find(int id) {
        for (var knownUser : users) {
            if (knownUser.getId() == id) {
                return knownUser;
            }
        }

        throw new UserNotFoundException(id);
    }

    @Override
    public void addFriend(int userId, int friendId) {
        find(userId);
        find(friendId);

        friends.computeIfAbsent(userId, key -> new HashSet<>()).add(friendId);
    }

    @Override
    public void removeFriend(int userId, int friendId) {
        find(userId);
        find(friendId);

        var set = friends.get(userId);
        if (set != null) {
            set.remove(friendId);
        }
    }

    @Override
    public List<Integer> getFriends(int userId) {
        find(userId);

        var set = friends.get(userId);
        if (set == null) {
            return Collections.emptyList();
        }
        else {
            return List.copyOf(set);
        }
    }
}
