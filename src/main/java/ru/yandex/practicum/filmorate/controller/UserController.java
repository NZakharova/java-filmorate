package ru.yandex.practicum.filmorate.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.IdGenerator;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.ValidationException;

@RestController
@Slf4j
public class UserController {

    private final List<User> users = new ArrayList<>();
    private final IdGenerator idGenerator = new IdGenerator();

    @GetMapping("/users")
    public List<User> findAll() {
        return users;
    }

    @PostMapping(value = "/users")
    public User create(@RequestBody User user) throws ValidationException {
        try {
            user.validate();
        } catch (ValidationException ex) {
            log.error("Ошибка валидации", ex);
            throw ex;
        }

        user.setId(idGenerator.getNextId());

        if (user.getName() == null || user.getName().length() == 0) {
            user.setName(user.getLogin());
        }

        log.info("Добавлен фильм: " + user);
        users.add(user);
        return user;
    }

    @PutMapping(value = "/users")
    public User update(@RequestBody User user) throws ValidationException {
        try {
            user.validate();
        } catch (ValidationException ex) {
            log.error("Ошибка валидации", ex);
            throw ex;
        }

        if (user.getName() == null || user.getName().length() == 0) {
            user.setName(user.getLogin());
        }

        for (var f : users) {
            if (f.getId() == user.getId()) {
                users.remove(f);
                log.info("Заменён пользователь: " + f + " на " + user);
                users.add(user);
                return user;
            }
        }

        throw new ValidationException("Пользователь не найден");
    }
}