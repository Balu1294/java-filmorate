package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;

public class UserDbStorage implements UserStorage {
    @Override
    public User createUser(User user) throws ValidationException {
        return null;
    }

    @Override
    public User updateUser(User user) throws ValidationException {
        return null;
    }

    @Override
    public List<User> getAllUsers() {
        return null;
    }

    @Override
    public User getUserById(Integer id) {
        return null;
    }

    @Override
    public void removeUser(User user) {

    }
}
