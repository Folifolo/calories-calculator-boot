DROP TABLE IF EXISTS ingredient cascade;
DROP TABLE IF EXISTS dish_portion cascade;
DROP TABLE IF EXISTS dish_ingredient cascade;
DROP TABLE IF EXISTS account cascade;
DROP TABLE IF EXISTS dish cascade;



CREATE TABLE ingredient (
                              id INT AUTO_INCREMENT  PRIMARY KEY,
                              name VARCHAR(250) NOT NULL,
                              calories INT
);

CREATE TABLE dish_portion (
                            id INT AUTO_INCREMENT  PRIMARY KEY,
                            dish_id INT,
                            date DATE,
                            calories DOUBLE
);

CREATE TABLE dish_ingredient (
                            id INT AUTO_INCREMENT  PRIMARY KEY,
                            dish_id INT,
                            ingredient_id INT,
                            weight INT
);

CREATE TABLE account (
                         id INT AUTO_INCREMENT  PRIMARY KEY,
                         name VARCHAR(250) NOT NULL ,
                         password VARCHAR(250) NOT NULL
);

CREATE TABLE dish (
                                 id INT AUTO_INCREMENT  PRIMARY KEY,
                                 name VARCHAR(250) NOT NULL,
                                 weight INT
);

