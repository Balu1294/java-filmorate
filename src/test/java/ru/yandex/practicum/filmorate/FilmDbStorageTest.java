package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.dao.FilmDbStorage;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class FilmDbStorageTest {

    private final FilmDbStorage filmDbStorage;
    private Film filmTest;

    @BeforeEach
    public void beforeData() {
        filmTest = Film.builder()
                .name("Тестовый фильм")
                .description("Фильм для тестирования")
                .releaseDate(LocalDate.of(2020, 5, 26))
                .duration(90)
                .mpa(new Mpa(4, "R"))
                .build();
        filmDbStorage.addFilm(filmTest);
    }

    @Test
    void getFilmById() {
        Optional<Film> firstFilm = filmDbStorage.getFilmById(1);
        Assertions.assertThat(firstFilm)
                .isPresent()
                .hasValueSatisfying(film ->
                        Assertions.assertThat(film).hasFieldOrPropertyWithValue("id", 1))
                .hasValueSatisfying(film ->
                        Assertions.assertThat(film).hasFieldOrPropertyWithValue("name", "Тестовый фильм"))
                .hasValueSatisfying(film ->
                        Assertions.assertThat(film).hasFieldOrPropertyWithValue("description", "Фильм для тестирования"))
                .hasValueSatisfying(film ->
                        Assertions.assertThat(film).hasFieldOrPropertyWithValue("releaseDate", LocalDate.of(2020, 5, 26)))
                .hasValueSatisfying(film ->
                        Assertions.assertThat(film).hasFieldOrPropertyWithValue("duration", 90))
                .hasValueSatisfying(film ->
                        Assertions.assertThat(film).hasFieldOrPropertyWithValue("mpa", new Mpa(4, "R")));
    }

    @Test
    void updateFilm() {
        filmTest.setName("Тестовый фильм 2");
        filmTest.setDescription("Второй фильм для тестирования");
        filmTest.setReleaseDate(LocalDate.of(2021, 2, 2));
        filmTest.setDuration(45);
        filmTest.setMpa(new Mpa(1, "G"));
        filmDbStorage.updateFilm(filmTest);
        Optional<Film> secondFilm = filmDbStorage.getFilmById(1);
        Assertions.assertThat(secondFilm)
                .isPresent()
                .hasValueSatisfying(film ->
                        Assertions.assertThat(film).hasFieldOrPropertyWithValue("id", 1))
                .hasValueSatisfying(film ->
                        Assertions.assertThat(film).hasFieldOrPropertyWithValue("name", "Тестовый фильм 2"))
                .hasValueSatisfying(film ->
                        Assertions.assertThat(film).hasFieldOrPropertyWithValue("description", "Второй фильм для тестирования"))
                .hasValueSatisfying(film ->
                        Assertions.assertThat(film).hasFieldOrPropertyWithValue("releaseDate", LocalDate.of(2021, 2, 2)))
                .hasValueSatisfying(film ->
                        Assertions.assertThat(film).hasFieldOrPropertyWithValue("duration", 45))
                .hasValueSatisfying(film ->
                        Assertions.assertThat(film).hasFieldOrPropertyWithValue("mpa", new Mpa(1, "G")));
    }

    @Test
    void findAll() {
        Film newFilm = Film.builder()
                .name("Третий тестовый фильм")
                .description("Фильм для нового теста")
                .duration(100)
                .releaseDate(LocalDate.of(2015, 1, 1))
                .mpa(new Mpa(1, "G"))
                .build();
        filmDbStorage.addFilm(newFilm);
        List<Film> films = filmDbStorage.getAllFilms();
        Assertions.assertThat(films).isNotEmpty().isNotNull().doesNotHaveDuplicates();
        Assertions.assertThat(films).extracting("description").contains(newFilm.getDescription());
        Assertions.assertThat(films).extracting("name").contains(filmTest.getName());
    }
}