package ru.yandex.practicum.filmorate.model.exceptions;

public class FilmNotFoundException extends ObjectNotFoundException {
    public FilmNotFoundException(int id) {
        super("Фильм не найден, id=" + id);
    }
}

