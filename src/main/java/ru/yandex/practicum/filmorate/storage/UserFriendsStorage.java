package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.UserFriend;

import java.util.List;

public interface UserFriendsStorage {

    //Добавление в друзья
    void addFriend(Integer userId, Integer friendId);

    // Удаление из друзей
    void removeFriend(Integer userId, Integer friendId);

    // Показать всю таблицу
    List<UserFriend> getAllTable();

    // Показать друзей пользователя по id
    List<User> getFriendsForUser(Integer id);

    // Показать общих друзей
    List<User> findCommonFriends(Integer userId, Integer friendId);
}
