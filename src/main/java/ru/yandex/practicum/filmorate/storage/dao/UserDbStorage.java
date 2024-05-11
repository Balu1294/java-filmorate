package ru.yandex.practicum.filmorate.storage.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public User createUser(User user) {
        String sqlQuery = "INSERT INTO users(email, name, login, birthday) VALUES(?,?,?,?);";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"id"});
            stmt.setString(1, user.getEmail());
            stmt.setString(2, user.getName());
            stmt.setString(3, user.getLogin());
            stmt.setDate(4, Date.valueOf(user.getBirthday()));
            return stmt;
        }, keyHolder);
        user.setId(Objects.requireNonNull(keyHolder.getKey().intValue()));
        return user;
    }

    @Override
    public User updateUser(User user) {
        String sqlQuery = "UPDATE users SET email=?, name=?, login=?, birthday=? WHERE id=?;";
        jdbcTemplate.update(sqlQuery, user.getEmail(), user.getName(), user.getLogin(), user.getBirthday(), user.getId());
        return user;
    }

    @Override
    public List<User> getAllUsers() {
        String sqlQuery = "SELECT * FROM users;";
        return jdbcTemplate.query(sqlQuery, mapRow());
    }

    @Override
    public Optional<User> getUserById(Integer id) {
        String sqlQuery = "SELECT * FROM users WHERE id=?;";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sqlQuery, mapRow(), id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public void removeUser(Integer id) {
        String sqlQuery = "DELETE FROM users WHERE id=?";
        jdbcTemplate.update(sqlQuery, id);
    }

    public RowMapper<User> mapRow() {
        return (rs, rowNum) -> new User(rs.getInt("id"), rs.getString("email"),
                rs.getString("name"), rs.getString("login"),
                LocalDate.parse(rs.getString("birthday")));
    }

    // Метод подбора рекомендаций фильмов для пользователя с id
    @Override
    public List<Integer> getRecommendedFilmsId (Integer userId) {
        List<Integer> filmsId = new ArrayList<>();
        SqlRowSet recommendedUser = jdbcTemplate.queryForRowSet("select user_id from likes " +
                "where film_id in (select film_id from likes where user_id = ?) and not user_id = ?" +
                "group by user_id order by count(film_id) desc limit 1", userId, userId);
        if (!recommendedUser.next()) {
            return filmsId;
        }
        SqlRowSet recommendedFilm = jdbcTemplate.queryForRowSet("select film_id from likes " +
                "where not film_id in (select film_id from likes where user_id = ?) and user_id = ?",
                userId, recommendedUser.getInt("user_id"));
        while (recommendedFilm.next()) {
            filmsId.add(recommendedFilm.getInt("film_id"));
        }
        return filmsId;
    }
}
