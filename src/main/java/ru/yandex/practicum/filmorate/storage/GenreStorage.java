package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface GenreStorage {

    //Получение жанра по id
    Optional<Genre> getById(Integer id);


    // Получение всех жанров
    List<Genre> getAllGenres();

    // Получение жанров фильма
    List<Genre> getGenresOfFilm(Integer id);

    //Заполнение таблицы films_genre
    void updateFilmsGenre(Integer filmId, Integer genreId);

    // Добавление жанров к фильму
    void addGenreToFilm(Film film, Set<Genre> genresList);

    // Удаление жанров фильма
    void removeGenresForFilm(Integer filmId);

    void loadFilms(List<Film> films);
}
