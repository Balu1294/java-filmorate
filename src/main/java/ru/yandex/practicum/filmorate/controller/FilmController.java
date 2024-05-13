package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/films")
@Validated
@Slf4j
public class FilmController {
    private static final String PATH_LIKE = "/{id}/like/{userId}";

    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film) throws ValidationException {
        log.info("Поступил запрос на добавление нового фильма");
        return filmService.addFilm(film);
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) throws ValidationException {
        log.info("Поступил запрос на обновление данных о фильме с id = {}", film.getId());
        return filmService.updateFilm(film);
    }

    @GetMapping
    public List<Film> getAllFilms() {
        log.info("Поступил запрос на вывод списка всех фильмов");
        return filmService.getAllFilms();
    }

    @GetMapping("/{id}")
    public Film getFilmById(@PathVariable Integer id) {
        return filmService.getFilmById(id);
    }

    /*пользователь ставит лайк фильму */
    @PutMapping(PATH_LIKE)
    public void userLikedFilm(@PathVariable Integer id, @PathVariable Integer userId) {
        log.info("Поступил запрос на добавление лайка к фильму");
        filmService.addLike(id, userId);
    }

    /*пользователь удаляет лайк */
    @DeleteMapping(PATH_LIKE)
    public void userRemovedLike(@PathVariable Integer id, @PathVariable Integer userId) throws ValidationException {
        log.info("Поступил запрос на удаление лайка у фильма");
        filmService.removeLike(id, userId);
    }

    /*возвращает список топ фильмов*/
    /*@GetMapping("/popular")
    public List<Film> getTopFilms(@RequestParam(defaultValue = "10", required = false) int count) {
        log.info("Поступил запрос на вывод списка топовых фильмов");
        return filmService.getTopFilms(count);
    }*/

    /* Метод удаления фильма по id */
    @DeleteMapping("/{filmId}")
    public void removeFilm(@PathVariable("filmId") Integer id) {
        log.info("Поступил запрос на удаление фильма");
        filmService.removeFilm(id);
    }


    /**
     * Поиск по названию фильмов и по режиссёру.
     * Возвращает список фильмов, отсортированных по популярности
     * @param query — текст для поиска
     * @param by — может принимать значения director (поиск по режиссёру),
     *           title (поиск по названию), либо оба значения через запятую
     *          title (поиск по названию), либо оба значения через запятую
     *           при поиске одновременно и по режиссеру и по названию.
     */
    @GetMapping("/search")
    public List<Film> getFilmsBySearch(@RequestParam String query,
                                      @RequestParam String by) {
        return filmService.getFilmsBySearch(query, by);
    }

    @GetMapping("/director/{directorId}")
    public List<Film> getDirectorSorted(
            @PathVariable int directorId,
            @RequestParam(value = "sortBy", required = true) String sortBy) {
        return filmService.getDirectorSorted(directorId, sortBy);
    }

    @GetMapping("/popular")
    public List<Film> getPopularFilms(
            @RequestParam(defaultValue = "10") int count,
            @RequestParam(required = false) Integer genreId,
            @RequestParam(required = false) Integer year) {
        if (genreId != null && year != null) {
            log.info("Поступил запрос на вывод списка популярных фильмов по жанру с id = {} за год {}", genreId, year);
            return filmService.getPopularFilmsByGenreAndYear(genreId, year, count);
        } else if (genreId != null) {
            log.info("Поступил запрос на вывод списка популярных фильмов по жанру с id = {}", genreId);
            return filmService.getPopularFilmsByGenre(genreId, count);
        } else if (year != null) {
            log.info("Поступил запрос на вывод списка популярных фильмов за год {}", year);
            return filmService.getPopularFilmsByYear(year, count);
        } else {
            log.info("Поступил запрос на вывод списка популярных фильмов");
            return filmService.getPopularFilms(count);
        }
    }

     // Метод для вывода общих по лайкам фильмов с другим пользователем/
    @GetMapping("/common")
    public List<Film> getCommonFilms(@RequestParam Integer userId,
                                     @RequestParam Integer friendId) {
        return filmService.getCommonFilms(userId, friendId);

    }
}