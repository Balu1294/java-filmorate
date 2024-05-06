package ru.yandex.practicum.filmorate.storage.validators;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.model.User;

@Slf4j
@UtilityClass
public class UserValidator {
    public void validationUsers(User user) {
        if (user.getName() == null || user.getName().isEmpty() || user.getName().isBlank()) {
            user.setName(user.getLogin());
            log.info("У пользователя с Email {} отсутствует имя. Его имя будет заменено логином.", user.getEmail());
        }
    }
}
