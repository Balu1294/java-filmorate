package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Review;

import java.util.List;
import java.util.Optional;

public interface ReviewStorage {
    // Создание нового отзыва
    Review addReview(Review review);

    // Обновление отзыва
    Review updateReview(Review review);

    // Удаление отзыва по id
    void removeReviewById(Integer id);

    // Получение отзыва по id
    Optional<Review> getReviewById(Integer id);

    // Получение отзывов к фильму
    List<Review> getReviewsForFilm(Integer filmId, Integer countOfReviews);

    //Добавление лайков и дизлайков
    void addReaction(int reviewId, int userId, boolean isLike);

    //Удаление лайков и дизлайков
    void deleteReaction(int reviewId, int userId);

    //Получение полезности отзыва
    Integer getUsefulness(int id);
}
