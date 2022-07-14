package ru.yandex.practicum.filmorate.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.model.exceptions.ObjectNotFoundException;

@Slf4j
@RestController
public class FilmController {

    private final FilmService filmService;

    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping("/films")
    public List<Film> findAll() {
        return filmService.getAll();
    }

    @GetMapping("/films/{id}")
    public Film find(@PathVariable int id)  {
        return filmService.find(id);
    }

    @PostMapping(value = "/films")
    public Film create(@RequestBody Film film) {
        return filmService.add(film);
    }

    @PutMapping(value = "/films")
    public Film update(@RequestBody Film film) {
        return filmService.update(film);
    }

    @PutMapping("/films/{filmId}/like/{userId}")
    public void addLike(@PathVariable int filmId, @PathVariable int userId) {
        filmService.addLike(filmId, userId);
    }

    @DeleteMapping("/films/{filmId}/like/{userId}")
    public void removeLike(@PathVariable int filmId, @PathVariable int userId) {
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