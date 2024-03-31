package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserService {
    User createUser(User user) throws ValidationException;

    User updateUser(User user) throws ValidationException;

    List<User> getAllUsers();

    void removeUser(User user);

    void addNewFriend(int userId, int friendId) throws ValidationException;

    void deleteFriend(Integer userId, Integer friendId) throws ValidationException;

    List<User> getMutualFriends(Integer userId, Integer friendId);

    List<User> getUserFriends(Integer userId);
}
