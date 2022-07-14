# java-filmorate

## База данных

![db](./images/db%20diagram.png)

Заявка на друзья работает как в ВК: люди подписываются друг на друга.
Если оба человека подписаны - они друзья.
Если человек подписан, но на него не подписались в ответ, он остаётся просто подписчиком, не другом.

### Запросы

1. Получение списка друзей пользователя с `id` = 10

```sql
SELECT DISTINCT f."FriendId"
from "User_Friend" as f
INNER JOIN "User_Friend" AS uf ON uf."FriendId" = 10
WHERE f."UserId" = 10
```

2. Получение списка подписок пользователя с `id` = 10

```sql
SELECT "FriendId" FROM "User_Friend" WHERE "UserId" = 10
```

3. Получение общего списка друзей у пользователей 2 и 3

```sql
SELECT DISTINCT f."FriendId"
FROM "User_Friend" as f
INNER JOIN "User_Friend" AS uf ON uf."FriendId" = f."FriendId"
WHERE f."UserId" = 2 AND uf."UserId" = 3
```

3. Получение списка фильмов с рейтингом `G`

```sql
SELECT f."Name"
FROM "Film" as f
INNER JOIN "Rating" AS r ON r."RatingID" = f."RatingID"
WHERE r."Name" = 'G'
```

4. Получение фильмов в жанре "комедия"

```sql
SELECT DISTINCT f."Name"
FROM "Film" AS f
INNER JOIN "Film_Genre" AS fg ON fg."FilmID" = f."FilmID"
INNER JOIN "Genre" AS g ON g."GenreID" = fg."GenreID"
WHERE g."Name" = 'Комедия'
```

5. Получение 10 самых популярных фильмов

```sql
SELECT f."Name"
FROM "Film" AS f
INNER JOIN "Film_Like" AS fl ON f."FilmID" = fl."FilmID"
GROUP BY f."Name"
ORDER BY COUNT(fl) DESC
LIMIT 10
```
