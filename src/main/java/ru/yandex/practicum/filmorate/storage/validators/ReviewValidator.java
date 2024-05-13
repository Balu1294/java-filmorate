package ru.yandex.practicum.filmorate.storage.validators;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Review;

@Slf4j
@UtilityClass
public class ReviewValidator {

    public void validationReviews(Review review) {
        if (review.getUserId() < 1) {
            throw new NotFoundException("id пользователя не может быть меньше 1");
        }
        if (review.getFilmId() < 1) {
            throw new NotFoundException("id фильма не может быть меньше 1");
        }
    }
}
