package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FilmServiceImp implements FilmService {
    FilmStorage filmStorage;
    UserStorage userStorage;

    @Autowired
    public FilmServiceImp(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    @Override
    public Film addFilm(Film film) throws ValidationException {
        return filmStorage.addFilm(film);
    }

    @Override
    public Film updateFilm(Film film) throws ValidationException {
        return filmStorage.updateFilm(film);
    }

    @Override
    public void removeFilm(Film film) {
        filmStorage.removeFilm(film);
    }

    public List<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }

    @Override
    public Film addLike(int filmId, int userId) throws ValidationException {
        if (!userStorage.getAllUsers().contains(userStorage.getUserById(userId))) {
            log.error("Отсутствует пользователь с id={}", userId);
            throw new NotFoundException("Отсутствует пользователь с id= " + userId);
        }
        Film film = filmStorage.getFilmById(filmId);
        film.addLike(userId);
        updateFilm(film);
        log.info("Добавлен лайк пользователем с id={} фильму с id={}", userId, filmId);
        return film;
    }

    @Override
    public void removeLike(int filmId, int userId) throws ValidationException {
        Film film = filmStorage.getFilmById(filmId);
        if (film.getLikes().contains(userId)) {
            log.info("Удален лайк пользователем с id={} у фильма с id={}", userId, filmId);
            film.removeLike(userId);
            updateFilm(film);
        } else {
            log.error("Пользователь не ставил лайк фильму" + film);
            throw new NotFoundException("Пользователь не ставил лайк фильму" + film);
        }
    }

    @Override
    public List<Film> getTopFilms(int count) {
        log.info("Выводится список из {} фильмов", count);
        return filmStorage.getAllFilms().stream()
                .filter(film -> !film.getLikes().isEmpty())
                .sorted((o1, o2) -> Integer.compare(o2.getLikes().size(), o1.getLikes().size()))
                .limit(count)
                .collect(Collectors.toList());
    }
}
