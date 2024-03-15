package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.Duration;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FilmControllerTest {
    Film testFilm;
    FilmController filmController;

    @BeforeEach
    void init() {
        testFilm = Film.builder()
                .name("Тестовый фильм")
                .description("Это тестовый фильм для проверки программы")
                .releaseDate(LocalDate.of(2020, 01, 01))
                .duration(Duration.ofMinutes(50))
                .build();
        filmController = new FilmController();
    }

    @Test
    void createNewOkFilm() throws ValidationException {
        filmController.validateFilm(testFilm);
        assertEquals("Тестовый фильм", testFilm.getName(), "Названия фильмов не совпадают");
        assertEquals("Это тестовый фильм для проверки программы", testFilm.getDescription(),
                "Ожидалось другое описание фильма");
        assertEquals("2020-01-01", testFilm.getReleaseDate().toString(),
                "Ожидалась другая дата релиза");
        assertEquals("PT50M", testFilm.getDuration().toString(),
                "Ожидалась другая продолжительность фильма");
    }

    @Test
    void createNewBadNameFilm() throws ValidationException {
        testFilm.setName("");

        ValidationException exception = assertThrows(ValidationException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                filmController.validateFilm(testFilm);
            }
        });
        assertEquals("Название фильма не может быть пустым", exception.getMessage());
    }

    @Test
    void createNewBadDescriptionFilm() {
        testFilm.setDescription("Количество символов в описании явно превышает число 200. По условиям валидации," +
                " при превышении этого числа должно генерироваться исключение с таким сообщением 'Описание не может " +
                "содержать более 200 символов");
        ValidationException exception = assertThrows(ValidationException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                filmController.validateFilm(testFilm);
            }
        });
        assertEquals("Описание не может содержать более 200 символов", exception.getMessage());
    }

    @Test
    void createNewBadReleaseDateFilm() {
        testFilm.setReleaseDate(LocalDate.of(1894, 12, 28));
        ValidationException exception = assertThrows(ValidationException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                filmController.validateFilm(testFilm);
            }
        });
        assertEquals("Дата релиза не может быть раньше изобретения кино", exception.getMessage());
    }

    @Test
    void createNewBadDurationFilm() {
        testFilm.setDuration(testFilm.getDuration().minusMinutes(51));
        ValidationException exception = assertThrows(ValidationException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                filmController.validateFilm(testFilm);
            }
        });
        assertEquals("Продолжительность фильма не может быть отрицательной", exception.getMessage());
    }
}