package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.service.ReviewService;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Validated
@RestController
@RequestMapping("/reviews")
public class ReviewController {
    private final ReviewService reviewService;

    @PostMapping
    public Review addReview(@Valid @RequestBody Review review) {
        log.info("Поступил запрос на добавление нового отзыва");
        return reviewService.addReview(review);
    }

    @PutMapping
    public Review updateReview(@Valid @RequestBody Review review) {
        log.info("Поступил запрос на обновление отзыва");
        return reviewService.updateReview(review);
    }

    @DeleteMapping("/{id}")
    public void removeReview(@PathVariable Integer id) {
        log.info("Поступил запрос на удаление отзыва");
        reviewService.removeReviewById(id);
    }

    @GetMapping("/{id}")
    public Review getReviewById(@PathVariable Integer id) {
        log.info("Поступил запрос на получение отзыва по id");
        return reviewService.getReviewById(id);
    }

    @GetMapping
    public List<Review> getReviewsToFilm(@RequestParam(required = false) Integer filmId,
                                         @RequestParam(defaultValue = "10", required = false) Integer count) {
        log.info("Поступил запрос на поиск отзывов к фильму");
        return reviewService.getReviewsForFilm(filmId, count);
    }

    @PutMapping("/{review-id}/like/{user-id}")
    public void addLikeForReview(@PathVariable("review-id") Integer reviewId,
                                 @PathVariable("user-id") Integer userId) {
        log.info("Поступил запрос на добавление лайка отзыву");
        reviewService.addLike(reviewId, userId);
    }

    @PutMapping("/{review-id}/dislike/{user-id}")
    public void addDislikeForReview(@PathVariable("review-id") Integer reviewId,
                                    @PathVariable("user-id") Integer userId) {
        log.info("Поступил запрос на добавление дизлайка отзыву");
        reviewService.addDislike(reviewId, userId);
    }

    @DeleteMapping("/{review-id}/like/{user-id}")
    public void removeLikeForReview(@PathVariable("review-id") Integer reviewId,
                                    @PathVariable("user-id") Integer userId) {
        log.info("Поступил запрос на удаление лайка отзыву");
        reviewService.removeReaction(reviewId, userId);
    }

    @DeleteMapping("/{review-id}/dislike/{user-id}")
    public void removeDislikeForReview(@PathVariable("review-id") Integer reviewId,
                                       @PathVariable("user-id") Integer userId) {
        log.info("Поступил запрос на удаление дизлайка отзыву");
        reviewService.removeReaction(reviewId, userId);
    }
}
