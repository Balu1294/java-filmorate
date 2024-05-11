package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.dao.UserDbStorage;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class UserDbTest {

    private final UserDbStorage userStorage;
    private final UserService userService;
    private User userTest;

    @BeforeEach
    public void beforeData() {
        userTest = new User(1, "testuser@Yandex.ru", "tuser", "Тестовый пользователь",
                LocalDate.of(2004, 1, 5));
        userStorage.createUser(userTest);
    }

    @Test
    void getUserById() {
        Optional<User> firstUser = userStorage.getUserById(1);
        Assertions.assertThat(firstUser)
                .isPresent()
                .hasValueSatisfying(user ->
                        Assertions.assertThat(user).hasFieldOrPropertyWithValue("id", 1))
                .hasValueSatisfying(user ->
                        Assertions.assertThat(user).hasFieldOrPropertyWithValue("email", "testuser@Yandex.ru"))
                .hasValueSatisfying(user ->
                        Assertions.assertThat(user).hasFieldOrPropertyWithValue("name", "tuser"))
                .hasValueSatisfying(user ->
                        Assertions.assertThat(user).hasFieldOrPropertyWithValue("login", "Тестовый пользователь"))
                .hasValueSatisfying(user ->
                        Assertions.assertThat(user).hasFieldOrPropertyWithValue("birthday", LocalDate.of(2004, 1, 5)));
    }

    @Test
    void userUpdate() throws ValidationException {
        userTest.setLogin("Тестовый пользователь 2");
        userTest.setName("");
        userTest.setEmail("test2@mail.ru");
        userTest.setBirthday(LocalDate.of(2020, 5, 26));
        userService.updateUser(userTest);
        Optional<User> secondUser = userStorage.getUserById(1);
        Assertions.assertThat(secondUser)
                .isPresent()
                .hasValueSatisfying(user ->
                        Assertions.assertThat(user).hasFieldOrPropertyWithValue("id", 1))
                .hasValueSatisfying(user ->
                        Assertions.assertThat(user).hasFieldOrPropertyWithValue("name", "Тестовый пользователь 2"))
                .hasValueSatisfying(user ->
                        Assertions.assertThat(user).hasFieldOrPropertyWithValue("email", "test2@mail.ru"))
                .hasValueSatisfying(user ->
                        Assertions.assertThat(user).hasFieldOrPropertyWithValue("birthday", LocalDate.of(2020, 5, 26)))
                .hasValueSatisfying(user ->
                        Assertions.assertThat(user).hasFieldOrPropertyWithValue("login", "Тестовый пользователь 2"));
    }

    @Test
    void getAllUsers() {
        User newUser = new User(2, "newUser@gmail.com", "NUser", "Сергей", LocalDate.of(1995, 12, 5));
        userStorage.createUser(newUser);
        List<User> users = userStorage.getAllUsers();
        Assertions.assertThat(users).isNotEmpty().isNotNull().doesNotHaveDuplicates();
        Assertions.assertThat(users).extracting("login").contains(newUser.getLogin());
        Assertions.assertThat(users).extracting("email").contains(userTest.getEmail());

    }
}
