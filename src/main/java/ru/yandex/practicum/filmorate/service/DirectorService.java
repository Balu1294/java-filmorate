package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.storage.DirectorStorage;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class DirectorService {
    private final DirectorStorage storage;


    public Director addDirector(Director director) {
        Director added = storage.addDirector(director);
        log.info("Режиссер добавлен.");
        return added;
    }


    public Director getDirectorById(int directorId) {
        Director director = storage.getDirectorById(directorId);
        log.info("Режиссер {} получен.", directorId);
        return director;
    }


    public Director updateDirector(Director director) {
        Director updated = storage.updateDirector(director);
        log.info("Режиссер {} обновлен.", director.getId());
        return updated;
    }


    public void removeDirector(int directorId) {
        storage.removeDirector(directorId);
        log.info("Режиссер {} удален.", directorId);
    }


    public List<Director> getAllDirectors() {
        List<Director> directors = storage.getAllDirectors();
        log.info("Всё режиссеры получены.");
        return directors;
    }
}
