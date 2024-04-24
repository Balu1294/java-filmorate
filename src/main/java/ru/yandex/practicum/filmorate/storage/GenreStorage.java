package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Optional;

public interface GenreStorage {

    //Получение жанра по id
    Optional<Genre> getById(Integer id);


// Получение всех жанров
    List<Genre> getAllGenres();
}
