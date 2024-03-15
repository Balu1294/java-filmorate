package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {

    private Map<Integer, Film> films = new HashMap<>();
    private int idGenerator = 1;
    private static final LocalDate DATE_RELEASE_FIRST_FILM = LocalDate.of(1895, 12, 28);

    @PostMapping
    public Film addFilm(@RequestBody Film film) throws ValidationException {
        log.info("Добавляется новый фильм с id = {}", idGenerator);
        validateFilm(film);
        film.setId(idGenerator++);
        films.put(film.getId(), film);
        return film;
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film film) throws ValidationException {
        if(films.get(film.getId()) != null) {
            log.info("Обновляются данные о фильме с id = {}", film.getId());
            validateFilm(film);
            films.put(film.getId(), film);
            return film;
        } else {
            log.error("Фильм не найден");
            throw new ValidationException("Фильм не найден");
        }
    }

    @GetMapping
    public List<Film> getAllFilms() {
        return new ArrayList<>(films.values());
    }

    @SuppressWarnings("checkstyle:SeparatorWrap")
    public void validateFilm(Film film) throws ValidationException {
        if (film.getName().isEmpty()) {
            log.error("Валидация не пройдена. Название фильма фильма пустое.");
            throw new ValidationException("Название фильма не может быть пустым");
        }
        if (film.getDescription().length() > 200) {
            log.error("Валидация не пройдена. Длина описания фильма {} больше 200 символов", film.getName());
            throw new ValidationException("Описание не может содержать более 200 символов");
        }
        if (film.getReleaseDate().isBefore(DATE_RELEASE_FIRST_FILM)) {
            log.error("Валидация не пройдена. Дата релиза фильма {} раньше изобретения кино",
                    film.getReleaseDate().toString());
            throw new ValidationException("Дата релиза не может быть раньше изобретения кино");
        }
        if (film.getDuration() < 0) {
            log.error("Валидация не пройдена. Продолжительность фильма отрицательная = {}", film.getDuration());
            throw new ValidationException("Продолжительность фильма не может быть отрицательной");
        }
    }
}
