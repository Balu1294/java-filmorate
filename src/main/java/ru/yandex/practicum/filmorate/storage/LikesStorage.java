package ru.yandex.practicum.filmorate.storage;

public interface LikesStorage {

    //Поставить лайк
    void addLike(Integer userId, Integer filmId);

    //Удалить лайк
    void removeLike(Integer userId, Integer filmId);
}
