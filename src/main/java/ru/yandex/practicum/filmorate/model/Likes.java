package ru.yandex.practicum.filmorate.model;

import lombok.Data;

@Data
public class Likes {
    private int userId;
    private  int filmId;
}
