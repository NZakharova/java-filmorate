package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.IdGenerator;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {
    private final List<User> users = new ArrayList<>();
    private final IdGenerator idGenerator = new IdGenerator();

    @Override
    public User add(User user) {
        user.setId(idGenerator.getNextId());

        replaceEmptyName(user);

        log.info("Добавлен пользователь: " + user);
        users.add(user);
        return user;
    }

    private void replaceEmptyName(User user) {
        if (user.getName() == null || user.getName().length() == 0) {
            user.setName(user.getLogin());
        }
    }

    @Override
    public User remove(int id) {
        var user = find(id);
        if (user != null) {
            users.remove(user);
        }

        return user;
    }

    @Override
    public User update(User user) {
        var removedUser = remove(user.getId());
        if (removedUser != null) {
            replaceEmptyName(user);

            users.add(user);
            log.info("Заменён пользователь: " + removedUser + " на " + user);
            return user;
        }

        return null;
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

        return null;
    }
}
