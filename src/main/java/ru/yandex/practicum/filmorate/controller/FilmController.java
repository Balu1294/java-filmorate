package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.List;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {

    private final FilmStorage filmStorage;
    private FilmService filmService;

    @Autowired
    public FilmController(FilmStorage filmStorage, FilmService filmService) {
        this.filmStorage = filmStorage;
        this.filmService = filmService;
    }

    @PostMapping
    public Film addFilm(@RequestBody Film film) throws ValidationException {
        log.info("Добавляется новый фильм");
        return filmStorage.addFilm(film);
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film film) throws ValidationException {
        log.info("Обновляются данные о фильме с id = {}", film.getId());
        return filmStorage.updateFilm(film);
    }

    @GetMapping
    public List<Film> getAllFilms() {
        log.info("Выводится список всех фильмов");
        return filmStorage.getAllFilms();
    }

    /*пользователь ставит лайк фильму */
    @PutMapping("/{id}/like/{userId}")
    public Film userLikedFilm(@PathVariable String id, @PathVariable String userId) {
        log.info("Пользователь с id={} ставит лайк фильму с id={}", userId, id);
        return filmService.addLike(Integer.parseInt(id), Integer.parseInt(userId));
    }

    /*пользователь удаляет лайк */
    @DeleteMapping("/{id}/like/{userId}")
    public void userRemovedLike(@PathVariable String id, @PathVariable String userId) {
        log.info("Пользователь с id= {} удаляет лайк фильму с id= {}", userId, id);
        filmService.removeLike(Integer.parseInt(id), Integer.parseInt(userId));
    }

    /*возвращает список топ фильмов*/
    @GetMapping("/popular")
    public List<Film> getTopFilms(@RequestParam(defaultValue = "10") String count) {
        log.info("Выводится список топ {} фильмов", count);
        return filmService.getTopFilms(Integer.parseInt(count));
    }
}
