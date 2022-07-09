package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.IdGenerator;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final List<Film> films = new ArrayList<>();
    private final IdGenerator idGenerator = new IdGenerator();

    @Override
    public Film add(Film film) {
        film.setId(idGenerator.getNextId());
        log.info("Добавлен фильм: " + film);
        films.add(film);
        return film;
    }

    @Override
    public Film remove(int id) {
        var film = find(id);
        if (film != null) {
            films.remove(film);
        }

        return film;
    }

    @Override
    public Film update(Film film) {
        var removedFilm = remove(film.getId());
        if (removedFilm != null) {
            films.add(film);
            log.info("Заменён фильм: " + removedFilm + " на " + film);
            return film;
        }

        return null;
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

        return null;
    }
}
