package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmService {
    Film addFilm(Film film) throws ValidationException;

    Film updateFilm(Film film) throws ValidationException;

    void removeFilm(Film film);

    List<Film> getAllFilms();

    Film addLike(int filmId, int userId) throws ValidationException;

    void removeLike(int filmId, int userId) throws ValidationException;

    List<Film> getTopFilms(int count);

}
