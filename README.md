# Java Filmorate project
Filmorate - это бэкэнд-сервис на основе Restful API для хранения и управления информацией о фильмах(название, рейтинг MPA, жанр, описание и продолжительность), составления рейтинга фильмов на основе отзывов пользователей, поиска фильма, а также для общения пользователей.

## Стек
***Java 11, Spring Boot, Lombok, Maven, Junit, JDBC, SQL, H2***
## Реализованы следующие эндпоинты:
[//]: # (#### Фильмы)
<details><summary> 1. Фильмы </summary>

POST /films - создание фильма

PUT /films - редактирование фильма

GET /films - получение списка всех фильмов

GET /films/{id} - получение информации о фильме по его id

PUT /films/{id}/like/{userId} — поставить лайк фильму

DELETE /films/{id}/like/{userId} — удалить лайк фильма

DELETE /films/{id} - удаление фильма по id

GET /films/popular?count={count} — возвращает список из первых count фильмов по количеству лайков. Если значение параметра count не задано, возвращает первые 10.

GET /films/search?query={query}?by={by} - поиск фильмов по заголовку и режиссеру

GET /films/director/directorId={directorId}?sortBy={sortBy} - получение всех фильмов режиссера с сортировкой по лайкам или годам

GET /films/common?userId={userId}?friendId={friendId} - получение общих фильмов пользователя и его друга
</details>
<details><summary> 2. Пользователи </summary>
    
POST /users - создание пользователя

PUT /users - редактирование пользователя

GET /users - получение списка всех пользователей

DELETE /users/{userId} - удаление пользователя по id

GET /users/{id} - получение данных о пользователе по id

PUT /users/{id}/friends/{friendId} — добавление в друзья

DELETE /users/{id}/friends/{friendId} — удаление из друзей

GET /users/{id}/friends — возвращает список друзей

GET /users/{id}/friends/common/{otherId} — возвращает список друзей, общих с другим пользователем

GET /users/{id}/recommendations - получение рекомендаций по фильмам

GET /users/{id}/feed - возвращает ленту событий пользователя
</details>

<details><summary> 3. Режиссеры </summary>
POST /directors - создание режиссера

GET /directors - получение списка всех режиссеров

GET /directors/{id} - получение режиссера по id

PUT /directors - обновление режиссера

DELETE /directors/{id} - удаление режиссера по id
</details>
<details><summary> 4. Жанры </summary>
GET /genres - получение списка всех жанров

GET /genres/{id} - получение жанра по id
</details>

<details><summary> 5. MPA рейтинг </summary>

GET /mpa - получение списка всех рейтингов

GET /mpa/{id} - получение рейтинга по id
</details>

<details><summary> 6. Отзывы </summary>

POST /reviews - создание отзыва

PUT /reviews - обновление отзыва

DELETE /reviews/{id} - удаление отзыва по id

GET /reviews/{id} - получение отзыва по id

GET /reviews?filmId={filmId}?count={count} - получение count отзывов по id фильма. Если значение не задано, возвращает первые 10
</details>

<details><summary> 7. Лайки </summary>

PUT /reviews/{id}/like/{userId} - добавление лайка

PUT /reviews/{id}/dislike/{userId} - добавление дизлайка

DELETE /reviews/{id}/like{userId} - удаление лайка

DELETE /reviews{id}/dislike{userId} - удаление дизлайка
</details>

## ER - Диаграмма приложения Filmorate:
![Это ссылка на ER диаграмму по данному проекту](https://github.com/Balu1294/java-filmorate/blob/develop/ER-%D0%B4%D0%B8%D0%B0%D0%B3%D1%80%D0%B0%D0%BC%D0%BC%D0%B0%20filmorate.png)
