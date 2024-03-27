package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private Map<Integer, User> users = new HashMap<>();
    private int idGenerator = 1;

    @PostMapping
    public User createUser(@Valid @RequestBody User user) throws ValidationException {
        validationUsers(user);
        user.setId(idGenerator++);
        users.put(user.getId(), user);
        log.info("Создается пользователь с id = {}. Пользователь создан.", idGenerator - 1);
        return user;
    }

    @PutMapping
    public User updateUser(@RequestBody @Valid User user) throws ValidationException {
        if (users.get(user.getId()) != null) {
            validationUsers(user);
            users.put(user.getId(), user);
            log.info("Обновление данных пользователя {}", user.getName());
            return user;
        } else {
            log.error("Пользователь не найден");
            throw new ValidationException("Пользователь не найден.");
        }
    }

    @GetMapping
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    public void validationUsers(User user) throws ValidationException {
        if (user.getName() == null || user.getName().isEmpty() || user.getName().isBlank()) {
            user.setName(user.getLogin());
            log.info("У пользователя с Email {} отсутствует имя. Его имя будет заменено логином.", user.getEmail());
        }
    }
}
