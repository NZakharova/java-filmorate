package ru.yandex.practicum.filmorate.model.exceptions;

public class RatingNotFoundException extends ObjectNotFoundException {
    public RatingNotFoundException(int id) {
        super("Рейтинг не найден, id=" + id);
    }
}
