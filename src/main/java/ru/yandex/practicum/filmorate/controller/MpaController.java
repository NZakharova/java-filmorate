package ru.yandex.practicum.filmorate.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.model.exceptions.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.storage.mpa.MpaStorage;

import java.util.List;

@RestController
public class MpaController {
    private final MpaStorage mpaStorage;

    public MpaController(MpaStorage genreStorage) {
        this.mpaStorage = genreStorage;
    }

    @GetMapping("/mpa")
    public List<Rating> findAll() {
        return mpaStorage.getAll();
    }

    @GetMapping("/mpa/{id}")
    public Rating find(@PathVariable int id) {
        return mpaStorage.find(id);
    }

    @ExceptionHandler
    public ResponseEntity<String> handleObjectNotFoundException(ObjectNotFoundException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
    }
}
