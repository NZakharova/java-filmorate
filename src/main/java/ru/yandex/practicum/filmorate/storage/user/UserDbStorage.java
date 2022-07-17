package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.UserValidator;
import ru.yandex.practicum.filmorate.model.exceptions.UserNotFoundException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component("userDb")
public class UserDbStorage implements UserStorage {
    private final UserValidator userValidator;
    private final JdbcTemplate jdbcTemplate;

    public UserDbStorage(UserValidator userValidator, JdbcTemplate jdbcTemplate) {
        this.userValidator = userValidator;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public int add(User user) {
        userValidator.validate(user);
        var sql = "INSERT INTO `user` (`email`, `login`, `name`, `birthday`)" +
                "VALUES (?,?,?,?)";

        var keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            var statement = connection.prepareStatement(sql, new String[]{"userId"});
            statement.setString(1, user.getEmail());
            statement.setString(2, user.getLogin());
            statement.setString(3, user.getName());
            statement.setDate(4, java.sql.Date.valueOf(user.getBirthday()));
            return statement;
        }, keyHolder);

        var key = keyHolder.getKey();
        if (key == null) {
            throw new UnsupportedOperationException("key == null");
        }
        return key.intValue();
    }

    @Override
    public void remove(int id) {
        var sql = "DELETE FROM `user` WHERE `userId` = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public void update(User user) {
        userValidator.validate(user);

        var sql = "UPDATE `user` SET " +
                "`name` = ?, " +
                "`email` = ?, " +
                "`login` = ?, " +
                "`birthday` = ? " +
                "WHERE `userId` = ?";

        jdbcTemplate.update(sql, user.getName(), user.getEmail(), user.getLogin(), user.getBirthday(), user.getId());
    }

    @Override
    public List<User> getAll() {
        var sql = "SELECT `userId`, `name`, `email`, `login`, `birthday` " +
                "FROM `user`";
        return jdbcTemplate.query(sql, this::toUser);
    }

    @Override
    public User find(int id) {
        try {
            var sql = "SELECT `userId`, `name`, `email`, `login`, `birthday` " +
                    "FROM `user` " +
                    "WHERE `userId` = ?";
            return jdbcTemplate.queryForObject(sql, this::toUser, id);
        } catch (EmptyResultDataAccessException ex) {
            throw new UserNotFoundException(id);
        }
    }

    @Override
    public void addFriend(int userId, int friendId) {
        var sql = "INSERT INTO `user_friend` (`userId`, `friendId`) " +
                "VALUES (?, ?)";

        jdbcTemplate.update(sql, userId, friendId);
    }

    @Override
    public void removeFriend(int userId, int friendId) {
        var sql = "DELETE FROM `user_friend` WHERE `userId` = ? AND `friendId` = ?";
        jdbcTemplate.update(sql, userId, friendId);
    }

    @Override
    public List<Integer> getFriends(int userId) {
        var sql = "SELECT `friendId` FROM `user_friend` WHERE `userId` = ?";
        return jdbcTemplate.queryForList(sql, Integer.class, userId);
    }

    private User toUser(ResultSet set, int row) throws SQLException {
        return new User(
                set.getInt("userId"),
                set.getString("email"),
                set.getString("login"),
                set.getString("name"),
                set.getDate("birthday").toLocalDate()
        );
    }
}
