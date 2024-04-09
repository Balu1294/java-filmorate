package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserServiceImp;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/users", produces = "application/json")
public class UserController {

    //    private final UserStorage userStorage;
    private final UserServiceImp userService;

    @PostMapping
    public User createUser(@RequestBody @Valid User user) throws ValidationException {
        log.info("Поступление запроса на создание пользователя");
        return userService.createUser(user);
    }

    @PutMapping
    public User updateUser(@RequestBody @Valid User user) throws ValidationException {
        log.info("Поступление запроса на обновление информации о пользователе");
        return userService.updateUser(user);
    }

    @GetMapping
    public List<User> getAllUsers() {
        log.info("Поступление запроса на вывод списка всех пользователей");
        return userService.getAllUsers();
    }

    /*Добавление в друзья */
    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable int id, @PathVariable int friendId) throws ValidationException {
        log.info("Поступил запрос на добавление в друзья.");
        userService.addNewFriend(id, friendId);
    }

    /*Удаление из друзей */
    @DeleteMapping("/{id}/friends/{friendId}")
    public void removeFriend(@PathVariable int id, @PathVariable int friendId) throws ValidationException {
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
    public List<User> getFriends(@PathVariable int id) {
        log.info("Поступил запрос на получение списка друзей.");
        return userService.getUserFriends(id);
    }

}
