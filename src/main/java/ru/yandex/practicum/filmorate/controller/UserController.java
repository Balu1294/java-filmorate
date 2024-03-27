package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private Map<Integer, User> users = new HashMap<>();
    private int idGenerator = 1;
    InMemoryUserStorage userStorage;

    @Autowired
    public UserController(InMemoryUserStorage userStorage) {
        this.userStorage = userStorage;
    }

    @PostMapping
    public User createUser(@RequestBody User user) throws ValidationException {
        return userStorage.createUser(user);
    }

    @PutMapping
    public User updateUser(@RequestBody User user) throws ValidationException {
        return userStorage.updateUser(user);
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userStorage.getAllUsers();
    }


}
