package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.GenreStorage;
import ru.yandex.practicum.filmorate.storage.LikesStorage;
import ru.yandex.practicum.filmorate.storage.validators.FilmValidator;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
@RequiredArgsConstructor
public class FilmServiceImp {
    private final FilmStorage filmStorage;
    private final UserService userService;
    private final GenreStorage genreStorage;
    private final LikesStorage likesStorage;

    public Film addFilm(Film film) throws ValidationException {
        FilmValidator.filmValidate(film);
        filmStorage.addFilm(film);
        if (!film.getGenres().isEmpty() || film.getGenres() != null) {
            Set<Genre> genres = new HashSet<>(film.getGenres());
            genreStorage.addGenreToFilm(film, genres);
            film.setGenres(genres);
        }
        return film;
    }

    public Film updateFilm(Film film) throws ValidationException {
        FilmValidator.filmValidate(film);
        String exMessage = String.format("Фильм с id = %d отсутствует в базе", film.getId());
        filmStorage.getFilmById(film.getId()).orElseThrow(() -> new NotFoundException(exMessage));
        filmStorage.updateFilm(film);
        if (!film.getGenres().isEmpty() || film.getGenres() != null) {
            Set<Genre> genres = new HashSet<>(film.getGenres());
            genreStorage.removeGenresForFilm(film.getId());
            genreStorage.addGenreToFilm(film, genres);
            film.setGenres(genres);
        } else {
            genreStorage.removeGenresForFilm(film.getId());
        }
        return film;
    }

//    @Override
//    public void removeFilm(Film film) {
//        filmStorage.removeFilm(film);


    public List<Film> getAllFilms() {
        List<Film> films = filmStorage.getAllFilms();
        return films;
    }

    public void addLike(Integer filmId, Integer userId) {
        likesStorage.addLike(userId, filmId);
    }

    public void removeLike(int filmId, int userId) throws ValidationException {
//        Film film = this.getFilmById(filmId);
        filmStorage.getFilmById(filmId).orElseThrow(() -> new NotFoundException("Фильм не найден"));
        userService.getUserById(userId);
        likesStorage.removeLike(userId, filmId);
        log.info("Удален лайк пользователем с id={} у фильма с id={}", userId, filmId);

        log.error("Пользователь не ставил лайк фильму с ID=" + filmId);
    }

    public Film getFilmById(Integer id) {
        String exMessage = String.format("Фильм с id = %d отсутствует в базе данных", id);
        final Film film = filmStorage.getFilmById(id).orElseThrow(() -> new NotFoundException(exMessage));
//        genreStorage.loadFilms(List.of(film));
        return film;

    }

    public List<Film> getTopFilms(int count) {
        log.info("Выводится список из {} фильмов", count);
        List<Film> topFilms = new ArrayList<>(filmStorage.getPopularFilms(count));
        genreStorage.loadFilms(topFilms);
        return topFilms;
    }
}
