CREATE TABLE owner (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    birthdate DATE NOT NULL
);

CREATE TABLE pet (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    birthdate DATE NOT NULL,
    breed VARCHAR(255) NOT NULL,
    color VARCHAR(50) NOT NULL,
    owner_id INT REFERENCES owner(id) NOT NULL /* let there be no animals without owners */
);

CREATE TABLE pet_friends (
    pet_id INT REFERENCES pet(id),
    friend_id INT REFERENCES pet(id),
    PRIMARY KEY (pet_id, friend_id)
);
