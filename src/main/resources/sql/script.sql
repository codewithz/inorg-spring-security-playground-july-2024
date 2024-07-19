CREATE TABLE users (
                       username VARCHAR(50) NOT NULL PRIMARY KEY,
                       password VARCHAR(500) NOT NULL,
                       enabled BOOLEAN NOT NULL
);


CREATE TABLE authorities (
                             username VARCHAR(50) NOT NULL,
                             authority VARCHAR(50) NOT NULL,
                             CONSTRAINT fk_authorities_users FOREIGN KEY (username) REFERENCES users(username)
);

CREATE UNIQUE INDEX ix_auth_username ON authorities (username, authority);

INSERT INTO users (username, password, enabled) VALUES
    ('user', '{noop}inorg@12345', '1')
    ON CONFLICT (username) DO NOTHING;

INSERT INTO users (username, password, enabled) VALUES
    ('admin', '{bcrypt}$2a$12$f0OhQ8.5zVYfXY87JcsQcOfwb7LezvhYx3OLGUzNlawBVrQnIXcWm', '1')
    ON CONFLICT (username) DO NOTHING;

INSERT INTO authorities (username, authority) VALUES
    ('user', 'read')
    ON CONFLICT (username, authority) DO NOTHING;

INSERT INTO authorities (username, authority) VALUES
    ('admin', 'admin')
    ON CONFLICT (username, authority) DO NOTHING;


CREATE TABLE customer (
                          id SERIAL PRIMARY KEY,
                          email VARCHAR(45) NOT NULL,
                          pwd VARCHAR(200) NOT NULL,
                          role VARCHAR(45) NOT NULL
);


INSERT INTO customer (email, pwd, role) VALUES
                                            ('happy@example.com', '{noop}inorg@12345', 'read'),
                                            ('admin@example.com', '{bcrypt}$2a$12$f0OhQ8.5zVYfXY87JcsQcOfwb7LezvhYx3OLGUzNlawBVrQnIXcWm', 'admin');

--- Changes for AuthN and AuthZ

-- ----------- Query changes for AuthN & AuthZ module ---------------

-- Drop table if it exists
DROP TABLE IF EXISTS customer;

-- Create customer table
CREATE TABLE customer (
                          customer_id SERIAL PRIMARY KEY,
                          name VARCHAR(100) NOT NULL,
                          email VARCHAR(100) NOT NULL,
                          mobile_number VARCHAR(20) NOT NULL,
                          pwd VARCHAR(500) NOT NULL,
                          role VARCHAR(100) NOT NULL,
                          create_dt DATE DEFAULT NULL
);
-- $2a$12$pHuvQ9nb/R6axX.x2ZpuJuoWAE1DzvQNzT21Ovnzvdj4OH4ViGe6. -- cwz@123
-- Insert data into customer table
INSERT INTO customer (name, email, mobile_number, pwd, role, create_dt)
VALUES
    ('Tom', 'tom@example.com', '9876548337', '{bcrypt}$2a$12$pHuvQ9nb/R6axX.x2ZpuJuoWAE1DzvQNzT21Ovnzvdj4OH4ViGe6.', 'admin', CURRENT_DATE),
    ('Alex', 'alex@example.com', '9876548337', '{bcrypt}$2a$12$pHuvQ9nb/R6axX.x2ZpuJuoWAE1DzvQNzT21Ovnzvdj4OH4ViGe6.', 'admin', CURRENT_DATE),
    ('Mike', 'mike@example.com', '9876548337', '{bcrypt}$2a$12$pHuvQ9nb/R6axX.x2ZpuJuoWAE1DzvQNzT21Ovnzvdj4OH4ViGe6.', 'admin', CURRENT_DATE);

-- Drop table if it exists
DROP TABLE IF EXISTS authorities;

-- Create authorities table
CREATE TABLE authorities (
                             id SERIAL PRIMARY KEY,
                             customer_id INT NOT NULL,
                             name VARCHAR(50) NOT NULL,
                             FOREIGN KEY (customer_id) REFERENCES customer (customer_id)
);

-- Insert data into authorities table
INSERT INTO authorities (customer_id, name)
VALUES
    (1, 'READ'),
    (1, 'WRITE');

INSERT INTO authorities (customer_id, name)
VALUES
    (2, 'READ');


INSERT INTO authorities (customer_id, name)
VALUES
    (3, 'WRITE');