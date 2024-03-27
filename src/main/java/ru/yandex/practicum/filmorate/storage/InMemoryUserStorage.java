package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {
    private Map<Integer, User> users = new HashMap<>();
    private int idGenerator = 1;

    @Override
    public User createUser(@Valid User user) throws ValidationException {
        validationUsers(user);
        user.setId(idGenerator++);
        users.put(user.getId(), user);
        log.info("Создается пользователь с id = {}. Пользователь создан.", idGenerator - 1);
        return user;
    }

    @Override
    public User updateUser(@Valid User user) throws ValidationException {
        if (users.get(user.getId()) != null) {
            validationUsers(user);
            users.put(user.getId(), user);
            log.info("Обновление данных пользователя {}", user.getName());
            return user;
        } else {
            log.error("Пользователь не найден");
            throw new ValidationException("Пользователь не найден.");
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
}
