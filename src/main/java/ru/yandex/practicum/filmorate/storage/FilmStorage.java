package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Optional;

public interface FilmStorage {
    Film addFilm(Film film) throws ValidationException;

//    Film getFilmById(int id);

    Film updateFilm(Film film) throws ValidationException;

//    List<Film> getAllFilms();

    Optional<Film> getFilmById(Integer id);

//    void removeFilm(Film film);

    List<Film> getAllFilms();


    List<Film> getPopularFilms(Integer count);

//    void removeFilm(Film film);
//
//    void validateFilm(Film film) throws ValidationException;
}
