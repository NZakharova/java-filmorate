package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.FilmValidator;
import ru.yandex.practicum.filmorate.model.IdGenerator;
import ru.yandex.practicum.filmorate.model.exceptions.FilmNotFoundException;

import java.util.*;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final List<Film> films = new ArrayList<>();
    private final IdGenerator idGenerator;
    private final FilmValidator validator;
    private final Map<Integer, Set<Integer>> likes = new HashMap<>();

    public InMemoryFilmStorage(FilmValidator validator, IdGenerator idGenerator) {
        this.validator = validator;
        this.idGenerator = idGenerator;
    }

    @Override
    public Film add(Film film) {
        validator.validate(film);
        film.setId(idGenerator.getNextId());
        log.info("Добавлен фильм: " + film);
        films.add(film);
        return film;
    }

    @Override
    public Film remove(int id) {
        var film = find(id);
        films.remove(film);
        return film;
    }

    @Override
    public Film update(Film film) {
        validator.validate(film);

        var removedFilm = remove(film.getId());
        films.add(film);
        log.info("Заменён фильм: " + removedFilm + " на " + film);
        return film;
    }

    @Override
    public List<Film> getAll() {
        return List.copyOf(films);
    }

    @Override
    public Film find(int id) {
        for (var knownFilm : films) {
            if (knownFilm.getId() == id) {
                return knownFilm;
            }
        }

        throw new FilmNotFoundException(id);
    }

    @Override
    public void addLike(int filmId, int userId) {
        find(filmId); // убедимся что фильм существует

        likes.computeIfAbsent(filmId, id -> new HashSet<>()).add(userId);
    }

    @Override
    public void removeLike(int filmId, int userId) {
        var set = likes.get(filmId);
        if (set != null) {
            set.remove(userId);
        } else {
            throw new FilmNotFoundException(filmId);
        }
    }

    @Override
    public List<Film> getMostPopularFilms(int count) {
        var allFilms = new ArrayList<>(getAll());

        allFilms.sort(Comparator.comparing(this::getLikesCount).reversed());

        return allFilms.subList(0, Math.min(count, allFilms.size()));
    }


    private int getLikesCount(Film film) {
        var set = likes.get(film.getId());
        if (set == null) {
            return 0;
        } else {
            return set.size();
        }
    }
}
