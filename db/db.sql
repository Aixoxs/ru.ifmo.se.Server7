CREATE SEQUENCE content_tb_id_seq
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;

CREATE TABLE IF NOT EXISTS users (
                                     id       SERIAL PRIMARY KEY NOT NULL,
                                     login    VARCHAR(255) UNIQUE NOT NULL,
                                     password VARCHAR(255) UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS music_bands (
                                           id      integer DEFAULT nextval('content_tb_id_seq') NOT NULL,
                                           name     text      NOT NULL,
                                           coordinates_x        BIGINT,
                                           coordinates_y        FLOAT,
                                           creationDate  date   NOT NULL,
                                           numberOfParticipants INT,
                                           establishmentDate    date NOT NULL,
                                           genre    VARCHAR,
                                           person_name  text NOT NULL,
                                           person_height FLOAT NOT NULL,
                                           person_eyeColor VARCHAR NOT NULL,
                                           person_hairColor VARCHAR NOT NULL,
                                           person_nationality VARCHAR NOT NULL,
                                           userID   INTEGER NOT NULL,
                                           FOREIGN KEY (userID) REFERENCES users (id)
);

insert into music_bands values (DEFAULT, 'Queen', 8, 8, '03/24/1986', 9, '06/28/1984', 'MATH_ROCK', 'Freddie', 165.78, 'BROWN', 'BLACK', 'FRANCE', 1);
insert into music_bands values (DEFAULT, 'Muse', 9, 9, '03/05/1994', 3, '07/15/1992', 'MATH_ROCK', 'Matthew', 178.8, 'BLACK', 'BLACK', 'FRANCE', 1);
insert into music_bands values (DEFAULT, 'Awolnation', 8, 8, '03/03/2009', 9, '06/24/2009', 'MATH_ROCK', 'Jimmy', 183.78, 'BROWN', 'BLACK', 'FRANCE', 1);
