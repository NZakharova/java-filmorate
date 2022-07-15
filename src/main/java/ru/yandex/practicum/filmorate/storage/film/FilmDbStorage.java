package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.FilmValidator;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;
import ru.yandex.practicum.filmorate.storage.mpa.MpaStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Component
public class FilmDbStorage implements FilmStorage {
    private final FilmValidator filmValidator;
    private final MpaStorage mpaStorage;
    private final GenreStorage genreStorage;
    private final JdbcTemplate jdbcTemplate;

    public FilmDbStorage(FilmValidator filmValidator, MpaStorage mpaStorage, GenreStorage genreStorage, JdbcTemplate jdbcTemplate) {
        this.filmValidator = filmValidator;
        this.mpaStorage = mpaStorage;
        this.genreStorage = genreStorage;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public int add(Film film) {
        filmValidator.validate(film);

        var sql = "INSERT INTO `film` (`name`, `description`, `releaseDate`, `duration`, `rate`, `ratingId`) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        var keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            var statement = connection.prepareStatement(sql, new String[]{"filmId"});
            statement.setString(1, film.getName());
            statement.setString(2, film.getDescription());
            statement.setDate(3, java.sql.Date.valueOf(film.getReleaseDate()));
            statement.setInt(4, film.getDuration());
            statement.setInt(5, film.getRate());
            statement.setInt(6, film.getMpa().getId());
            return statement;
        }, keyHolder);

        var key = keyHolder.getKey();
        if (key == null) {
            throw new UnsupportedOperationException("key == null");
        }

        int filmId = key.intValue();

        var genres = film.getGenres();
        if (genres != null) {
            updateGenres(filmId, genres);
        }

        return filmId;
    }

    @Override
    public void remove(int id) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void update(Film film) {
        filmValidator.validate(film);

        var sql = "UPDATE `film` SET " +
                "`name` = ?, " +
                "`description` = ?, " +
                "`releaseDate` = ?, " +
                "`duration` = ?, " +
                "`rate` = ?, " +
                "`ratingId` = ? " +
                "WHERE `filmId` = ?";

        jdbcTemplate.update(sql,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getRate(),
                film.getMpa().getId(),
                film.getId());

        var genres = film.getGenres();
        if (genres != null) {
            updateGenres(film.getId(), film.getGenres());
        }
    }

    private void updateGenres(int filmId, Collection<Genre> genres) {
        jdbcTemplate.update("DELETE FROM `film_genre` WHERE `filmId` = ?", filmId);

        var unique = new HashMap<Integer, Genre>();
        for (var genre : genres) {
            unique.put(genre.getId(), genre);
        }

        for (var genre : unique.values()) {
            jdbcTemplate.update("INSERT INTO `film_genre` (`filmId`, `genreId`) VALUES (?, ?)",
                    filmId,
                    genre.getId()
            );
        }
    }

    @Override
    public List<Film> getAll() {
        var sql = "SELECT `filmId`, `name`, `description`, `releaseDate`, `duration`, `ratingId` " +
                "FROM `film`";
        return jdbcTemplate.query(sql, this::toFilm);
    }

    @Override
    public Film find(int id) {
        try {
            var sql = "SELECT `filmId`, `name`, `description`, `releaseDate`, `duration`, `rate`, `ratingId` " +
                    "FROM `film` " +
                    "WHERE `filmId` = ?";

            return jdbcTemplate.queryForObject(sql, this::toFilm, id);
        } catch (EmptyResultDataAccessException ex) {
            throw new FilmNotFoundException(id);
        }
    }

    private Film toFilm(ResultSet set, int rowNum) throws SQLException {
        var filmId = set.getInt("filmId");

        var genreIds = jdbcTemplate.queryForList("SELECT `genreId` FROM `film_genre` WHERE filmId = ?", Integer.class, filmId);

        var genres = new ArrayList<Genre>();

        for (var genreId : genreIds) {
            genres.add(genreStorage.find(genreId));
        }

        return new Film(
                filmId,
                set.getString("name"),
                set.getString("description"),
                set.getDate("releaseDate").toLocalDate(),
                set.getInt("duration"),
                set.getInt("rate"),
                mpaStorage.find(set.getInt("ratingId")),
                genres
        );
    }

    @Override
    public void addLike(int filmId, int userId) {
        find(filmId);
        var sql = "INSERT INTO `film_like` (`userId`, `filmId`) VALUES (?, ?)";
        jdbcTemplate.update(sql, userId, filmId);
    }

    @Override
    public void removeLike(int filmId, int userId) {
        var sql = "DELETE FROM `film_like` WHERE `filmId` = ? AND `userId` = ?";
        int rowsCount = jdbcTemplate.update(sql, filmId, userId);
        if (rowsCount == 0) {
            throw new UserNotFoundException(userId);
        }
    }

    @Override
    public List<Film> getMostPopularFilms(int count) {
        var sql = "SELECT f.`filmId`, f.`name`, f.`description`, f.`releaseDate`, f.`rate`, f.`duration`, f.`ratingId` " +
                "FROM `film` AS f " +
                "LEFT OUTER JOIN `film_like` AS fl ON f.`filmId` = fl.`filmId` " +
                "GROUP BY f.`name` " +
                "ORDER BY COUNT(fl.`userId`) DESC " +
                "LIMIT ?";

        return jdbcTemplate.query(sql, this::toFilm, count);
    }
}
