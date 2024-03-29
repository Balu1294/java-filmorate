package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Integer, User> users = new HashMap<>();
    private int idGenerator = 1;

    @Override
    public User createUser(User user) throws ValidationException {
        validationUsers(user);
        user.setFriends(new HashSet<>());
        user.setId(idGenerator++);
        users.put(user.getId(), user);
        log.info("Создается пользователь с id = {}. Пользователь создан.", idGenerator - 1);
        return user;
    }

    @Override
    public User updateUser(User user) throws ValidationException {
        if (users.get(user.getId()) != null) {
            validationUsers(user);
            user.setFriends(new HashSet<>());
            users.put(user.getId(), user);
            log.info("Обновление данных пользователя {}", user.getName());
            return user;
        } else {
            log.error("Пользователь не найден");
            throw new NotFoundException("Пользователь не найден.");
        }
    }

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    public void validationUsers(User user) throws ValidationException {
        if (user.getName() == null || user.getName().isEmpty() || user.getName().isBlank()) {
            user.setName(user.getLogin());
            log.info("У пользователя с Email {} отсутствует имя. Его имя будет заменено логином.", user.getEmail());
        }
    }

    @Override
    public User getUserById(Integer id) {
        return users.get(id);
    }

    @Override
    public User addNewFriend(Integer userId, Integer friendId) {
        log.info("Пользователь с id= {} добавляет в друзья пользователя с id= {}", userId, friendId);
        User user = getUserById(userId);
        User friendUser = getUserById(friendId);
        user.getFriends().add(friendId);
        friendUser.getFriends().add(userId);
        return user;

    }

    @Override
    public List<User> getFriendsByUserId(Integer id) {
        if (!users.containsKey(id)) {
            throw new NotFoundException("Пользователь с id= " + id + " отсутствует");
        }
        return getAllUsers().stream()
                .filter(user -> user.getFriends().contains(id))
                .collect(Collectors.toList());
    }

    @Override
    public User deleteFriend(Integer userId, Integer friendId) {
        if (!users.containsKey(userId)) {
            throw new NotFoundException("Пользователь с id= " + userId + " отсутствует");
        }
        if (!users.containsKey(friendId)) {
            throw new NotFoundException("Пользователь с id= " + friendId + " отсутствует");
        }
        User user = getUserById(userId);
        User friendUser = getUserById(friendId);
        user.getFriends().remove(friendId);
        friendUser.getFriends().remove(userId);
        return user;
    }
}
