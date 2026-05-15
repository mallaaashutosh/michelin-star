USE restaurant;

DELETE FROM payment;
DELETE FROM orders;
DELETE FROM cart;
DELETE FROM menu;
DELETE FROM customer;
DELETE FROM admin;

ALTER TABLE payment AUTO_INCREMENT = 1;
ALTER TABLE orders AUTO_INCREMENT = 1;
ALTER TABLE cart AUTO_INCREMENT = 1;
ALTER TABLE menu AUTO_INCREMENT = 1;
ALTER TABLE customer AUTO_INCREMENT = 1;
ALTER TABLE admin AUTO_INCREMENT = 1;


INSERT INTO admin (name, email, password) VALUES
('Archana Bhattarai Sharma', 'archanabhattarai@gmail.com', 'archana123'),
('Anisha Gurung', 'anishagurung@gmail.com', 'anisha123'),
('Preeti Kumari Dhamala', 'preetikumaridhamala@gmail.com', 'preeti123'),
('Aasutosh Malla', 'aasutoshmalla@gmail.com', 'aasutosh123'),
('Unika Adhikari', 'unikaadhikari@gmail.com', 'unika123');


INSERT INTO customer (name, phone_number, email, password, status) VALUES
('Hari KC', '9845987657', 'hari@gmail.com', 'hari123', 'approved'),
('Nirajan Adhikari', '9845643889', 'nirajan@gmail.com', 'nirajan123', 'approved'),
('Jwala Shrestha', '9834876543', 'jwala@gmail.com', 'jwala123', 'approved'),
('Anita Poudel', '9836091876', 'anita@gmail.com', 'anita123', 'pending'),
('Suikriti Aryal', '9836591876', 'suikriti@gmail.com', 'suikriti123', 'pending');


INSERT INTO menu (name, category, price, image, availability) VALUES
('Steam Momo', 'Nepali', 180.00, 'steam-momo.jpg', 'available'),
('Fried Momo', 'Nepali', 200.00, 'fried-momo.jpg', 'available'),
('Dal Bhat Tarkari', 'Nepali', 250.00, 'dal-bhat.jpg', 'available'),
('Chicken Chowmein', 'Chinese', 220.00, 'chowmein.jpg', 'available'),
('Veg Fried Rice', 'Chinese', 190.00, 'fried-rice.jpg', 'available'),
('Butter Chicken', 'Indian', 320.00, 'butter-chicken.jpg', 'available'),
('Chicken Burger', 'FastFood', 250.00, 'chicken-burger.jpg', 'available'),
('Margherita Pizza', 'Italian', 450.00, 'pizza.jpg', 'available'),
('Masala Tea', 'Beverages', 60.00, 'masala-tea.jpg', 'available'),
('Fresh Lime Soda', 'Beverages', 80.00, 'lime-soda.jpg', 'available');


INSERT INTO orders (customer_id, menu_id, menu_name, quantity, price, total_amount, table_number, payment_method, status) VALUES
(1, 1, 'Steam Momo',      2, 180.00, 360.00, 3, 'cash', 'pending'),
(1, 9, 'Masala Tea',      2,  60.00, 120.00, 3, 'cash', 'pending'),   -- FIX: total_amount 360.00 → 120.00 (2 × 60.00)
(2, 4, 'Chicken Chowmein',1, 220.00, 220.00, 1, 'card', 'completed'),
(3, 3, 'Dal Bhat Tarkari',1, 250.00, 250.00, 5, 'cash', 'completed'),
(3, 10,'Fresh Lime Soda', 1,  80.00,  80.00, 5, 'cash', 'completed'); -- FIX: total_amount 250.00 → 80.00 (1 × 80.00)


INSERT INTO payment (customer_id, total_amount, method, status) VALUES
(1, 480.00, 'cash', 'paid'),
(2, 220.00, 'card', 'paid'),
(3, 330.00, 'cash', 'paid');