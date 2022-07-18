package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;
import ru.yandex.practicum.filmorate.storage.mpa.MpaStorage;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.time.LocalDate;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmoRateApplicationTests {
    private final UserDbStorage userStorage;
    private final JdbcTemplate jdbcTemplate;
    private final FilmDbStorage filmStorage;

    @BeforeEach
    void cleanDb() {
        var sql = "DELETE FROM `user_friend`;" +
                "DELETE FROM `film_like`;" +
                "DELETE FROM `film_genre`;" +
                "DELETE FROM `film`;" +
                "DELETE FROM `user`;" +
                "ALTER TABLE `user` ALTER COLUMN `userId` RESTART WITH 1";
        jdbcTemplate.update(sql);
    }

    static Film newFilm() {
        var mpa = new Mpa(1, "G", "");
        return new Film(0, "f1", "", LocalDate.ofYearDay(2020, 1), 100, 4, mpa, Collections.emptyList());
    }

    static User newUser(int number) {
        return new User(number,
                "u" + number + "@a.com",
                "u" + number,
                "u" + number,
                LocalDate.ofYearDay(2020, 1 + number));
    }

    User insertUserById(int id) {
        int realId = userStorage.add(newUser(id));
        return userStorage.find(realId);
    }

    @Test
    void testReadFirstUser() {
        insertUserById(1);

        var user = assertDoesNotThrow(() -> userStorage.find(1));
        assertEquals(1, user.getId());
        assertEquals("u1@a.com", user.getEmail());
        assertEquals("u1", user.getLogin());
    }

    @Test
    void testGetAllUsers() {
        assertEquals(0, assertDoesNotThrow(userStorage::getAll).size());
        insertUserById(1);

        assertEquals(1, assertDoesNotThrow(userStorage::getAll).size());
        insertUserById(2);

        assertEquals(2, assertDoesNotThrow(userStorage::getAll).size());
    }

    @Test
    void testAddUser() {
        final int id = 3;
        var input = new User(id, "u3@a.com", "u3", "u3", LocalDate.ofYearDay(2020, 3));

        int actualId = assertDoesNotThrow(() -> userStorage.add(input));

        assertEquals(id, actualId);
        var result = userStorage.find(id);
        assertEquals(input, result);
    }

    @Test
    void testRemoveUser() {
        insertUserById(1);
        insertUserById(2);

        assertEquals(2, userStorage.getAll().size());
        assertDoesNotThrow(() -> userStorage.remove(1));
        assertEquals(1, userStorage.getAll().size());
    }

    @Test
    void testUnknownUser() {
        assertThrows(UserNotFoundException.class, () -> userStorage.find(13));
        insertUserById(1);
        assertThrows(UserNotFoundException.class, () -> userStorage.find(13));
    }

    @Test
    void testUpdateUser() {
        int id = 1;
        insertUserById(id);

        var original = userStorage.find(id);

        var updated = original.toBuilder()
                .name("newName")
                .email("newEmail@a.com")
                .birthday(LocalDate.ofYearDay(2021, 17))
                .login("newLogin")
                .build();

        userStorage.update(updated);

        var result = userStorage.find(id);
        assertEquals(updated, result);
    }

    @Test
    void testAddUserWithAutoincrement() {
        assertDoesNotThrow(() -> insertUserById(0));
        assertDoesNotThrow(() -> insertUserById(0));

        assertEquals(2, userStorage.getAll().size());
    }

    @Test
    void testAddFriend() {
        insertUserById(1);
        insertUserById(2);

        assertDoesNotThrow(() -> userStorage.addFriend(1, 2));

        assertEquals(1, userStorage.getFriends(1).size());
        assertEquals(0, userStorage.getFriends(2).size());
    }

    @Test
    void testRemoveFriend() {
        insertUserById(1);
        insertUserById(2);

        assertDoesNotThrow(() -> userStorage.addFriend(1, 2));

        assertEquals(1, userStorage.getFriends(1).size());

        userStorage.removeFriend(1, 2);

        assertEquals(0, userStorage.getFriends(1).size());
    }

    @Test
    void testAddFilm() {
        var film = newFilm();

        int id = filmStorage.add(film);

        var expected = film.toBuilder().id(id).build();
        var real = filmStorage.find(id);

        assertEquals(1, filmStorage.getAll().size());
        assertEquals(expected, real);
    }

    @Test
    void testRemoveFilm() {
        assertEquals(0, filmStorage.getAll().size());

        var film = newFilm();
        int id = filmStorage.add(film);
        assertEquals(1, filmStorage.getAll().size());

        filmStorage.remove(id);
        assertEquals(0, filmStorage.getAll().size());
    }

    @Test
    void testUpdateFilm() {
        var film = newFilm();
        int id = filmStorage.add(film);

        var expected = film.toBuilder()
                .id(id)
                .description("desc")
                .rate(10)
                .duration(220)
                .name("updatedName")
                .releaseDate(LocalDate.ofYearDay(2020, 123))
                .build();

        filmStorage.update(expected);

        var real = filmStorage.find(id);

        assertEquals(expected, real);
    }

    @Test
    void testFindUnknownFilm() {
        assertThrows(FilmNotFoundException.class, () -> filmStorage.find(-10));
    }

    @Test
    void testRemoveUnknownFilm() {
        assertThrows(FilmNotFoundException.class, () -> filmStorage.remove(-10));

        var film = newFilm();
        filmStorage.add(film);
        assertThrows(FilmNotFoundException.class, () -> filmStorage.remove(-10));
    }

    @Test
    void testRemoveUnknowLikeOnFil() {
        var film = newFilm();
        int id = filmStorage.add(film);

        assertThrows(UserNotFoundException.class, () -> filmStorage.removeLike(id, -10));
    }

    @Test
    void testAddLikesOnFilm() {
        var user1 = insertUserById(1);
        var user2 = insertUserById(2);

        var film = newFilm();

        var filmId = filmStorage.add(film);
        assertEquals(0, filmStorage.getLikes(filmId).size());

        filmStorage.addLike(filmId, user1.getId());
        assertEquals(1, filmStorage.getLikes(filmId).size());

        filmStorage.addLike(filmId, user2.getId());
        assertEquals(2, filmStorage.getLikes(filmId).size());
    }

    @Test
    void testRemoveLikesOnFilm() {
        var user1 = insertUserById(1);

        var film = newFilm();

        var filmId = filmStorage.add(film);
        assertEquals(0, filmStorage.getLikes(filmId).size());

        filmStorage.addLike(filmId, user1.getId());
        assertEquals(1, filmStorage.getLikes(filmId).size());

        filmStorage.removeLike(filmId, user1.getId());
        assertEquals(0, filmStorage.getLikes(filmId).size());

        assertThrows(UserNotFoundException.class, () -> filmStorage.removeLike(filmId, -10));
    }

    @Test
    void testPopularFilms() {
        var user1 = insertUserById(1);
        var user2 = insertUserById(2);
        var user3 = insertUserById(3);

        var film = newFilm();

        var film1Id = filmStorage.add(film);
        var film2Id = filmStorage.add(film);
        var film3Id = filmStorage.add(film);

        var ids = filmStorage.getMostPopularFilms(10).stream().map(Film::getId).toArray();

        assertArrayEquals(new Object[]{film1Id, film2Id, film3Id}, ids);

        filmStorage.addLike(film3Id, user1.getId());
        filmStorage.addLike(film3Id, user2.getId());
        filmStorage.addLike(film3Id, user3.getId());

        filmStorage.addLike(film2Id, user1.getId());
        filmStorage.addLike(film2Id, user2.getId());

        ids = filmStorage.getMostPopularFilms(10).stream().map(Film::getId).toArray();
        assertArrayEquals(new Object[]{film3Id, film2Id, film1Id}, ids);
    }
}