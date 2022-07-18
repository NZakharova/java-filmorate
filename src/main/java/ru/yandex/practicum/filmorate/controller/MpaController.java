package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.mpa.MpaStorage;

import java.util.List;

@RestController
public class MpaController {
    private final MpaStorage mpaStorage;

    public MpaController(MpaStorage genreStorage) {
        this.mpaStorage = genreStorage;
    }

    @GetMapping("/mpa")
    public List<Mpa> findAll() {
        return mpaStorage.getAll();
    }

    @GetMapping("/mpa/{id}")
    public Mpa find(@PathVariable int id) {
        return mpaStorage.find(id);
    }
}
