package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.IdGenerator;
import ru.yandex.practicum.filmorate.model.UserValidator;
import ru.yandex.practicum.filmorate.model.exceptions.UserNotFoundException;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {
    private final List<User> users = new ArrayList<>();
    private final IdGenerator idGenerator;
    private final UserValidator validator;

    public InMemoryUserStorage(UserValidator validator, IdGenerator idGenerator) {
        this.validator = validator;
        this.idGenerator = idGenerator;
    }

    @Override
    public User add(User user) {
        replaceEmptyName(user);

        validator.validate(user);

        user.setId(idGenerator.getNextId());

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
        users.remove(user);
        return user;
    }

    @Override
    public User update(User user) {
        replaceEmptyName(user);
        validator.validate(user);

        var removedUser = remove(user.getId());
        users.add(user);
        log.info("Заменён пользователь: " + removedUser + " на " + user);
        return user;
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
}
