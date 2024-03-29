package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/users", produces = "application/json")
public class UserController {

    private final UserStorage userStorage;
    private final UserService userService;

    @PostMapping
    public User createUser(@RequestBody @Valid User user) throws ValidationException {
        log.info("Поступление запроса на создание пользователя");
        return userStorage.createUser(user);
    }

    @PutMapping
    public User updateUser(@RequestBody @Valid User user) throws ValidationException {
        log.info("Поступление запроса на обновление информации о пользователе");
        return userStorage.updateUser(user);
    }

    @GetMapping
    public List<User> getAllUsers() {
        log.info("Поступление запроса на вывод списка всех пользователей");
        return userStorage.getAllUsers();
    }

    /*Добавление в друзья */
    @PutMapping("/{id}/friends/{friendId}")
    public User addFriend(@PathVariable String id, @PathVariable String friendId) {
        log.info("Поступил запрос на добавления в друзья.");
        return userService.addNewFriend(Integer.parseInt(id), Integer.parseInt(friendId));
    }

    /*Удаление из друзей */
    @DeleteMapping("/{id}/friends/{friendId}")
    public void removeFriend(@PathVariable int id, @PathVariable int friendId) {
        log.info("Поступление запроса на удаление из друзей");
        userService.deleteFriend(id, friendId);
    }


    /*Вывод общих друзей с другим пользователем */
    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> outMutualFriends(@PathVariable int id, @PathVariable int otherId) {
        log.info("Поступление запроса на вывод общих друзей пользователя с id= {} с пользователем c id= {} ",
                id, otherId);
        return userService.getMutualFriends(id, otherId);
    }

    /*Вывод списка друзей пользователя */
    @GetMapping("/{id}/friends")
    public List<User> getFriends(@PathVariable String id) {
        log.info("Поступил запрос на получение списка друзей.");
        return userService.getUserFriends(Integer.parseInt(id));
    }

}
