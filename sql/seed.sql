USE restaurant;

DELETE FROM admin;
DELETE FROM staff;
DELETE FROM customer;
DELETE FROM menu;
DELETE FROM receipt;
DELETE FROM tables;
DELETE FROM payment;
DELETE FROM orders;
DELETE FROM booking;

ALTER TABLE admin AUTO_INCREMENT = 1;
ALTER TABLE staff AUTO_INCREMENT = 1;
ALTER TABLE customer AUTO_INCREMENT = 1;
ALTER TABLE menu AUTO_INCREMENT = 1;
ALTER TABLE receipt AUTO_INCREMENT = 1;
ALTER TABLE tables AUTO_INCREMENT = 1;
ALTER TABLE payment AUTO_INCREMENT = 1;
ALTER TABLE orders AUTO_INCREMENT = 1;
ALTER TABLE booking AUTO_INCREMENT = 1;

INSERT INTO admin (name) VALUES
('Archana Bhattarai Sharma'),
('Anisha Gurung'),
('Preeti Kumari Dhwmala'),
('Aasutosh Malla'),
('Unika Aadikari');

INSERT INTO staff (name, role, phone_number) VALUES
('Bibisha','Manager','9845643889'),
('Pooja', 'Waiter','9845987657'),
('Kristina','Waiter','9834876543'),
('Rakshya','Cook','9853487264'),
('Sristy','Waiter','9836091876');

INSERT INTO customer (name, phone_number, email) VALUES
('Hari KC','9845987657', 'hari@gmail.com'),
('Nirajan aadhikari','9845643889', 'nirajan@gmail.com'),
('Jwola Shrestha','9834876543', 'jwola@gmail.com'),
('Anita Poudel','9836091876', 'anita@gmail.com'),
('Suikriti Aryal','9836091876','Suikriti@gmail.com');

INSERT INTO menu (category, price) VALUES
('SteamMomo',180),
('FriedMomo - Fried', 200),
('Dal Bhat Tarkari',250),
('Chicken Chowmein',220),
('Veg Fried Rice',190);

INSERT INTO orders (customer_id, menu_id, dates) VALUES
(1, 1,  '2026-04-10'),
(2, 5,  '2026-04-10'),
(3, 3,  '2026-04-11'),
(4, 3,  '2026-04-11'),
(5, 2,  '2026-04-12');

INSERT INTO receipt (order_id, total_amount) VALUES
(1,  180),
(2,  190),
(3,  250),
(4,  450),
(5,  280);

INSERT INTO tables (capacity, location) VALUES
(2,  'Roof'),
(4,  'Outdoor'),
(4,  'Roof'),
(6,  'Indoor'),
(2,  'Outdoor');

INSERT INTO payment (order_id, amount, dates) VALUES
(1,  180, '2026-04-10'),
(2,  190, '2026-04-10'),
(3,  250, '2026-04-11'),
(4,  450, '2026-04-11'),
(5,  280, '2026-04-12');

INSERT INTO booking (customer_id, table_id, dates) VALUES
(1, 1, '2026-04-15'),
(2, 2, '2026-04-16'),
(3, 3, '2026-04-17'),
(4, 4, '2026-04-18'),
(5, 5, '2026-04-19');
