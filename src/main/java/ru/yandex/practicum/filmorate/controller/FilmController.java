package ru.yandex.practicum.filmorate.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.ValidationException;
import ru.yandex.practicum.filmorate.service.exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.exceptions.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

@Slf4j
@RestController
public class FilmController {

    private final FilmStorage filmStorage;
    private final FilmService filmService;

    public FilmController(FilmStorage filmStorage, FilmService filmService) {
        this.filmStorage = filmStorage;
        this.filmService = filmService;
    }

    @GetMapping("/films")
    public List<Film> findAll() {
        return filmStorage.getAll();
    }

    @GetMapping("/films/{id}")
    public Film find(@PathVariable int id) throws FilmNotFoundException {
        var film = filmStorage.find(id);
        if (film == null) {
            throw new FilmNotFoundException(id);
        }

        return film;
    }

    @PostMapping(value = "/films")
    public Film create(@RequestBody Film film) throws ValidationException {
        return filmStorage.add(film);
    }

    @PutMapping(value = "/films")
    public Film update(@RequestBody Film film) throws ValidationException, ObjectNotFoundException {
        var result = filmStorage.update(film);

        if (result == null) {
            throw new FilmNotFoundException(film.getId());
        }

        return result;
    }

    @PutMapping("/films/{filmId}/like/{userId}")
    public void addLike(@PathVariable int filmId, @PathVariable int userId) throws ObjectNotFoundException {
        filmService.addLike(filmId, userId);
    }

    @DeleteMapping("/films/{filmId}/like/{userId}")
    public void removeLike(@PathVariable int filmId, @PathVariable int userId) throws FilmNotFoundException {
        filmService.removeLike(filmId, userId);
    }

    @GetMapping({"/films/popular", "/films/popular?count={count}"})
    public List<Film> getMostPopularFilms(@RequestParam(required = false) Integer count) {
        if (count == null) {
            count = 10;
        }
        return filmService.getMostPopularFilms(count);
    }

    @ExceptionHandler
    public ResponseEntity<String> handleObjectNotFoundException(ObjectNotFoundException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
    }
}