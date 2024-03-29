package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;

import java.util.List;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {

    private final InMemoryFilmStorage filmStorage;
    private FilmService filmService;

    @Autowired
    public FilmController(InMemoryFilmStorage filmStorage, FilmService filmService) {
        this.filmStorage = filmStorage;
        this.filmService = filmService;
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

    /*пользователь ставит лайк фильму */
    @PutMapping("/films/{id}/like/{userId}")
    public Film userLikedFilm(@PathVariable String id, @PathVariable String userId) {
        return filmService.addLike(Integer.parseInt(id), Integer.parseInt(userId));
    }

    /*пользователь удаляет лайк */
    @DeleteMapping("/films/{id}/like/{userId}")
    public void userRemovedLike(@PathVariable String id, @PathVariable String userId) {
        filmService.removeLike(Integer.parseInt(id), Integer.parseInt(userId));
    }

    /*возвращает список топ фильмов*/
    @GetMapping("/films/popular?count={count}")
    public List<Film> getTopFilms(@RequestParam(defaultValue = "10") String count) {
        return filmService.getTopFilms(Integer.parseInt(count));
    }
}
