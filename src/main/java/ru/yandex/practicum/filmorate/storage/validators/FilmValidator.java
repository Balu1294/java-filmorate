package ru.yandex.practicum.filmorate.storage.validators;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.time.LocalDate;

@UtilityClass
@Slf4j
public class FilmValidator {

    public void filmValidate(Film film) throws ValidationException {

        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.error("Валидация не пройдена. Дата релиза фильма {} раньше изобретения кино",
                    film.getReleaseDate().toString());
            throw new ValidationException("Дата релиза не может быть раньше изобретения кино");
        }
        if (film.getMpa().getId() > 5) {
            log.error("Валидация не пройдена. Возрастного рейтинга с id = {} не существует", film.getMpa().getId());
            String messageEx = "Такого рейтинга не существует";
            throw new ValidationException(messageEx);
        }
        for (Genre genre : film.getGenres()) {
            if (genre.getId() > 6) {
                String messageEx = String.format("Жанра с id = %d не существует", genre.getId());
                throw new ValidationException(messageEx);
            }
        }
    }
}
