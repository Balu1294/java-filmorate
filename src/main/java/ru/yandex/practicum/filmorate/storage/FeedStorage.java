package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Feed;

import java.util.List;

public interface FeedStorage {

    // Метод добавления события в базу
    void addEvent(Integer userId, String eventType, String operation, Integer entityId);

    // Метод вывода списка событий
    List<Feed> getFeed(Integer userId);
}
