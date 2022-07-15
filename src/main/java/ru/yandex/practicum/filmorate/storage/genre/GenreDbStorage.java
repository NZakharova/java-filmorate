package ru.yandex.practicum.filmorate.storage.genre;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.exceptions.GenreNotFoundException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class GenreDbStorage implements GenreStorage {
    private final JdbcTemplate jdbcTemplate;

    public GenreDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Genre find(int id) {
        try {
            var sql = "SELECT `name` FROM `genre` WHERE `genreId` = ?";
            var name = jdbcTemplate.queryForObject(sql, String.class, id);
            return new Genre(id, name);
        }
        catch (EmptyResultDataAccessException ex) {
            throw new GenreNotFoundException(id);
        }
    }

    @Override
    public List<Genre> getAll() {
        var sql = "SELECT `genreId`, `name` FROM `genre`";
        return jdbcTemplate.query(sql, this::toGenre);
    }

    private Genre toGenre(ResultSet set, int row) throws SQLException {
        return new Genre(set.getInt("genreId"), set.getString("name"));
    }
}
