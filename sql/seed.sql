DELETE FROM admin;
DELETE FROM staff;
DELETE FROM customer;
DELETE FROM home_delivery;
DELETE FROM menu;
DELETE FROM beverage;
DELETE FROM receipt;
DELETE FROM generate_report;
DELETE FROM offers;
DELETE FROM tables;
DELETE FROM payment;
DELETE FROM rating;
DELETE FROM orders;
DELETE FROM booking;

ALTER TABLE admin AUTO_INCREMENT = 1;
ALTER TABLE staff AUTO_INCREMENT = 1;
ALTER TABLE customer AUTO_INCREMENT = 1;
ALTER TABLE home_delivery AUTO_INCREMENT = 1;
ALTER TABLE menu AUTO_INCREMENT = 1;
ALTER TABLE beverage AUTO_INCREMENT = 1;
ALTER TABLE receipt AUTO_INCREMENT = 1;
ALTER TABLE generate_report AUTO_INCREMENT = 1;
ALTER TABLE offers AUTO_INCREMENT = 1;
ALTER TABLE tables AUTO_INCREMENT = 1;
ALTER TABLE payment AUTO_INCREMENT = 1;
ALTER TABLE rating AUTO_INCREMENT = 1;
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

INSERT INTO home_delivery (order_id, address) VALUES
(1, 'Lakeside, Pokhara'),
(3, 'Newroad, Kathmandu'),
(5, 'SrijanaChowk, Pokhara'),
(7, 'Bharha ghare, Chitwan'),
(9, 'Butwal, Rupandehi');

INSERT INTO menu (category, price) VALUES
('SteamMomo',180),
('FriedMomo - Fried', 200),
('Dal Bhat Tarkari',250),
('Chicken Chowmein',220),
('Veg Fried Rice',190);

INSERT INTO beverage (name, category, price) VALUES
('Masala Tea','Hot Drink',  60),
('Black Coffee','Hot Drink',  80),
('Latte','Hot Drink', 120),
('Mango Lassi','Cold Drink', 150),
('Lemon Soda','Cold Drink',90);

INSERT INTO receipt (order_id, total_amount) VALUES
(1,  180),
(2,  190),
(3,  250),
(4,  450),
(5,  280);

INSERT INTO generate_report (order_id, dates) VALUES
(1,'2026-04-10'),
(2,'2026-04-13'),
(3,'2026-04-14'),
(4,'2026-04-14'),
(5,'2026-04-01');

INSERT INTO offers (name, discount) VALUES
('Dashain Special',5),
('Tihar Special',10),
('Winter Special',20),
('Summer Discount',5),
('Teej Special',12);

INSERT INTO tables (capacity, location) VALUES
(2,  'Roof),
(4,  'Outdoor),
(4,  'Roof),
(6,  'Indoor),
(2,  'Outdoor);

INSERT INTO payment (order_id, amount, dates) VALUES
(1,  180, '2026-04-10'),
(2,  190, '2026-04-10'),
(3,  250, '2026-04-11'),
(4,  450, '2026-04-11'),
(5,  280, '2026-04-12');

INSERT INTO rating (customer_id, menu_id) VALUES
(1, 1),
(1, 5),
(2, 3),
(2, 7),
(3, 6);

INSERT INTO orders (customer_id, menu_id, date) VALUES
(1, 1,  '2026-04-10'),
(2, 5,  '2026-04-10'),
(3, 3,  '2026-04-11'),
(4, 7,  '2026-04-11'),
(5, 6,  '2026-04-12');

INSERT INTO booking (customer_id, table_id, date) VALUES
(1, 1, '2026-04-15'),
(2, 2, '2026-04-16'),
(3, 3, '2026-04-17'),
(4, 4, '2026-04-18'),
(5, 5, '2026-04-19');
