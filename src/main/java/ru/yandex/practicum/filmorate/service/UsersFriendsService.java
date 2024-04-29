package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.UserFriend;
import ru.yandex.practicum.filmorate.storage.UserFriendsStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsersFriendsService {
    private final UserFriendsStorage userFriendsStorage;

    public void addFriend(Integer userId, Integer friendId) {
        userFriendsStorage.addFriend(userId, friendId);
    }

    public void removeFriend(Integer userId, Integer friendId) {
        userFriendsStorage.removeFriend(userId, friendId);
    }

    public List<UserFriend> getAllTable() {
        return userFriendsStorage.getAllTable();
    }

    public List<User> getFriendsForUser(Integer id) {
        return userFriendsStorage.getFriendsForUser(id);
    }

    public List<User> findCommonFriends(Integer userId, Integer friendId) {
        return userFriendsStorage.findCommonFriends(userId, friendId);
    }
}
