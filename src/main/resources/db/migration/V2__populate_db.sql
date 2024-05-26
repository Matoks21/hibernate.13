-- V2__populate_db.sql
-- Додати клієнтів
INSERT INTO client (name) VALUES
    ('John One'),
    ('Alice Two'),
    ('Michael Three'),
    ('Emily Four'),
    ('William Five'),
    ('Emma Six'),
    ('James Seven'),
    ('Olivia Eight'),
    ('Daniel Nine'),
    ('Sophia Ten');

-- Додати планети
INSERT INTO planet (id, name) VALUES
    ('MARS', 'Mars'),
    ('VEN', 'Venus'),
    ('EARTH', 'Earth'),
    ('JUP', 'Jupiter'),
    ('SAT', 'Saturn');

-- Додати квитки
INSERT INTO ticket (client_id, from_planet_id, to_planet_id,created_at) VALUES
    (1, 'EARTH', 'MARS', CURRENT_TIMESTAMP),
    (2, 'VEN', 'JUP', CURRENT_TIMESTAMP),
    (3, 'MARS', 'EARTH', CURRENT_TIMESTAMP),
    (4, 'SAT', 'VEN', CURRENT_TIMESTAMP),
    (5, 'EARTH', 'JUP', CURRENT_TIMESTAMP),
    (6, 'MARS', 'SAT', CURRENT_TIMESTAMP),
    (7, 'JUP', 'EARTH', CURRENT_TIMESTAMP),
    (8, 'VEN', 'MARS', CURRENT_TIMESTAMP),
    (9, 'SAT', 'EARTH', CURRENT_TIMESTAMP),
    (10, 'EARTH', 'VEN', CURRENT_TIMESTAMP);


