package ru.yandex.practicum.filmorate.storage.dao;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

@AllArgsConstructor
@Slf4j
@Component
public class GenreDbStorage implements GenreStorage {
    private JdbcTemplate jdbcTemplate;

    @Override
    public Optional<Genre> getById(Integer id) {

        try {
            log.info("Из базы данных получен жанр с id = {}", id);
            return Optional.ofNullable(jdbcTemplate.queryForObject("SELECT * FROM genres WHERE id=?",
                    mapRow(), id));
        } catch (EmptyResultDataAccessException ex) {
            log.error("Жанр с id = {} в базе данных отсутствует", id);
            return Optional.empty();
        }
    }

    @Override
    public List<Genre> getAllGenres() {
        return jdbcTemplate.query("SELECT * FROM genres ",
                mapRow());
    }

    @Override
    public List<Genre> getGenresOfFilm(Integer id) {
        log.info("Получены жанры к фильму с id = {}", id);
        String sqlQuery = "SELECT g.id, g.name FROM films_genre AS fg JOIN genres AS g " +
                "ON fg.genre_id = g.id " +
                "WHERE fg.film_id = ?";;
        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> new Genre(
                rs.getInt("id"),
                rs.getString("name")), id);
    }

    @Override
    public void addGenreToFilm(Film film, Set<Genre> genresList) {
        String sqlQuery = "INSERT INTO films_genre (film_id, genre_id) VALUES (?, ?)";
        List<Genre> genres = new ArrayList<>(genresList);
        jdbcTemplate.batchUpdate(
                sqlQuery, new BatchPreparedStatementSetter() {
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setLong(1, film.getId());
                        ps.setLong(2, genres.get(i).getId());
                    }

                    public int getBatchSize() {
                        return genresList.size();
                    }
                });
    }

    @Override
    public void removeGenresForFilm(Integer filmId) {
        String sqlQuery = "DELETE FROM films_genre WHERE film_id = ?";
        jdbcTemplate.update(sqlQuery, filmId);
    }

    public RowMapper<Genre> mapRow() {
        return (rs, rowNum) -> new Genre(rs.getInt("id"), rs.getString("name"));
    }
}
