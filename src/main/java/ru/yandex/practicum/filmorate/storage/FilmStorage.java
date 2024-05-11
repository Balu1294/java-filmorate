package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Optional;

public interface FilmStorage {

    // Метод добавления фильма
    Film addFilm(Film film) throws ValidationException;

    // Метод обновления фильма
    Film updateFilm(Film film) throws ValidationException;

    // Метод получения фильма по id
    Optional<Film> getFilmById(Integer id);

    // Метод получения всех фильмов
    List<Film> getAllFilms();

    // Метод получения списка популярных фильмов
    List<Film> getPopularFilms(Integer count);

    // Метод удаления фильма по id
    void removeFilm(Integer id);

  List<Film> getPopularFilmsByGenre(int genreId, int count);

    List<Film> getPopularFilmsByYear(int year, int count);
}
