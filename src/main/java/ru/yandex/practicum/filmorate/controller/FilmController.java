package ru.yandex.practicum.filmorate.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.FilmValidator;
import ru.yandex.practicum.filmorate.model.IdGenerator;
import ru.yandex.practicum.filmorate.model.ValidationException;

@Slf4j
@RestController
public class FilmController {

    private final List<Film> films = new ArrayList<>();
    private final IdGenerator idGenerator = new IdGenerator();

    @GetMapping("/films")
    public List<Film> findAll() {
        return films;
    }

    @PostMapping(value = "/films")
    public Film create(@RequestBody Film film) throws ValidationException {
        FilmValidator.validate(film);

        film.setId(idGenerator.getNextId());

        log.info("Добавлен фильм: " + film);
        films.add(film);
        return film;
    }

    @PutMapping(value = "/films")
    public Film update(@RequestBody Film film) throws ValidationException {
        FilmValidator.validate(film);

        for (var knownFilm : films) {
            if (knownFilm.getId() == film.getId()) {
                films.remove(knownFilm);
                log.info("Заменён фильм: " + knownFilm + " на " + film);
                films.add(film);
                return film;
            }
        }

        throw new ValidationException("Фильм не найден");
    }
}