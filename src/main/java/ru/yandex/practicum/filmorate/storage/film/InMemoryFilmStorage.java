package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.FilmValidator;
import ru.yandex.practicum.filmorate.model.IdGenerator;
import ru.yandex.practicum.filmorate.model.exceptions.FilmNotFoundException;

import java.util.*;

@Slf4j
//@Component
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
    public int add(Film film) {
        validator.validate(film);
        var filmToAdd = film.toBuilder().id(idGenerator.getNextId()).build();
        log.info("Добавлен фильм: " + film);
        films.add(filmToAdd);
        return filmToAdd.getId();
    }

    @Override
    public void remove(int id) {
        var film = find(id);
        films.remove(film);
    }

    @Override
    public void update(Film film) {
        validator.validate(film);

        var removedFilm = film.getId();
        films.remove(removedFilm);
        films.add(film);
        log.info("Заменён фильм: " + removedFilm + " на " + film);
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
