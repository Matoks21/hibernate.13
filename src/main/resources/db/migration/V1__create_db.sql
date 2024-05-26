-- V1__create_db.sql
CREATE TABLE IF NOT EXISTS client (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(200) NOT NULL CHECK(LENGTH(name) >= 3)
);

CREATE TABLE IF NOT EXISTS planet (
    id VARCHAR(10) PRIMARY KEY CHECK (id REGEXP '^[A-Z0-9]+$'),
    name VARCHAR(500) NOT NULL CHECK(LENGTH(name) >= 1)
);

CREATE TABLE IF NOT EXISTS ticket (
    id INT AUTO_INCREMENT PRIMARY KEY,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    client_id BIGINT NOT NULL,
    from_planet_id VARCHAR(10) NOT NULL CHECK (from_planet_id REGEXP '^[A-Z0-9]+$'),
    to_planet_id VARCHAR(10) NOT NULL CHECK (to_planet_id REGEXP '^[A-Z0-9]+$'),
    FOREIGN KEY (client_id) REFERENCES client(id),
    FOREIGN KEY (from_planet_id) REFERENCES planet(id),
    FOREIGN KEY (to_planet_id) REFERENCES planet(id)
);



