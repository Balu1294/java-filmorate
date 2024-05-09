package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.service.DirectorService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/directors")
@RequiredArgsConstructor
public class DirectorController {
    private final DirectorService service;

    @PostMapping
    public Director addDirector(@Valid @RequestBody Director director) {
        return service.addDirector(director);
    }

    @GetMapping("/{directorId}")
    public Director getDirectorById(@PathVariable int directorId) {
        return service.getDirectorById(directorId);
    }

    @PutMapping
    public Director updateDirector(@Valid @RequestBody Director director) {
        return service.updateDirector(director);
    }

    @DeleteMapping("/{directorId}")
    public void removeDirector(@PathVariable int directorId) {
        service.removeDirector(directorId);
    }

    @GetMapping
    public List<Director> getAllDirectors() {
        return service.getAllDirectors();
    }
}