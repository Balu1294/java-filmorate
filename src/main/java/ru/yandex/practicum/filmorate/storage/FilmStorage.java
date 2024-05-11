package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Optional;

public interface FilmStorage {
    Film addFilm(Film film) throws ValidationException;

    Film updateFilm(Film film) throws ValidationException;

    Optional<Film> getFilmById(Integer id);

    List<Film> getAllFilms();

    List<Film> getPopularFilms(Integer count);

    List<Film> getPopularFilmsByGenre(int genreId, int count);

    List<Film> getPopularFilmsByYear(int year, int count);

}
