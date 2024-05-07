package ru.yandex.practicum.filmorate.storage.dao;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;


@Primary
@Component
@AllArgsConstructor
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;
    private final GenreDbStorage genreDbStorage;

    @Override
    public Film addFilm(Film film) {
        String sqlQuery = "insert into films(name, description, releaseDate, duration, mpa_id) " +
                "values (?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"id"});
            stmt.setString(1, film.getName());
            stmt.setString(2, film.getDescription());
            stmt.setDate(3, Date.valueOf(film.getReleaseDate()));
            stmt.setInt(4, film.getDuration());
            stmt.setInt(5, film.getMpa().getId());
            return stmt;
        }, keyHolder);
        film.setId(Objects.requireNonNull(keyHolder.getKey().intValue()));
        return film;
    }


    @Override
    public Film updateFilm(Film film) {
        String sqlQuery = "UPDATE films SET name = ?, description = ?, releaseDate = ? ,duration = ?, mpa_id=? WHERE id = ?";
        jdbcTemplate.update(sqlQuery, film.getName(), film.getDescription(),
                film.getReleaseDate(), film.getDuration(), film.getMpa().getId(), film.getId());
        return film;
    }

    @Override
    public List<Film> getAllFilms() {
        String sqlQuery = "select f.*, m.name as mpa_name from films as f  join mpa as m on  f.mpa_id=m.id ";
        return jdbcTemplate.query(sqlQuery, new RowMapper<Film>() {
            @Override
            public Film mapRow(ResultSet rs, int rowNum) throws SQLException {
                Film film = Film.builder()
                        .id(rs.getInt("id"))
                        .name(rs.getString("name"))
                        .description(rs.getString("description"))
                        .releaseDate(LocalDate.parse(rs.getString("releaseDate")))
                        .duration(rs.getInt("duration"))
                        .mpa(Mpa.builder().id(rs.getInt("mpa_id")).name(rs.getString("mpa_name")).build())
                        .build();
                film.setGenres(new ArrayList<>(genreDbStorage.getGenresOfFilm(film.getId())));
                return film;
            }
        });
    }

    @Override
    public Optional<Film> getFilmById(Integer id) {
        String sqlQuery = "SELECT f.*, m.name AS mpa_name FROM films AS f JOIN mpa AS m ON  f.mpa_id=m.id WHERE f.id = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sqlQuery, rowMap(), id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Film> getPopularFilms(Integer count) {
        String sqlQuery = "SELECT f.*,  m.name as mpa_name FROM films AS f "
                + "JOIN mpa AS m ON  f.mpa_id=m.id "
                + "LEFT JOIN  likes AS l ON f.id=l.film_id "
                + "GROUP BY f.id ORDER BY COUNT(l.user_id) DESC LIMIT ?";
        return jdbcTemplate.query(sqlQuery, new RowMapper<Film>() {
            @Override
            public Film mapRow(ResultSet rs, int rowNum) throws SQLException {
                Film film = Film.builder()
                        .id(rs.getInt("id"))
                        .name(rs.getString("name"))
                        .description(rs.getString("description"))
                        .releaseDate(LocalDate.parse(rs.getString("releaseDate")))
                        .duration(rs.getInt("duration"))
                        .mpa(Mpa.builder().id(rs.getInt("mpa_id")).name(rs.getString("mpa_name")).build())
                        .build();
                film.setGenres(new ArrayList<>(genreDbStorage.getGenresOfFilm(film.getId())));
                return film;
            }
        }, count);
    }

    @Override
    public void removeFilm(Integer id) {
        String sqlQuery = "DELETE FROM films WHERE id=?";
        jdbcTemplate.update(sqlQuery, id);
    }

    public RowMapper<Film> rowMap() {
        return new RowMapper<Film>() {
            @Override
            public Film mapRow(ResultSet rs, int rowNum) throws SQLException {
                Film film = Film.builder()
                        .id(rs.getInt("id"))
                        .name(rs.getString("name"))
                        .description(rs.getString("description"))
                        .releaseDate(LocalDate.parse(rs.getString("releaseDate")))
                        .duration(rs.getInt("duration"))
                        .mpa(Mpa.builder().id(rs.getInt("mpa_id")).name(rs.getString("mpa_name")).build())
                        .build();
                film.setGenres(new ArrayList<>(genreDbStorage.getGenresOfFilm(film.getId())));
                return film;
            }
        };
    }
}
