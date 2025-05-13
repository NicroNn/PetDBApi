CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL,
    owner_id BIGINT,
    FOREIGN KEY (owner_id) REFERENCES owner(id)
);

INSERT INTO users(username, password, role)
VALUES ('admin', '$2a$10$S3YdJ7W5VZbX9wJ8fL4QkO4Z8tTk0yFg7sD3rQ2mN1oJvXhI5G7u', 'ROLE_ADMIN');