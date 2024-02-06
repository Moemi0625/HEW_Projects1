CREATE USER webtuser WITH PASSWORD 'pass';
CREATE DATABASE webtdb OWNER webtuser ENCODING 'UTF8'; 

DROP TABLE IF EXISTS webt;

CREATE TABLE webt
(
    id SERIAL PRIMARY KEY,
    title TEXT,
    author TEXT,
    synopsis TEXT,
    genres VARCHAR[],
    rating INTEGER,
    done TEXT,
    start_year INTEGER,
    image_path TEXT
);

INSERT INTO webt (title, author, synopsis, genres, rating, done, start_year, image_path)
VALUES
 ('Title1', 'Author1', 'Synopsis', ARRAY['action'], 3, 'Y', 2013, '/images/webt1.jpg'),
 ('Title2', 'Author2', 'Synopsis', ARRAY['romance', 'fantasy'], 4, 'N', 2022, '/images/webt2.jpg'),
 ('Title3', 'Author3', 'Synopsis', ARRAY[ 'fantasy'], 3, 'Y', 2022, '/images/webt3.jpg'),
 ('Title4', 'Author4', 'Synopsis', ARRAY['action'], 3, 'Y', 2019, '/images/webt4.jpg'),
 ('Title5', 'Author5', 'Synopsis', ARRAY['romance', 'fantasy'], 1, 'N', 2022, '/images/webt1.jpg'),
 ('Title6', 'Author6', 'Synopsis', ARRAY[ 'fantasy'], 3, 'Y', 2024, '/images/webt2.jpg'),
 ('Title7', 'Author7', 'Synopsis', ARRAY['action'], 3, 'Y', 2020, '/images/webt1.jpg'),
 ('Title8', 'Author8', 'Synopsis', ARRAY['suspense', 'fantasy'], 3, 'N', 2022, '/images/webt3.jpg'),
 ('Title9', 'Author9', 'Synopsis', ARRAY[ 'fantasy'], 3, 'Y', 2023, '/images/webt4.jpg'),
 ('Title10', 'Author10', 'Synopsis', ARRAY['others'], 3, 'Y', 2018, '/images/webt1.jpg'),
 ('Title11', 'Author11', 'Synopsis', ARRAY['romance', 'fantasy'], 2, 'N', 2022, '/images/webt2.jpg'),
 ('Title12', 'Author12', 'Synopsis', ARRAY[ 'comedy'], 3, 'Y', 2021, '/images/webt3.jpg'),
 ('Title13', 'Author13', 'Synopsis', ARRAY['action', 'fantasy'], 5, 'Y', 2021, '/images/webt4.jpg');


DROP TABLE IF EXISTS webt_acct;

CREATE TABLE webt_acct
(
    id SERIAL PRIMARY KEY,
    login_id TEXT UNIQUE,
    name TEXT,
    password TEXT,
    image_path TEXT
);

INSERT INTO webt_acct (login_id, name, password, image_path)
VALUES
('akahori', 'akahori', 'akahori0625', 'null');


