package ru.yandex.practicum.filmorate.storage.mpa;

import ru.yandex.practicum.filmorate.model.Rating;

import java.util.List;

public interface MpaStorage {
    Rating find(int id);

    List<Rating> getAll();
}
