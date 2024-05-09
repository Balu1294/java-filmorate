package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class Film {
    int id;
    @NotBlank
    String name;
    @NotBlank
    @Size(max = 200)
    String description;
    @NotNull
    LocalDate releaseDate;
    @Positive
    int duration;
    @EqualsAndHashCode.Exclude
    @JsonIgnore
    @Builder.Default
    int likes = 0;
    @NotNull
    Mpa mpa;
    @Builder.Default
    List<Genre> genres = new ArrayList<>();
    private List<Director> directors = new ArrayList<>();
}
