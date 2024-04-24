package ru.yandex.practicum.filmorate.dao;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Slf4j
@AllArgsConstructor
@Component
public class MpaDbStorage implements MpaStorage {

    private JdbcTemplate jdbcTemplate;

    @Override
    public Optional<Mpa> getMpaForId(int id) {
        String sqlQeury = "SELECT * FROM Mpa WHERE id= ?";
        try {
            log.info("Из базы данных получен МРА с id = {}", id);
            return Optional.ofNullable(jdbcTemplate.queryForObject(sqlQeury, mapRow(), id));
        } catch (EmptyResultDataAccessException ex) {
            log.error("МРА с id = {} в базе данных отсутствует", id);
            return Optional.empty();
        }
    }

    @Override
    public List<Mpa> getAllMpa() {
        String sqlQuery = "SELECT * FROM Mpa";
        return jdbcTemplate.query(sqlQuery, mapRow());
    }

    @Override
    public Optional<Mpa> getMpaForFilmId(int filmId) {
        String sqlQuery = "SELECT m.id, m.name " + "FROM films AS f" + "JOIN Mpa AS m ON f.mpa_id=m.id" + " WHERE f.id=?";
        try {
            log.info("Из базы данных получен МРА принадлежащий фильму с id = {}", filmId);
            return Optional.ofNullable(jdbcTemplate.queryForObject(sqlQuery, mapRow(), filmId));
        } catch (EmptyResultDataAccessException ex) {
            log.error("У фильма с id = {} отсутствует МРА", filmId);
            return Optional.empty();
        }
    }

    public RowMapper<Mpa> mapRow() {
        return new RowMapper<Mpa>() {
            @Override
            public Mpa mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new Mpa(rs.getInt("id"), rs.getString("name"));
            }
        };
    }
}
