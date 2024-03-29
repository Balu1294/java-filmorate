package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserStorage userStorage;

    public User addNewFriend(int userId, int friendId) {
        log.info("Пользователь с id= {} добавляет в друзья пользователя с id= {}", userId, friendId);
        userStorage.addNewFriend(userId, friendId);
        return userStorage.getUserById(userId);
    }

    public User deleteFriend(Integer userId, Integer friendId) {
        log.info("Пользователь с id= {} удаляет из друзей пользователя с id= {}", userId, friendId);
        userStorage.deleteFriend(userId, friendId);
        return userStorage.getUserById(userId);
    }

    public List<User> getMutualFriends(Integer userId, Integer friendId) {
        User user = userStorage.getUserById(userId);
        User friendUser = userStorage.getUserById(friendId);
        List<Integer> mutualFriends = new ArrayList<>(user.getFriends());
        mutualFriends.retainAll(friendUser.getFriends());
        List<User> mutualUsers = new ArrayList<>();
        for (Integer id : mutualFriends) {
            mutualUsers.add(userStorage.getUserById(id));
        }
        log.info("Выводится список общих друзей пользователя с id={} и пользователя с id={}", userId, friendId);
        return mutualUsers;
    }

    public List<User> getUserFriends(Integer userId) {
        log.info("Выводится список друзей пользователя с id= {}", userId);
        return userStorage.getFriendsByUserId(userId);
    }
}
