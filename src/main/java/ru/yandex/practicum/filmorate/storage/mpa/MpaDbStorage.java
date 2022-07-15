package ru.yandex.practicum.filmorate.storage.mpa;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.model.exceptions.RatingNotFoundException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class MpaDbStorage implements MpaStorage {
    private final JdbcTemplate jdbcTemplate;

    public MpaDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Rating find(int id) {
        try {
            var sql = "SELECT `name` FROM `rating` WHERE `ratingId` = ?";
            var name = jdbcTemplate.queryForObject(sql, String.class, id);
            return new Rating(id, name, "");
        } catch (EmptyResultDataAccessException ex) {
            throw new RatingNotFoundException(id);
        }
    }

    @Override
    public List<Rating> getAll() {
        var sql = "SELECT `ratingId`, `name` FROM `rating`";
        return jdbcTemplate.query(sql, this::toRating);
    }

    private Rating toRating(ResultSet set, int row) throws SQLException {
        return new Rating(set.getInt("ratingId"), set.getString("name"), "");
    }
}
