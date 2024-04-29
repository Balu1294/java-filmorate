package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.storage.validators.UserValidator;

import java.util.List;


@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImp implements UserService {

    private final UserStorage userStorage;
    private final UsersFriendsService friendsService;

    public User createUser(User user) throws ValidationException {
        UserValidator.validationUsers(user);
        return userStorage.createUser(user);
    }

    public User updateUser(User user) throws ValidationException {
        UserValidator.validationUsers(user);
        String exMessage = String.format("Пользователь с id = %d отсутствует в базе", user.getId());
        userStorage.getUserById(user.getId()).orElseThrow(() -> new NotFoundException(exMessage));
        return userStorage.updateUser(user);
    }

    public List<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

//    @Override
    public User getUserById(Integer id) {
        String exMessage = String.format("Пользователь с id = %d отсутствует в базе", id);
        return userStorage.getUserById(id).orElseThrow(() -> new NotFoundException(exMessage));
    }


//    public void removeUser(User user) {
//        userStorage.removeUser(user);
//    }

    //    @Override
    public void addNewFriend(int userId, int friendId) {
        log.info("Пользователь с id= {} добавляет в друзья пользователя с id= {}", userId, friendId);
        userVerification(userId, friendId);
        friendsService.addFriend(userId, friendId);
        log.info("Пользователь добавлен в друзья");
    }

    //    @Override
    public void deleteFriend(Integer userId, Integer friendId) {
        log.info("Пользовательн с id = {} удаляет из друзей пользователя с id = {}", userId, friendId);
        userVerification(userId, friendId);
        friendsService.removeFriend(userId, friendId);
        log.info("Пользователь удален из друзей");
    }

    //    @Override
    public List<User> getMutualFriends(Integer userId, Integer friendId) {
        log.info("Выводится список общих друзей пользователя с id={} и пользователя с id={}", userId, friendId);
        return friendsService.findCommonFriends(userId, friendId);
    }

    public List<User> getUserFriends(Integer userId) {
        String exMessage = String.format("Пользователь с id= {} не найден", userId);
        userStorage.getUserById(userId).orElseThrow(() -> new NotFoundException(exMessage));
        return friendsService.getFriendsForUser(userId);
    }
//    @Override
//    public List<User> getUserFriends(Integer userId) {
//        log.info("Выводится список друзей пользователя с id= {}", userId);
//        List<User> usersList = userStorage.getAllUsers();
//        if (!usersList.contains(userStorage.getUserById(userId))) {
//            throw new NotFoundException("Пользователь с id= " + userId + " отсутствует");
//        }
//        return usersList.stream()
//                .filter(user -> user.getFriends().contains(userId))
//                .collect(Collectors.toList());
//    }

    public void userVerification(Integer userId, Integer friendId) {
        String exUserMessage = String.format("Пользователь с id= {} не найден", userId);
        String exFriendMessage = String.format("Пользователь с id= {} не найден", friendId);
        userStorage.getUserById(userId).orElseThrow(() -> new NotFoundException(exUserMessage));
        userStorage.getUserById(friendId).orElseThrow(() -> new NotFoundException(exFriendMessage));
    }
}
