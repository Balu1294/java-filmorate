package ru.yandex.practicum.filmorate.dao;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Slf4j
@Component
public class GenreDbStorage implements GenreStorage {
    private JdbcTemplate jdbcTemplate;

    @Override
    public Optional<Genre> getById(Integer id) {
        try {
            log.info("Из базы данных получен жанр с id = {}", id);
            return Optional.ofNullable(jdbcTemplate.queryForObject("select * from genres where id=?",
                    (rs, rowNum) -> new Genre(rs.getInt("id"), rs.getString("name")), id));
        } catch (EmptyResultDataAccessException ex) {
            log.error("Жанр с id = {} в базе данных отсутствует", id);
            return Optional.empty();
        }
    }

    @Override
    public List<Genre> getAllGenres() {
        return jdbcTemplate.query("select * from genres ",
                (rs, rowNum) -> new Genre(rs.getInt("id"), rs.getString("name")));
    }

    public void addGenreForFilm(int filmId, int genreId) {
        String sqlQuery = "INSERT INTO genres(film_id, genre_id) VALUES (?,?)";
        jdbcTemplate.update(sqlQuery,
                filmId,
                genreId);
    }
}
