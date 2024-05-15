package ru.yandex.practicum.filmorate.storage.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.enums.EventType;
import ru.yandex.practicum.filmorate.enums.Operation;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.storage.FeedStorage;
import ru.yandex.practicum.filmorate.storage.ReviewStorage;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RequiredArgsConstructor
@Component
public class ReviewDbStorage implements ReviewStorage {
    private final JdbcTemplate jdbcTemplate;
    private final FeedStorage feedStorage;

    @Override
    public Review addReview(Review review) {
        String sqlQuery = "INSERT INTO reviews(content, is_positive, user_id, film_id) " +
                "VALUES (?, ?, ?, ?) ";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"review_id"});
            stmt.setString(1, review.getContent());
            stmt.setBoolean(2, review.getIsPositive());
            stmt.setInt(3, review.getUserId());
            stmt.setInt(4, review.getFilmId());
            return stmt;
        }, keyHolder);
        review.setReviewId(Objects.requireNonNull(keyHolder.getKey().intValue()));
        feedStorage.addEvent(review.getUserId(), EventType.REVIEW.name(), Operation.ADD.name(), review.getReviewId());
        return review;
    }

    @Override
    public Review updateReview(Review review) {
        String sqlQuery = "UPDATE reviews SET content=?, is_positive=? WHERE review_id=?";
        jdbcTemplate.update(sqlQuery,
                review.getContent(),
                review.getIsPositive(),
                review.getReviewId());
        return getReviewById(review.getReviewId()).orElseThrow();
    }

    @Override
    public void removeReviewById(Integer id) {
        String sqlQuery = "DELETE FROM reviews WHERE review_id=?";
        jdbcTemplate.update(sqlQuery, id);
    }

    @Override
    public Optional<Review> getReviewById(Integer id) {
        String sqlQuery = "SELECT * FROM reviews WHERE review_id =?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sqlQuery, new RowMapper<Review>() {
                @Override
                public Review mapRow(ResultSet rs, int rowNum) throws SQLException {
                    Review review = Review.builder()
                            .reviewId(rs.getInt("review_id"))
                            .content(rs.getString("content"))
                            .isPositive(rs.getBoolean("is_positive"))
                            .userId(rs.getInt("user_id"))
                            .filmId(rs.getInt("film_id"))
                            .useful(getUsefulness(id))
                            .build();
                    return review;
                }
            }, id));
        } catch (EmptyResultDataAccessException ex) {
            return Optional.empty();
        }
    }

    @Override
    public List<Review> getReviewsForFilm(Integer filmId, Integer countOfReviews) {
        String sqlQuerywithFilmId = (filmId != null) ? " WHERE film_id= " + filmId + " " : "";

        String sqlQuery = "SELECT *, " +
                "(COUNT(SELECT user_id FROM reviews_likes AS rl WHERE rl.review_id = r.review_id AND is_like = TRUE) - " +
                "COUNT(SELECT user_id FROM reviews_likes AS rl WHERE rl.review_id = r.review_id AND is_like = FALSE)) as useful " +
                "FROM reviews AS r " + sqlQuerywithFilmId +
                "GROUP BY review_id " +
                "ORDER BY useful DESC " +
                "LIMIT " + countOfReviews;
        List<Review> allReviewsByFilm = new ArrayList<>();
        allReviewsByFilm = jdbcTemplate.query(sqlQuery, (rs, rowNum) -> makeReviews(rs));
        return allReviewsByFilm;
    }

    @Override
    public void addReaction(int reviewId, int userId, boolean isLike) {
        String sqlQuery = "INSERT INTO reviews_likes (review_id, user_id, is_like) " +
                "VALUES(?, ?, ?) ";

        jdbcTemplate.update(sqlQuery, reviewId, userId, isLike);
    }

    @Override
    public void deleteReaction(int reviewId, int userId) {
        String sqlQuery = "DELETE FROM reviews_likes WHERE review_id = ? AND user_id = ? ";
        jdbcTemplate.update(sqlQuery, reviewId, userId);
    }

    @Override
    public Integer getUsefulness(int id) {
        String sqlQueryForLikes = "SELECT COUNT(review_id) AS likes FROM reviews_likes " +
                "WHERE is_like = true AND review_id = ? ";
        String sqlQueryForDisLikes = "SELECT COUNT(review_id) AS disLikes FROM reviews_likes " +
                "WHERE is_like = false AND review_id = ? ";

        SqlRowSet reviewLikesRows = jdbcTemplate.queryForRowSet(sqlQueryForLikes, id);
        SqlRowSet reviewDisLikesRows = jdbcTemplate.queryForRowSet(sqlQueryForDisLikes, id);

        int likes = 0;
        int disLikes = 0;
        if (reviewLikesRows.next()) {
            likes = (reviewLikesRows.getInt("likes"));
        }
        if (reviewDisLikesRows.next()) {
            disLikes = (reviewDisLikesRows.getInt("disLikes"));
        }
        int usefulness = likes - disLikes;

        return usefulness;
    }

    private Review makeReviews(ResultSet rs) throws SQLException {
        String sql = "SELECT * " +
                "FROM films AS f JOIN mpa AS m ON f.mpa_id = m.id ";
        Review review = Review.builder()
                .reviewId(rs.getInt("review_id"))
                .content(rs.getString("content"))
                .isPositive(rs.getBoolean("is_positive"))
                .userId(rs.getInt("user_id"))
                .filmId(rs.getInt("film_id"))
                .useful(rs.getInt("useful"))
                .build();
        return review;
    }


}
