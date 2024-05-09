package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Director;

import java.util.List;

public interface DirectorStorage {
    Director addDirector(Director director);

    Director getDirectorById(int directorId);

    Director updateDirector(Director director);

    void removeDirector(int directorId);

    List<Director> getAllDirectors();
}
