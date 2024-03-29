package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
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
    UserService userService;

    @Autowired
    public UserController(InMemoryUserStorage userStorage, UserService userService) {
        this.userStorage = userStorage;
        this.userService = userService;
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
    /*Добавление в друзья */

    @PutMapping("/users/{id}/friends/{friendId}")
    public User addFriend(@PathVariable int id, @PathVariable int friendId) {
        return userService.addNewFriends(id, friendId);
    }
    /*Удаление из друзей */

    @DeleteMapping("/users/{id}/friends/{friendId}")
    public void removeFriend(@PathVariable int id, @PathVariable int friendId) {
        userService.deleteFriend(id, friendId);
    }
    /*Вывод общих друзей с другим пользователем */

    @GetMapping("/users/{id}/friends/common/{otherId}")
    public List<Integer> outMutualFriends(@PathVariable int id, @PathVariable("value = otherId") int friendId) {
        return userService.getMutualFriends(id, friendId);
    }

    /*Вывод списка друзей пользователя */
    @GetMapping("/users/{id}/friends")
    public List<Integer> outFriendsOfUser(@PathVariable int id) {
        return userService.getFriendsOfUser(id);
    }

}
