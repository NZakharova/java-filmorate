package ru.yandex.practicum.filmorate.service.exceptions;

public class FilmNotFoundException extends ObjectNotFoundException {
    public FilmNotFoundException(int id) {
        super("Фильм не найден, id=" + id);
    }
}

