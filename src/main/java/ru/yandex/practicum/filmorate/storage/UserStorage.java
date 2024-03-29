package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {
    User createUser(User user) throws ValidationException;

    User updateUser(User user) throws ValidationException;

    List<User> getAllUsers();

    User addNewFriend(Integer userId, Integer friendId);

    User getUserById(Integer id);

    List<User> getFriendsByUserId(Integer id);

    User deleteFriend(Integer userId, Integer friendId);
}
