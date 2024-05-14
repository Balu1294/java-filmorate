package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.enums.EventType;
import ru.yandex.practicum.filmorate.enums.Operation;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Feed;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FeedStorage;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.storage.validators.UserValidator;

import java.util.ArrayList;
import java.util.List;


@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserStorage userStorage;
    private final FilmStorage filmStorage;
    private final UsersFriendsService friendsService;
    private final FeedStorage feedStorage;

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

    public User getUserById(Integer id) {
        String exMessage = String.format("Пользователь с id = %d отсутствует в базе", id);
        return userStorage.getUserById(id).orElseThrow(() -> new NotFoundException(exMessage));
    }

    public void addNewFriend(Integer userId, Integer friendId) {
        log.info("Пользователь с id= {} добавляет в друзья пользователя с id= {}", userId, friendId);
        userVerification(userId, friendId);
        friendsService.addFriend(userId, friendId);
        log.info("Пользователь добавлен в друзья");
        feedStorage.addEvent(userId, EventType.FRIEND.name(), Operation.ADD.name(), friendId);
    }

    public void deleteFriend(Integer userId, Integer friendId) {
        log.info("Пользовательн с id = {} удаляет из друзей пользователя с id = {}", userId, friendId);
        userVerification(userId, friendId);
        friendsService.removeFriend(userId, friendId);
        log.info("Пользователь удален из друзей");
        feedStorage.addEvent(userId, EventType.FRIEND.name(), Operation.REMOVE.name(), friendId);
    }

    public List<User> getMutualFriends(Integer userId, Integer friendId) {
        log.info("Выводится список общих друзей пользователя с id={} и пользователя с id={}", userId, friendId);
        return friendsService.findCommonFriends(userId, friendId);
    }

    public List<User> getUserFriends(Integer userId) {
        String exMessage = String.format("Пользователь с id= %d не найден", userId);
        userStorage.getUserById(userId).orElseThrow(() -> new NotFoundException(exMessage));
        return friendsService.getFriendsForUser(userId);
    }

    public void userVerification(Integer userId, Integer friendId) {
        String exUserMessage = String.format("Пользователь с id= %d не найден", userId);
        String exFriendMessage = String.format("Пользователь с id= %d не найден", friendId);
        userStorage.getUserById(userId).orElseThrow(() -> new NotFoundException(exUserMessage));
        userStorage.getUserById(friendId).orElseThrow(() -> new NotFoundException(exFriendMessage));
    }

    /* Удаление пользователя по id*/
    public void removeUser(Integer id) {
        userStorage.getUserById(id).orElseThrow(() ->
                new NotFoundException(String.format("Пользователь с id = %d не найден", id)));
        userStorage.removeUser(id);
        log.info("Удален пользователь с id = {}", id);
    }

    // Метод подбора рекомендаций фильмов для пользователя с id
    public List<Film> getRecommendedFilms(Integer userId) {
        log.info("Запрос на вывод рекомендаций фильмов");
        List<Film> recommendedFilms = new ArrayList<>();
        List<Integer> filmsId = userStorage.getRecommendedFilmsId(userId);
        for (Integer filmId : filmsId) {
            recommendedFilms.add(filmStorage.getFilmById(filmId).orElseThrow());
        }
        return recommendedFilms;
    }

    // Методо вывода списка событий для пользователя с id
    public List<Feed> getFeed(Integer userId) {
        getUserById(userId);
        return feedStorage.getFeed(userId);
    }
}
