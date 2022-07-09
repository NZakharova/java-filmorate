package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {
    Film add(Film film);
    Film remove(int id);
    Film update(Film film);
    List<Film> getAll();
    Film find(int id);
}
