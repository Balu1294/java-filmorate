package ru.yandex.practicum.filmorate.storage.dao;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.storage.LikesStorage;

@Slf4j
@AllArgsConstructor
@Component
public class LikesDbStorage implements LikesStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void addLike(Integer userId, Integer filmId) {
        log.info("Пользователь с id = {} ставит лайк фильму с id = {}", userId, filmId);
            String sqlQuery = "INSERT INTO likes(user_id, film_id) VALUES (?,?)";
        try {
            jdbcTemplate.update(sqlQuery, userId, filmId);
            log.info("Лайк поставлен");
        } catch (DataIntegrityViolationException exception) {
            throw new DataIntegrityViolationException("Лайк от данного пользователя уже поставлен этому фильму!");
        }
    }

    @Override
    public void removeLike(Integer userId, Integer filmId) {
        log.info("Пользователь с id = {} удаляет лайк у фильма с id = {}", userId, filmId);
        String sqlQuery = "DELETE FROM likes WHERE user_id=? AND film_id=?";
        jdbcTemplate.update(sqlQuery, userId, filmId);
        log.info("Лайк удален");
    }
}
