CREATE DATABASE bus_reservation;
USE bus_reservation;
CREATE TABLE buses (
    bus_id INT PRIMARY KEY AUTO_INCREMENT,
    bus_name VARCHAR(50),
    capacity INT
);

CREATE TABLE routes (
    route_id INT PRIMARY KEY AUTO_INCREMENT,
    bus_id INT,
    origin VARCHAR(50),
    destination VARCHAR(50),
    FOREIGN KEY (bus_id) REFERENCES buses(bus_id)
);

CREATE TABLE reservations (
    reservation_id INT PRIMARY KEY AUTO_INCREMENT,
    customer_name VARCHAR(50),
    route_id INT,
    travel_time TIME,
    seat_number INT,
    FOREIGN KEY (route_id) REFERENCES routes(route_id)
);

INSERT INTO buses (bus_name, capacity) VALUES
('Express 1', 40),
('Express 2', 50),
('Ultra Deluxe', 30),
('Luxury Liner', 20);

INSERT INTO routes (bus_id, origin, destination) VALUES
(1, 'Chennai', 'Villupuram'),
(2, 'Tiruvannamalai', 'Bangalore'),
(3, 'Trichy', 'Coimbatore'),
(4, 'Chennai', 'Kanyakumari');

select * from reservations;

