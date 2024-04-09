package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmServiceImp;

import java.util.List;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private static final String PATH_LIKE = "/{id}/like/{userId}";

    private final FilmServiceImp filmService;

    @Autowired
    public FilmController(FilmServiceImp filmService) {
        this.filmService = filmService;
    }

    @PostMapping
    public Film addFilm(@RequestBody Film film) throws ValidationException {
        log.info("Поступил запрос на добавление нового фильма");
        return filmService.addFilm(film);
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film film) throws ValidationException {
        log.info("Поступил запрос на обновление данных о фильме с id = {}", film.getId());
        return filmService.updateFilm(film);
    }

    @GetMapping
    public List<Film> getAllFilms() {
        log.info("Поступил запрос на вывод списка всех фильмов");
        return filmService.getAllFilms();
    }

    /*пользователь ставит лайк фильму */
    @PutMapping(PATH_LIKE)
    public Film userLikedFilm(@PathVariable int id, @PathVariable int userId) throws ValidationException {
        log.info("Поступил запрос на добавление лайка к фильму");
        return filmService.addLike(id, userId);
    }

    /*пользователь удаляет лайк */
    @DeleteMapping(PATH_LIKE)
    public void userRemovedLike(@PathVariable int id, @PathVariable int userId) throws ValidationException {
        log.info("Поступил запрос на удаление лайка у фильма");
        filmService.removeLike(id, userId);
    }

    /*возвращает список топ фильмов*/
    @GetMapping("/popular")
    public List<Film> getTopFilms(@RequestParam(defaultValue = "10", required = false) int count) {
        log.info("Поступил запрос на вывод списка топовых фильмов");
        return filmService.getTopFilms(count);
    }
}
