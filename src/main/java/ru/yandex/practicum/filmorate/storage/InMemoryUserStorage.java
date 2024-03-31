package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Integer, User> users = new HashMap<>();
    private int idGenerator = 1;

    @Override
    public User createUser(User user) throws ValidationException {
        validationUsers(user);
        user.setId(idGenerator++);
        users.put(user.getId(), user);
        log.info("Создается пользователь с id = {}. Пользователь создан.", idGenerator - 1);
        return user;
    }

    @Override
    public User updateUser(User user) throws ValidationException {
        if (users.containsKey(user.getId())) {
            validationUsers(user);
            users.put(user.getId(), user);
            log.info("Обновление данных пользователя {}", user.getName());
            return user;
        } else {
            log.error("Пользователь не найден");
            throw new NotFoundException("Пользователь не найден.");
        }
    }

    @Override
    public void removeUser(User user) {
        if (users.containsKey(user.getId())) {
            users.remove(user.getId());
        } else {
            throw new NotFoundException("Пользователь " + user.getName() + " отсутствует");
        }
    }

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    public void validationUsers(User user) {
        if (user.getName() == null || user.getName().isEmpty() || user.getName().isBlank()) {
            user.setName(user.getLogin());
            log.info("У пользователя с Email {} отсутствует имя. Его имя будет заменено логином.", user.getEmail());
        }
    }

    @Override
    public User getUserById(Integer id) {
        if (users.containsKey(id)) {
            return users.get(id);
        } else {
            throw new NotFoundException("Пользователь с id= " + id + "отсутствует.");
        }
    }
}
