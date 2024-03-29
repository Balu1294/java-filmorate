package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class UserService {
    InMemoryUserStorage userStorage;

    @Autowired
    public UserService(InMemoryUserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User addNewFriends(Integer userId, Integer friendId) {
        User user = userStorage.getUsers().get(userId);
        User friendUser = userStorage.getUsers().get(friendId);
        user.getFriends().add(friendId);
        friendUser.getFriends().add(userId);
        return user;
    }

    public void deleteFriend(Integer userId, Integer friendId) {
        User user = userStorage.getUsers().get(userId);
        User friendUser = userStorage.getUsers().get(friendId);
        user.getFriends().remove(friendId);
        friendUser.getFriends().remove(userId);
    }

    public List<Integer> getMutualFriends(Integer userId, Integer friendId) {
        User user = userStorage.getUsers().get(userId);
        User friendUser = userStorage.getUsers().get(friendId);
        List<Integer> mutualFriends = new ArrayList<>(user.getFriends());
        mutualFriends.retainAll(friendUser.getFriends());
        return mutualFriends;
    }

    public List<Integer> getFriendsOfUser(int id) {
        User user = userStorage.getUsers().get(id);
        return new ArrayList<>(user.getFriends());
    }
}
