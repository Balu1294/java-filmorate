package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FilmService {
    FilmStorage filmStorage;
    UserStorage userStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public Film addLike(int filmId, int userId) {
        if (!userStorage.getAllUsers().contains(userStorage.getUserById(userId))) {
            log.error("Отсутствует пользователь с id={}", userId);
            throw new NotFoundException("Отсутствует пользователь с id= " + userId);
        }
        Film film = filmStorage.getFilmById(filmId);
        film.getLikes().add(userId);
        log.info("Добавлен лайк пользователем с id={} фильму с id={}", userId, filmId);
        return film;
    }

    public void removeLike(int filmId, int userId) {
        Film film = filmStorage.getFilmById(filmId);
        if (film.getLikes().contains(userId)) {
            log.info("Удален лайк пользователем с id={} у фильма с id={}", userId, filmId);
            film.getLikes().remove(userId);
        } else {
            log.error("Пользователь не ставил лайк фильму" + film);
            throw new NotFoundException("Пользователь не ставил лайк фильму" + film);
        }
    }

    public List<Film> getTopFilms(int count) {
        log.info("Вываодится список из {} фильмов", count);
        return filmStorage.getAllFilms().stream()
                .filter(film -> !film.getLikes().isEmpty())
                .sorted((o1, o2) -> Integer.compare(o2.getLikes().size(), o1.getLikes().size()))
                .limit(count)
                .collect(Collectors.toList());
    }
}
