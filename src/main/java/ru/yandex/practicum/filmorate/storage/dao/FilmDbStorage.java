package ru.yandex.practicum.filmorate.storage.dao;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static ru.yandex.practicum.filmorate.storage.dao.DirectorDBStorage.directorRowMapper;


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
        insertDirector(film);
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        String sqlQuery = "UPDATE films SET name = ?, description = ?, releaseDate = ? ,duration = ?, mpa_id=? WHERE id = ?";
        jdbcTemplate.update(sqlQuery, film.getName(), film.getDescription(),
                film.getReleaseDate(), film.getDuration(), film.getMpa().getId(), film.getId());

        String deleteDirectorsSql = "DELETE FROM films_directors WHERE film_id=?"; // добавляю режисера
        jdbcTemplate.update(deleteDirectorsSql, film.getId());
        insertDirector(film);

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
                        .directors(selectDirectors(rs.getInt("id"))) // добавляю режисера
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
                        .directors(selectDirectors(rs.getInt("id"))) // добавляю режисера
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
                        .directors(selectDirectors(rs.getInt("id"))) // добавляю режисера
                        .build();
                film.setGenres(new ArrayList<>(genreDbStorage.getGenresOfFilm(film.getId())));
                return film;
            }
        };
    }

    @Override
    public List<Film> getFilmsBySearch(String query, String by) {
        String sql = "SELECT f.*" +
                "FROM films f " +
                "LEFT JOIN films_directors fd ON f.id = fd.film_id " +
                "LEFT JOIN directors d ON fd.director_id = d.director_id " +
                "LEFT JOIN  likes AS l ON f.id=l.film_id ";
        ArrayList<Film> result = new ArrayList<>();
        SqlRowSet filmsByQuery = null;
        if (by.equals("title")) {
            filmsByQuery = jdbcTemplate.queryForRowSet(sql + "WHERE LOWER(f.name) LIKE ? GROUP BY f.id ORDER BY COUNT(l.user_id) DESC", query);
        }
        if (by.equals("director")) {
            filmsByQuery = jdbcTemplate.queryForRowSet(sql + "WHERE LOWER(d.director_name) LIKE ? GROUP BY f.id ORDER BY COUNT(l.user_id) DESC", query);
        }
        if (by.equals("director,title") || by.equals("title,director")) {
            filmsByQuery = jdbcTemplate.queryForRowSet(sql + "WHERE LOWER(f.name) LIKE ? " +
                    "OR LOWER(d.director_name) LIKE ? GROUP BY f.id ORDER BY COUNT(l.user_id) DESC ", query, query);
        }

        if (filmsByQuery != null) {
            while (filmsByQuery.next()) {
                int filmId = filmsByQuery.getInt("id");
                result.add(getFilmById(filmId).get());
            }
        }

        return result.stream()
                .sorted(Comparator.comparing(Film::getLikes).reversed())
                .collect(Collectors.toList());


    }

    private void insertDirector(Film film) {
        String sql = "INSERT INTO films_directors(film_id, director_id) " +
                "VALUES (?, ?)";
        if (!film.getDirectors().isEmpty()) {
            for (Director director : film.getDirectors()) {
                if (director.getId() > 0) {
                    jdbcTemplate.update(sql, film.getId(), director.getId());
                }
            }
        }
    }

    @Override
    public List<Director> selectDirectors(int filmId) {
        String sql = "SELECT d.director_id, d.director_name FROM directors d " +
                "LEFT JOIN films_directors fd ON fd.director_id = d.director_id " +
                "WHERE fd.film_id=?";
        return jdbcTemplate.query(sql, directorRowMapper(), filmId);
    }

    @Override
    public List<Film> getDirectorSorted(int directorId, String sortBy) {
        List<Integer> filmsIds;
        filmsIds = jdbcTemplate.query(
                "SELECT film_id FROM films_directors WHERE director_id = ?;",
                (rs, rowNum) -> rs.getInt("film_id"),
                directorId
        );
        if (filmsIds.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        List<Film> films = new ArrayList<>();
        for (int id : filmsIds) {
            films.add(getFilmById(id).get());
        }

        switch (sortBy) {
            case "likes":
                films = films.stream()
                        .sorted((film1, film2) -> film2.getLikes() - film1.getLikes())
                        .collect(Collectors.toList());
                break;
            case "year":
                films = films.stream()
                        .sorted(Comparator.comparing(Film::getReleaseDate))
                        .collect(Collectors.toList());
                break;
            default:
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        return films;
    }

    public List<Film> getPopularFilmsByGenre(int genreId, int count) {
        String sqlQuery = "SELECT f.*, m.name as mpa_name, COUNT(l.user_id) AS like_count " +
                "FROM films AS f " +
                "JOIN films_genre AS fg ON f.id = fg.film_id " +
                "JOIN mpa AS m ON f.mpa_id = m.id " +
                "LEFT JOIN likes AS l ON f.id = l.film_id " +
                "WHERE fg.genre_id = ? " +
                "GROUP BY f.id " +
                "ORDER BY like_count DESC " +
                "LIMIT ?";
        return jdbcTemplate.query(sqlQuery, rowMap(), genreId, count);
    }

    public List<Film> getPopularFilmsByYear(int year, int count) {
        String sqlQuery = "SELECT f.*, m.name as mpa_name, COUNT(l.user_id) AS like_count " +
                "FROM films AS f " +
                "JOIN mpa AS m ON f.mpa_id = m.id " +
                "LEFT JOIN likes AS l ON f.id = l.film_id " +
                "WHERE YEAR(f.releaseDate) = ? " +
                "GROUP BY f.id " +
                "ORDER BY like_count DESC " +
                "LIMIT ?";
        return jdbcTemplate.query(sqlQuery, rowMap(), year, count);
    }

    @Override
    public List<Film> getPopularFilmsByGenreAndYear(int genreId, int year, int count) {
        String sqlQuery = "SELECT f.*, m.name AS mpa_name FROM films AS f "
                + "JOIN films_genre AS fg ON f.id = fg.film_id "
                + "JOIN mpa AS m ON f.mpa_id = m.id "
                + "LEFT JOIN likes AS l ON f.id = l.film_id "
                + "WHERE fg.genre_id = ? AND YEAR(f.releaseDate) = ? "
                + "GROUP BY f.id " // Включаем столбец m.name в GROUP BY -- , m.name
                + "ORDER BY COUNT(l.user_id) DESC LIMIT ?";
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
                        .directors(selectDirectors(rs.getInt("id"))) // добавляю режисера
                        .build();
                film.setGenres(new ArrayList<>(genreDbStorage.getGenresOfFilm(film.getId())));
                return film;
            }
        }, genreId, year, count);
    }

    //Метод вывода общих фильмов с другим пользователем
    @Override
    public List<Film> getCommonFilms (Integer userId, Integer friendId){
        SqlRowSet userFilms = jdbcTemplate.queryForRowSet("SELECT u.film_id " +
                "FROM likes as u " +
                "INNER JOIN (SELECT film_id FROM likes WHERE user_id = ? ) as f " +
                "ON u.film_id = f.film_id " +
                "WHERE user_id = ? ;", friendId, userId);
        if (!userFilms.next()) {
            return new ArrayList<>();
        }
        String sqlQuery = "select f.*, m.name as mpa_name from films as f  join mpa as m on  f.mpa_id=m.id " +
                "LEFT JOIN  likes AS l ON f.id=l.film_id " +
                "where f.id = ? order by count(l.user_id) desc";
        return jdbcTemplate.query(sqlQuery, rowMap(), userFilms.getInt("film_id"));
    }
}