package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.util.List;

@Slf4j
@AllArgsConstructor
@Service
public class GenreService {
    private final GenreStorage genreStorage;

    public Genre getGenreById(Integer id) {
        return genreStorage.getById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Жанр с id = %d не найден", id)));
    }

    public List<Genre> getAllGenres() {
        return genreStorage.getAllGenres();
    }
}
