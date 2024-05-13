package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

public interface UserStorage {

    // Метод создания нового пользователя
    User createUser(User user) throws ValidationException;

    // Метод обновления данных о пользователе
    User updateUser(User user) throws ValidationException;

    // Метод для вывода списка всех пользователей
    List<User> getAllUsers();

    // Метод получения пользователя по id
    Optional<User> getUserById(Integer id);

    // Метод удаления пользователя по id
     void removeUser(Integer id);

     // Метод подбора рекомендаций фильмов для пользователя с id
    List<Integer> getRecommendedFilmsId(Integer userId);
}
