package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.ValidationException;

import java.util.List;

public interface FilmStorage {
    Film add(Film film) throws ValidationException;

    Film remove(int id);

    Film update(Film film) throws ValidationException;

    List<Film> getAll();

    Film find(int id);
}
