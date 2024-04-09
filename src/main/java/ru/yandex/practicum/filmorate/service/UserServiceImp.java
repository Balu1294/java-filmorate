package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImp implements UserService {

    private final UserStorage userStorage;

    @Override
    public User createUser(User user) throws ValidationException {
        return userStorage.createUser(user);
    }

    @Override
    public User updateUser(User user) throws ValidationException {
        return userStorage.updateUser(user);
    }

    @Override
    public List<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    @Override
    public void removeUser(User user) {
        userStorage.removeUser(user);
    }

    @Override
    public void addNewFriend(int userId, int friendId) throws ValidationException {
        log.info("Пользователь с id= {} добавляет в друзья пользователя с id= {}", userId, friendId);
        List<User> usersList = userStorage.getAllUsers();
        if (usersList.contains(userStorage.getUserById(userId)) && usersList.contains(userStorage.getUserById(friendId))) {
            User user = userStorage.getUserById(userId);
            User friendUser = userStorage.getUserById(friendId);
            user.addFriend(friendId);
            friendUser.addFriend(userId);
            userStorage.updateUser(user);
            userStorage.updateUser(friendUser);
        } else {
            String exMessage = String.format("Пользователи с id= {} и {} отсутствуют", userId, friendId);
            throw new NotFoundException(exMessage);
        }
    }

    @Override
    public void deleteFriend(Integer userId, Integer friendId) throws ValidationException {
        List<User> usersList = userStorage.getAllUsers();
        if (!usersList.contains(userStorage.getUserById(userId))) {
            throw new NotFoundException("Пользователь с id= " + userId + " отсутствует");
        }
        if (!usersList.contains(userStorage.getUserById(friendId))) {
            throw new NotFoundException("Пользователь с id= " + friendId + " отсутствует");
        }
        User user = userStorage.getUserById(userId);
        User friendUser = userStorage.getUserById(friendId);
        user.removeFriend(friendId);
        friendUser.removeFriend(userId);
        userStorage.updateUser(user);
        userStorage.updateUser(friendUser);
    }

    @Override
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

    @Override
    public List<User> getUserFriends(Integer userId) {
        log.info("Выводится список друзей пользователя с id= {}", userId);
        List<User> usersList = userStorage.getAllUsers();
        if (!usersList.contains(userStorage.getUserById(userId))) {
            throw new NotFoundException("Пользователь с id= " + userId + " отсутствует");
        }
        return usersList.stream()
                .filter(user -> user.getFriends().contains(userId))
                .collect(Collectors.toList());
    }
}
