drop table IF EXISTS user_friends;
drop table IF EXISTS likes;
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
film_id integer not null references films(id) ON DELETE CASCADE ON UPDATE CASCADE,
genre_id integer not null references genres(id) ON DELETE RESTRICT ON UPDATE CASCADE,
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
    user_id       bigint  NOT NULL REFERENCES users (id) ON DELETE CASCADE ON UPDATE CASCADE,
    friend_id     bigint  NOT NULL REFERENCES users (id) ON DELETE CASCADE ON UPDATE CASCADE,
    friend_status boolean NOT NULL DEFAULT FALSE,
    PRIMARY KEY (user_id, friend_id)
);


create table if not exists likes (
user_id integer not null references users(id) ON DELETE CASCADE ON UPDATE CASCADE,
film_id integer not null references films(id) ON DELETE CASCADE ON UPDATE CASCADE,
PRIMARY KEY (user_id, film_id)
);