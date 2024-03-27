package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;

import java.util.List;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {

    private final InMemoryFilmStorage filmStorage;

    @Autowired
    public FilmController(InMemoryFilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    @PostMapping
    public Film addFilm(@RequestBody Film film) throws ValidationException {
        return filmStorage.addFilm(film);
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film film) throws ValidationException {
        return filmStorage.updateFilm(film);
    }

    @GetMapping
    public List<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }


}
