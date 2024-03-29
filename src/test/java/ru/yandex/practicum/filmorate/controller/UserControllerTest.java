package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;

class UserControllerTest {
    User testuser;
    UserController userController;
    UserStorage userStorage;
    UserService userService;
    Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @BeforeEach
    void init() {
        testuser = User.builder()
                .name("Тестовый пользователь")
                .login("LoginTest")
                .email("yandex@ya.ru")
                .birthday(LocalDate.of(1994, 12, 06))
                .build();
        userController = new UserController(userStorage, userService);
    }

    @Test
    void createNewOkUser() throws ValidationException {
        userController.createUser(testuser);
        Set<ConstraintViolation<User>> violations = validator.validate(testuser);
        assertTrue(violations.size() == 0);
    }

    @Test
    void createNewNullNameUser() throws ValidationException {
        testuser.setName("");
        userController.createUser(testuser);
        Assertions.assertEquals("LoginTest", testuser.getName());
    }

    @Test
    void createNewEmptyLoginUser() throws ValidationException {
        testuser.setLogin("");
        userController.createUser(testuser);
        Set<ConstraintViolation<User>> violations = validator.validate(testuser);
        assertTrue(violations.size() == 1);
    }

    @Test
    void createNewBadEmailUser() throws ValidationException {
        testuser.setEmail("yandex.ya.ru");
        userController.createUser(testuser);
        Set<ConstraintViolation<User>> violations = validator.validate(testuser);
        assertTrue(violations.size() == 1);
    }

    @Test
    void createNewBadBirthdayUser() throws ValidationException {
        testuser.setBirthday(LocalDate.of(2025, 1, 1));
        userController.createUser(testuser);
        Set<ConstraintViolation<User>> violations = validator.validate(testuser);
        assertTrue(violations.size() == 1);
    }
}