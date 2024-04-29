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
//        if (film.getName().isEmpty()) {
//            log.error("Валидация не пройдена. Название фильма фильма пустое.");
//            throw new ValidationException("Название фильма не может быть пустым");
//        }
//        if (film.getDescription().length() > 200) {
//            log.error("Валидация не пройдена. Длина описания фильма {} больше 200 символов", film.getName());
//            throw new ValidationException("Описание не может содержать более 200 символов");
//        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.error("Валидация не пройдена. Дата релиза фильма {} раньше изобретения кино",
                    film.getReleaseDate().toString());
            throw new ValidationException("Дата релиза не может быть раньше изобретения кино");
        }
//        if (film.getDuration() < 0) {
//            log.error("Валидация не пройдена. Продолжительность фильма {} отрицательная = {}",
//                    film.getName(), film.getDuration());
//            String messageException = String.format("Продолжительность фильма %s не может быть отрицательной",
//                    film.getName());
//            throw new ValidationException(messageException);
//        }
//        if (film.getMpa().getId() > 5) {
//            log.error("Валидация не пройдена. Возрастного рейтинга с id = {} не существует", film.getMpa().getId());
//            String messageEx = "Такого рейтинга не существует";
//            throw new ValidationException(messageEx);
//        }
//        for (Genre genre : film.getGenres()) {
//            if (genre.getId() > 6) {
//                String messageEx = String.format("Жанра с id = %d не существует", genre.getId());
//                throw new ValidationException(messageEx);
//            }

//        }
    }
}
