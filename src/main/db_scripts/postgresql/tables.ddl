CREATE TABLE us_user (
                         id SERIAL PRIMARY KEY,
                         username VARCHAR(100) UNIQUE,
                         password VARCHAR(100) NOT NULL,
                         email VARCHAR(100),
                         name VARCHAR(100)
);

CREATE TABLE us_role (
                         id SERIAL PRIMARY KEY,
                         name VARCHAR(100) UNIQUE
);

CREATE TABLE us_user_role (
                              id SERIAL PRIMARY KEY,
                              user_id int NOT NULL REFERENCES us_user(id),
                              role_id int NOT NULL REFERENCES us_role(id)
);
