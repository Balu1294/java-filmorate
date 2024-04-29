package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Slf4j
@AllArgsConstructor
@Service
public class GenreService {
    private final GenreStorage genreStorage;

    public Genre getGenreById(Integer id) {
        String exMesssage = String.format("Жанр с id = %d не найден", id);

        return genreStorage.getById(id)
                .orElseThrow(() -> new NotFoundException(exMesssage));
    }

    public List<Genre> getAllGenres() {
        return genreStorage.getAllGenres();
    }

//    public Set<Genre> getGenresOfFilm(Integer id) {
//        return genreStorage.getGenresOfFilm(id);
//}
}
