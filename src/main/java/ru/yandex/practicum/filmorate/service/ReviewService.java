package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.storage.ReviewStorage;
import ru.yandex.practicum.filmorate.storage.validators.ReviewValidator;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewStorage reviewStorage;

    public Review addReview(Review review) {
        ReviewValidator.validationReviews(review);
        log.info("Пользователь с id = {} добавляет отзыв к фильму с id = {}", review.getUserId(), review.getFilmId());
        return reviewStorage.addReview(review);
    }

    public Review updateReview(Review review) {
        ReviewValidator.validationReviews(review);
        log.info("Пользователь с id = {} обновляет отзыв к фильму с id = {}", review.getUserId(), review.getFilmId());
        return reviewStorage.updateReview(review);
    }

    public Review getReviewById(Integer id) {
        return reviewStorage.getReviewById(id).orElseThrow(() ->
                new NotFoundException(String.format("Отзыв с id= %d отсутствует в базу", id)));
    }

    public void removeReviewById(Integer id) {
        Review review = getReviewById(id);
        reviewStorage.removeReviewById(id);
        log.info("Отзыв c id= {} пользователя c id= {} для фильма с id= {} удален",
                review.getReviewId(), review.getUserId(), review.getFilmId());
    }

    public List<Review> getReviewsForFilm(Integer filmId, Integer count) {
        log.info("Получены отзывы к фильму с id= {}", filmId);
        return reviewStorage.getReviewsForFilm(filmId, count);
    }

    public void addLike(Integer reviewId, Integer userId) {
        getReviewById(reviewId);
        reviewStorage.addReaction(reviewId, userId, true);
        log.info("Добавлен лайк к отзыву с id= {} от пользователя с id= {}", reviewId, userId);
    }

    public void addDislike(Integer reviewId, Integer userId) {
        getReviewById(reviewId);
        reviewStorage.addReaction(reviewId, userId, false);
        log.info("Добавлен дизлайк к отзыву с id= {} от пользователя с id= {}", reviewId, userId);
    }

    public void removeReaction(Integer reviewId, Integer userId) {
        getReviewById(reviewId);
        reviewStorage.deleteReaction(reviewId, userId);
        log.info("Удалена реакция к отзыву с id= {} от пользователя с id= {}", reviewId, userId);
    }
}
