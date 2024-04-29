package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.UserFriend;
import ru.yandex.practicum.filmorate.storage.UserFriendsStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class UserFriendsDbStorage implements UserFriendsStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void addFriend(Integer userId, Integer friendId) {
        String sqlQuery = "insert into user_friends (user_id, friend_id) values (?, ?)";
        jdbcTemplate.update(sqlQuery, userId, friendId);
    }

    @Override
    public void removeFriend(Integer userId, Integer friendId) {
        log.info("Пользовательн с id = {} удаляет из друзей пользователя с id = {}", userId, friendId);
        String sqlQuery = "DELETE FROM user_friends where user_id=? and friend_id=?;";
        jdbcTemplate.update(sqlQuery, userId, friendId);
        log.info("Пользователь удален из друзей");
    }

    @Override
    public List<UserFriend> getAllTable() {
        log.info("Выводится содержимое таблицы users_friend");
        String sqlQuery = "SELECT * FROM users_friend";
        return jdbcTemplate.query(sqlQuery, new RowMapper<UserFriend>() {
            @Override
            public UserFriend mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new UserFriend(rs.getInt("userId"), rs.getInt("friendId"));
            }
        });
    }

    @Override
    public List<User> getFriendsForUser(Integer id) {
        log.info("Выводится список друзей пользователя с id = {}", id);
        String sqlQuery = "SELECT u.* FROM user_friends AS uf\n" +
                "                JOIN users AS u ON uf.user_id=u.id\n" +
                "                WHERE u.id=?";
        return jdbcTemplate.query(sqlQuery, rowMap(), id);
    }

    @Override
    public List<User> findCommonFriends(Integer userId, Integer friendId) {
        String sqlQuery = "SELECT * FROM users u "
                + "JOIN user_friends AS f1 ON u.id = f1.friend_id "
                + "JOIN user_friends AS f2 ON u.id = f2.friend_id "
                + "WHERE f1.user_id = ? AND f2.user_id = ?";
        return jdbcTemplate.query(sqlQuery, rowMap(), userId, friendId);
    }

    public RowMapper<User> rowMap() {
        return new RowMapper<User>() {
            @Override
            public User mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new User(rs.getInt("id"), rs.getString("email"),
                        rs.getString("login"), rs.getString("name"),
                        rs.getDate("birthday").toLocalDate());
            }
        };
    }
}
