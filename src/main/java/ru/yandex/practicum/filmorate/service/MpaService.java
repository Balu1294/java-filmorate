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
    private final MpaStorage mpaStorage;

    public Optional<Mpa> getMpaForId(Integer id) {
        return Optional.ofNullable(mpaStorage.getMpaForId(id)
                .orElseThrow(() -> new NotFoundException(String.format("MPA с id = %d не найдено", id))));
    }

    public List<Mpa> getAllMpa() {
        return mpaStorage.getAllMpa();
    }
}
