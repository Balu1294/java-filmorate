package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Film {
    private int id;
    @NotBlank
    private String name;
    @NotNull
    @Size(max = 200)
    private String description;
    @NotNull
    private LocalDate releaseDate;
    @Positive

    private int duration;
    @EqualsAndHashCode.Exclude
    @JsonIgnore
    @Builder.Default
    private int likes = 0;
    @NotNull
    private Mpa mpa;
    @Builder.Default
    private Set<Genre> genres = new HashSet<>();

//    public Film(String name, String description, LocalDate releaseDate, int duration, Mpa mpa) {
//        this.name = name;
//        this.description = description;
//        this.releaseDate = releaseDate;
//        this.duration = duration;
//        this.mpa = mpa;
//        //this.likes = new HashSet<>();
//    }
//
//    public Set<Integer> getLikes() {
//        return new HashSet<>(likes);
//    }
//
//    public void addLike(int id) {
//        likes.add(id);
//    }
//
//    public void removeLike(int id) {
//        likes.remove(id);
//    }
}
