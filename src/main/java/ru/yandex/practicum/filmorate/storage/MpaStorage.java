package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;
import java.util.Optional;

public interface MpaStorage {

    // Метод получения рейтинга(МРА) по id
    Optional<Mpa> getMpaForId(Integer id);

    // Метод получения списка доступных рейтингов (МРА)
    List<Mpa> getAllMpa();
}
