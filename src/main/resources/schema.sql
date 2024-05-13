drop table IF EXISTS user_friends, directors, films_directors;
drop table IF EXISTS likes;
drop table IF EXISTS reviews_likes;
drop table IF EXISTS reviews;
drop table IF EXISTS users;
drop table IF EXISTS films_genre;
drop table IF EXISTS films;
drop table IF EXISTS genres;
drop table IF EXISTS mpa;


create table if not exists mpa (
id integer generated by default as identity not null primary key,
name varchar(255) not null
);

create table if not exists films (
id integer generated by default as identity not null primary key,
name varchar(255) not null,
description varchar(255) not null,
releaseDate date not null,
duration integer not null,
mpa_id integer not null  references mpa(id)
);


create table if not exists genres (
id integer generated by default as identity not null primary key,
name varchar(255) not null
);

create table if not exists films_genre (
film_id integer not null references films(id) ON delete CASCADE ON update CASCADE,
genre_id integer not null references genres(id) ON delete RESTRICT ON update CASCADE,
PRIMARY KEY (film_id, genre_id)
);


create table if not exists  users (
id integer generated by default as identity not null primary key,
email varchar(255) unique,
name varchar(255),
login varchar(255) not null,
birthday date not null
);

create table if not exists user_friends(
    user_id       integer  NOT NULL REFERENCES users (id) ON delete CASCADE ON update CASCADE,
    friend_id     integer  NOT NULL REFERENCES users (id) ON delete CASCADE ON update CASCADE,
    friend_status boolean NOT NULL DEFAULT FALSE,
    PRIMARY KEY (user_id, friend_id)
);


create table if not exists likes (
user_id integer not null references users(id) ON delete CASCADE ON update CASCADE,
film_id integer not null references films(id) ON delete CASCADE ON update CASCADE,
PRIMARY KEY (user_id, film_id)
);


CREATE TABLE IF NOT EXISTS directors (
    director_id integer GENERATED BY DEFAULT AS IDENTITY (START WITH 1) PRIMARY KEY,
    director_name varchar
);

CREATE TABLE IF NOT EXISTS films_directors (
    film_id integer,
    director_id integer,
    FOREIGN KEY (film_id) REFERENCES films (id) ON DELETE CASCADE,
    FOREIGN KEY (director_id) REFERENCES directors (director_id) ON DELETE CASCADE,
    UNIQUE(film_id,director_id)
);

create table if not exists reviews (
    review_id integer generated by default as identity not null,
    content varchar(500) not null,
    is_positive boolean not null,
    user_id integer not null references users(id) ON delete CASCADE ON update CASCADE,
    film_id integer not null references films(id) ON delete CASCADE ON update CASCADE,
    PRIMARY KEY (review_id)
);

create table if not exists reviews_likes (
    review_id integer not null references reviews(review_id) ON delete CASCADE ON update CASCADE,
    user_id integer not null references users(id) ON delete CASCADE ON update CASCADE,
    is_like boolean,
  	PRIMARY KEY (review_id, user_id)
);

