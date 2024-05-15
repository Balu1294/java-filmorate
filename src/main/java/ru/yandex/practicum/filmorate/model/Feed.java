package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class Feed {
    @JsonFormat(shape = JsonFormat.Shape.NUMBER)
    private final Timestamp timestamp;
    private final Integer userId;
    private final String eventType;
    private final String operation;
    private final Integer eventId;
    private final Integer entityId;
}
