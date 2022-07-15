package ru.yandex.practicum.filmorate.model.exceptions;

public class GenreNotFoundException extends ObjectNotFoundException {
    public GenreNotFoundException(int id) {
        super("Жанр не найден, id=" + id);
    }
}
