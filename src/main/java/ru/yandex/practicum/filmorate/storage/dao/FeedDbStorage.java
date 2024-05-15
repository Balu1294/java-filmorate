package ru.yandex.practicum.filmorate.storage.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Feed;
import ru.yandex.practicum.filmorate.storage.FeedStorage;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

@Component
public class FeedDbStorage implements FeedStorage {
    private final JdbcTemplate jdbcTemplate;

    public FeedDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void addEvent(Integer userId, String eventType, String operation, Integer entityId) {
        String sqlQuery = "insert into feed(timestamp, user_Id, event_Type, operation, entity_Id) " +
                "values(?, ?, ?, ?, ?)";
        jdbcTemplate.update(sqlQuery, Timestamp.from(Instant.now()), userId, eventType, operation, entityId);
    }

    @Override
    public List<Feed> getFeed(Integer userId) {
        String sqlQuery = "select * from feed where user_id = ?";
        return jdbcTemplate.query(sqlQuery, mapRow(), userId);
    }

    private RowMapper<Feed> mapRow() {
        return (rs, rowNum) -> new Feed(rs.getTimestamp("timestamp"), rs.getInt("user_id"),
                rs.getString("event_type"), rs.getString("operation"),
                rs.getInt("event_id"), rs.getInt("entity_id"));
    }
}
