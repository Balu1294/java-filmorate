package ru.yandex.practicum.filmorate.storage;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Data
@Component
public class InMemoryFilmStorage implements FilmStorage {
    private Map<Integer, Film> films = new HashMap<>();
    private int idGenerator = 1;
    private static final LocalDate DATE_RELEASE_FIRST_FILM = LocalDate.of(1895, 12, 28);

    @Override
    public Film addFilm(Film film) throws ValidationException {
        log.info("Добавляется новый фильм с id = {}", idGenerator);
        validateFilm(film);
        film.setId(idGenerator++);
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film getFilmById(int id) {
        if (films.containsKey(id)) {
            return films.get(id);
        } else throw new NotFoundException("Film not found.");
    }

    @Override
    public Film updateFilm(Film film) throws ValidationException {
        if (films.get(film.getId()) != null) {
            log.info("Обновляются данные о фильме с id = {}", film.getId());
            validateFilm(film);
            films.put(film.getId(), film);
            return film;
        } else {
            log.error("Фильм не найден");
            throw new ValidationException("Фильм не найден");
        }
    }

    @Override
    public List<Film> getAllFilms() {
        return new ArrayList<>(films.values());
    }

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
            log.error("Валидация не пройдена. Продолжительность фильма {} отрицательная = {}",
                    film.getName(), film.getDuration());
            String messageException = String.format("Продолжительность фильма %s не может быть отрицательной",
                    film.getName());
            throw new ValidationException(messageException);
        }
    }
}
