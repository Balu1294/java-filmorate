package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.time.LocalDate;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private int id;
    @Email
    @NotNull
    private String email;
    @NotNull
    @NotBlank
    private String login;
    private String name;
    @Past
    private LocalDate birthday;
    //private Set<Integer> friends;

    public User(String email, String login, String name, LocalDate birthday) {
        this.email = email;
        this.login = login;
        this.name = name;
        this.birthday = birthday;
        //  this.friends = new HashSet<>();
    }
/*
    public Set<Integer> getFriends() {
        return new HashSet<>(friends);
    }

    public void addFriend(int id) {
        friends.add(id);
    }

    public void removeFriend(int id) {
        friends.remove(id);
    }*/
}
