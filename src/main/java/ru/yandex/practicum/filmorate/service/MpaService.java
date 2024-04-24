package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

import java.util.List;
import java.util.Optional;

@Slf4j
@AllArgsConstructor
@Service
public class MpaService {
    MpaStorage mpaStorage;

    public Optional<Mpa> getMpaForId(int id) {
        String exMesssage = String.format("MPA с id = %d не найдено", id);
        return Optional.ofNullable(mpaStorage.getMpaForId(id)
                .orElseThrow(() -> new NotFoundException(exMesssage)));
    }

    public List<Mpa> getAllMpa() {
        return mpaStorage.getAllMpa();
    }

    public Optional<Mpa> getMpaForFilmId(int filmId) {
        String exMesssage = String.format("MPF c id = %d не найдено", filmId);
        return Optional.ofNullable(mpaStorage.getMpaForFilmId(filmId)
                .orElseThrow(() -> new NotFoundException(exMesssage)));
    }
}
