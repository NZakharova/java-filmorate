CREATE TABLE IF NOT EXISTS "user" (
    "userId" int   NOT NULL,
    "email" varchar(100)   NOT NULL,
    "login" varchar(100)   NOT NULL,
    "name" varchar(100)   NULL,
    "birthday" date   NOT NULL,
    CONSTRAINT "pk_user" PRIMARY KEY (
        "userId"
    )
);

CREATE TABLE IF NOT EXISTS "user_friend" (
    "userId" int   NOT NULL,
    "friendId" int   NOT NULL,
    CONSTRAINT "pk_user_friend" PRIMARY KEY (
        "userId","friendId"
    )
);

CREATE TABLE IF NOT EXISTS "film" (
    "filmId" int   NOT NULL,
    "name" varchar(100)   NOT NULL,
    "description" varchar(100)   NULL,
    "releaseDate" date   NOT NULL,
    "duration" int   NULL,
    "ratingId" int   NOT NULL,
    CONSTRAINT "pk_film" PRIMARY KEY (
        "filmId"
    )
);

CREATE TABLE IF NOT EXISTS "genre" (
    "genreId" int   NOT NULL,
    "name" varchar(100)   NOT NULL,
    CONSTRAINT "pk_genre" PRIMARY KEY (
        "genreId"
    )
);

CREATE TABLE IF NOT EXISTS "film_genre" (
    "filmId" int   NOT NULL,
    "genreId" int   NOT NULL,
    CONSTRAINT "pk_film_genre" PRIMARY KEY (
        "filmId","genreId"
    )
);

CREATE TABLE IF NOT EXISTS "rating" (
    "ratingId" int   NOT NULL,
    "name" varchar(100)   NOT NULL,
    "description" varchar(100)   NOT NULL,
    CONSTRAINT "pk_rating" PRIMARY KEY (
        "ratingId"
    )
);

CREATE TABLE IF NOT EXISTS "film_like" (
    "userId" int   NOT NULL,
    "filmId" int   NOT NULL,
    CONSTRAINT "pk_film_like" PRIMARY KEY (
        "userId","filmId"
    )
);

ALTER TABLE "user_friend"
ADD CONSTRAINT IF NOT EXISTS "fk_user_friend_userId"
FOREIGN KEY("userId")
REFERENCES "user" ("userId");

ALTER TABLE "user_friend"
ADD CONSTRAINT IF NOT EXISTS "fk_user_friend_friendId"
FOREIGN KEY("friendId")
REFERENCES "user" ("userId");

ALTER TABLE "film"
ADD CONSTRAINT IF NOT EXISTS "fk_film_ratingId"
FOREIGN KEY("ratingId")
REFERENCES "rating" ("ratingId");

ALTER TABLE "film_genre"
ADD CONSTRAINT IF NOT EXISTS "fk_film_genre_filmId"
FOREIGN KEY("filmId")
REFERENCES "film" ("filmId");

ALTER TABLE "film_genre"
ADD CONSTRAINT IF NOT EXISTS "fk_film_genre_genreId"
FOREIGN KEY("genreId")
REFERENCES "genre" ("genreId");

ALTER TABLE "film_like"
ADD CONSTRAINT IF NOT EXISTS "fk_film_like_userId"
FOREIGN KEY("userId")
REFERENCES "user" ("userId");

ALTER TABLE "film_like"
ADD CONSTRAINT IF NOT EXISTS "fk_film_like_filmId"
FOREIGN KEY("filmId")
REFERENCES "film" ("filmId");
