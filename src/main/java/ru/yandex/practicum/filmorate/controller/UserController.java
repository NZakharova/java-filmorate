package ru.yandex.practicum.filmorate.controller;

import java.util.Collection;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.model.*;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.service.exceptions.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.service.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

@Slf4j
@RestController
public class UserController {

    private final UserStorage userStorage;
    private final UserService userService;

    public UserController(UserStorage userStorage, UserService userService) {
        this.userStorage = userStorage;
        this.userService = userService;
    }

    @GetMapping("/users")
    public List<User> findAll() {
        return userStorage.getAll();
    }

    @GetMapping("/users/{id}")
    public User find(@PathVariable int id) throws UserNotFoundException {
        var user = userStorage.find(id);
        if (user == null) {
            throw new UserNotFoundException(id);
        }

        return user;
    }

    @PostMapping(value = "/users")
    public User create(@RequestBody User user) throws ValidationException {
        return userStorage.add(user);
    }

    @PutMapping(value = "/users")
    public User update(@RequestBody User user) throws ValidationException, ObjectNotFoundException {
        var updated = userStorage.update(user);
        if (updated == null) {
            throw new UserNotFoundException(user.getId());
        }

        return updated;
    }

    @PutMapping("/users/{userId}/friends/{friendId}")
    public void addFriend(@PathVariable int userId, @PathVariable int friendId) throws UserNotFoundException {
        userService.addFriend(userId, friendId);
    }

    @DeleteMapping("/users/{userId}/friends/{friendId}")
    public void removeFriend(@PathVariable int userId, @PathVariable int friendId) throws UserNotFoundException {
        userService.removeFriend(userId, friendId);
    }

    @GetMapping("/users/{userId}/friends")
    public Collection<User> getFriends(@PathVariable int userId) throws UserNotFoundException {
        return userService.getFriends(userId);
    }

    @GetMapping("/users/{userId}/friends/common/{otherId}")
    public Collection<User> getCommonFriends(@PathVariable int userId, @PathVariable int otherId) throws UserNotFoundException {
        return userService.getCommonFriends(userId, otherId);
    }

    @ExceptionHandler
    public ResponseEntity<String> handleObjectNotFoundException(ObjectNotFoundException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
    }
}