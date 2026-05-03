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

INSERT INTO admin (name, email, password) VALUES
('Archana Bhattarai Sharma', 'archanabhattarai@gmail.com', 'archana123'),
('Anisha Gurung', 'anishagurung@gmail.com', 'anisha123'),
('Preeti Kumari Dhwmala', 'preetikumaridhamala@gmail.com', 'preeti123'),
('Aasutosh Malla', 'aasutoshMalla@gmail.com', 'aasutosh123'),
('Unika Aadikari', 'unikaadhikari@gmail.com', 'unika123');

INSERT INTO staff (name, role, phone_number, email, password, status) VALUES
('Bibisha','Manager','9845643889', 'bibisha@gmail.com', 'bibisha123', 'active'),
('Pooja', 'Waiter','9845987657', 'pooja@gmail.com', 'pooja123', 'active'),
('Kristina','Waiter','9834876543', 'kristina@gmail.com', 'kristina123', 'active'),
('Rakshya','Cook','9853487264', 'rakshya@gmail.com', 'rakshya123', 'active'),
('Sristy','Waiter','9836091876', 'sristy@gmail.com', 'sristy123', 'active');

INSERT INTO customer (name, phone_number, email, password, status) VALUES
('Hari KC','9845987657', 'hari@gmail.com', 'hari123', 'pending'),
('Nirajan aadhikari','9845643889', 'nirajan@gmail.com', 'niraijan123', 'pending'),
('Jwola Shrestha','9834876543', 'jwola@gmail.com', 'jwola@gmail.com', 'jwola123', 'pending'),
('Anita Poudel','9836091876', 'anita@gmail.com', 'anita@gmail.com', 'anita123', 'pending'),
('Suikriti Aryal','9836091876','Suikriti@gmail.com', 'suikriti@gmail.com', 'suikriti123', 'pending');

INSERT INTO menu (name, category, price, availability) VALUES
('SteamMomo', 'Japanese', 180.00, 'available'),
('FriedMomo - Fried', 'Indian', 200.00, 'available'),
('Dal Bhat Tarkari', 'Nepali',250.00, 'available'),
('Chicken Chowmein', 'Italian',220.00, 'available'),
('Veg Fried Rice', 'Turkish', 190.00, 'available');

INSERT INTO orders (customer_id, menu_id, dates, quantity, status) VALUES
(1, 1,  '2026-04-10', 1, 'pending'),
(2, 5,  '2026-04-10', 1, 'pending'),
(3, 3,  '2026-04-11', 1, 'pending'),
(4, 3,  '2026-04-11', 1, 'pending'),
(5, 2,  '2026-04-12', 1, 'pending');

INSERT INTO receipt (order_id, total_amount) VALUES
(1,  180.00),
(2,  190.00),
(3,  250.00),
(4,  450.00),
(5,  280.00);

INSERT INTO tables (capacity, location, table_number, status) VALUES
(2,  'Roof', 6, 'available'),
(4,  'Outdoor', 3, 'available'),
(4,  'Roof', 1, 'available'),
(6,  'Indoor', 4, 'available'),
(2,  'Outdoor', 2, 'available');

INSERT INTO payment (order_id, amount, dates, method, status) VALUES
(1,  180.00, '2026-04-10', 'cash', 'unpaid'),
(2,  190.00, '2026-04-10', 'cash', 'unpaid'),
(3,  250.00, '2026-04-11', 'cash', 'unpaid'),
(4,  450.00, '2026-04-11', 'cash', 'unpaid'),
(5,  280.00, '2026-04-12', 'cash', 'unpaid');

INSERT INTO booking (customer_id, table_id, dates, times, status) VALUES
(1, 1, '2026-04-15', '12:30', 'pending'),
(2, 2, '2026-04-16', '7:00', 'pending'),
(3, 3, '2026-04-17', '12:00', 'pending'),
(4, 4, '2026-04-18', '1:50', 'pending'),
(5, 5, '2026-04-19', '5:30', 'pending');
