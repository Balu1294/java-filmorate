package ru.yandex.practicum.filmorate.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
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
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FilmService {
    FilmStorage filmStorage;
    UserService userService;
    GenreStorage genreStorage;
    LikesStorage likesStorage;
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
    public List<Film> getAllFilms() {
        List<Film> films = filmStorage.getAllFilms();
        return films;
    }
    public void addLike(Integer filmId, Integer userId) {
        likesStorage.addLike(userId, filmId);
    }
    public void removeLike(int filmId, int userId) {
        filmStorage.getFilmById(filmId).orElseThrow(() -> new NotFoundException("Фильм не найден"));
        userService.getUserById(userId);
        likesStorage.removeLike(userId, filmId);
        log.info("Удален лайк пользователем с id={} у фильма с id={}", userId, filmId);
    }
    public Film getFilmById(Integer id) {
        String exMessage = String.format("Фильм с id = %d отсутствует в базе данных", id);
        final Film film = filmStorage.getFilmById(id).orElseThrow(() -> new NotFoundException(exMessage));
        return film;
    }
    public List<Film> getTopFilms(int count) {
        log.info("Выводится список из {} фильмов", count);
        List<Film> topFilms = new ArrayList<>(filmStorage.getPopularFilms(count));
        return topFilms;
    }
    private List<Genre> sortGenres(Set<Genre> genres) {
        return new ArrayList<>(genres).stream()
                .sorted((Comparator.comparingLong(Genre::getId)))
                .collect(Collectors.toList());
    }
    /* Метод удаления фильма по id */
    public void removeFilm(Integer id) {
        filmStorage.getFilmById(id).orElseThrow(() ->
                new NotFoundException(String.format("Фильм с id = %d не найден", id)));
        filmStorage.removeFilm(id);
        log.info("Удален фильм с id = %d", id);
    }
    public List<Film> getFilmsBySearch(String query, String by) {
        log.info("Начинаем поиск");
        if (query.isBlank()) {
            log.info("Пустой запрос поиска");
            return new ArrayList<>();
        }
        log.info("Начинаем поиск по запросу: {}", query);
        String subString = "%" + query.toLowerCase() + "%";
        return filmStorage.getFilmsBySearch(subString, by);
    }
    public List<Film> getDirectorSorted(int directorId, String sortBy) {
        log.info("Получение фильмов режиссера {} с сортировкой по {}", directorId, sortBy);
        return filmStorage.getDirectorSorted(directorId, sortBy);
    }

   /*public List<Film> getPopularFilmsByGenre(int genreId, int count) {
        return filmStorage.getPopularFilmsByGenre(genreId, count);
    }

    public List<Film> getPopularFilmsByYear(int year, int count) {
        return filmStorage.getPopularFilmsByYear(year, count);
    }*/

    public List<Film> getPopularFilms(int count) {
        return filmStorage.getPopularFilms(count);// Реализация получения популярных фильмов без учета жанра и года
    }


    public List<Film> getPopularFilmsByGenre(int genreId, int count) {
        return filmStorage.getPopularFilmsByGenre(genreId,count);// Реализация получения популярных фильмов по указанному жанру
    }


    public List<Film> getPopularFilmsByYear(int year, int count) {
        return filmStorage.getPopularFilmsByYear(year,count);// Реализация получения популярных фильмов за указанный год
    }


    public List<Film> getPopularFilmsByGenreAndYear(int genreId, int year, int count) {
        return filmStorage.getPopularFilmsByGenreAndYear(genreId,year,count);// Реализация получения популярных фильмов по указанному жанру и году
    }
}
