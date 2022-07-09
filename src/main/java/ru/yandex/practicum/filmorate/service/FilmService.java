package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.service.exceptions.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.service.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.*;

@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final Map<Integer, Set<Integer>> likes = new HashMap<>();

    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public void addLike(int filmId, int userId) throws ObjectNotFoundException {
        var film = filmStorage.find(filmId);
        if (film == null) {
            throw new FilmNotFoundException(filmId);
        }

        var user = userStorage.find(userId);
        if (user == null) {
            throw new UserNotFoundException(filmId);
        }

        likes.computeIfAbsent(filmId, id -> new HashSet<>()).add(userId);
    }

    public void removeLike(int filmId, int userId) throws FilmNotFoundException {
        var set = likes.get(filmId);
        if (set != null) {
            set.remove(userId);
        } else {
            throw new FilmNotFoundException(filmId);
        }
    }

    private int getLikesCount(Film film) {
        var set = likes.get(film.getId());
        if (set == null) {
            return 0;
        } else {
            return set.size();
        }
    }

    public List<Film> getMostPopularFilms(int count) {
        var allFilms = new ArrayList<>(filmStorage.getAll());

        allFilms.sort(Comparator.comparing(this::getLikesCount).reversed());

        return allFilms.subList(0, Math.min(count, allFilms.size()));
    }
}
