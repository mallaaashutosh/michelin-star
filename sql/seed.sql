-- Sample data for local development and demos.
-- Clears existing rows, resets auto-increment counters, then loads
-- test users, a full menu, sample orders, and matching payment records.
-- Also includes image path updates for menu items.
-- Run after schema.sql on a database that already has the table structure.

-- Work inside the restaurant database
USE restaurant;

-- Empty all tables so seed data starts from a clean slate
DELETE FROM payment;
DELETE FROM orders;
DELETE FROM cart;
DELETE FROM menu;
DELETE FROM user;

-- Reset ID counters so new rows start at 1 again
ALTER TABLE payment AUTO_INCREMENT = 1;
ALTER TABLE orders AUTO_INCREMENT = 1;
ALTER TABLE cart AUTO_INCREMENT = 1;
ALTER TABLE menu AUTO_INCREMENT = 1;
ALTER TABLE user AUTO_INCREMENT = 1;

-- Team admins plus sample customers (some pending approval for testing)
INSERT INTO user (name, email, phone_number, password, role, status) VALUES
('Archana Bhattarai Sharma', 'archana@michelinstar.com', '9801000001', 'archana123', 'admin', 'active'),
('Anisha Gurung', 'anisha@michelinstar.com', '9801000002', 'anisha123', 'admin', 'active'),
('Preeti Kumari Dhamala', 'preeti@michelinstar.com', '9801000003', 'preeti123', 'admin', 'active'),
('Aashutosh Malla', 'aashutosh@michelinstar.com', '9801000004', 'aashutosh123', 'admin', 'active'),
('Unika Adhikari', 'unika@michelinstar.com', '9801000005', 'unika123', 'admin', 'active'),
('Hari KC', 'hari@gmail.com', '9845987657', 'hari123', 'customer', 'active'),
('Nirajan Adhikari', 'nirajan@gmail.com', '9845643889', 'nirajan123', 'customer', 'active'),
('Jwala Shrestha', 'jwala@gmail.com', '9834876543', 'jwala123', 'customer', 'active'),
('Anita Poudel', 'anita@gmail.com', '9836091876', 'anita123', 'customer', 'pending'),
('Suikriti Aryal', 'suikriti@gmail.com', '9836591876', 'suikriti123', 'customer', 'pending');

-- Full menu across Nepali, Chinese, Indian, Italian, fast food, and beverages
INSERT INTO menu (name, category, price, image, availability) VALUES
-- Nepali dishes
('Steam Momo', 'Nepali', 180.00, 'steam-momo.jpg', 'available'),
('Fried Momo', 'Nepali', 200.00, 'fried-momo.jpg', 'available'),
('Dal Bhat Tarkari', 'Nepali', 250.00, 'dal-bhat.jpg', 'available'),
('Chicken Sekuwa', 'Nepali', 280.00, 'chicken-sekuwa.jpg', 'available'),
('Thukpa', 'Nepali', 200.00, 'thukpa.jpg', 'available'),
('Jhol Momo', 'Nepali', 220.00, 'jhol-momo.jpg', 'available'),
('Gundruk Aloo Tama', 'Nepali', 240.00, 'gundruk-aloo-tama.jpg', 'available'),
('Chicken Chili', 'Nepali', 260.00, 'chicken-chili.jpg', 'available'),

-- Chinese dishes
('Chicken Chowmein', 'Chinese', 220.00, 'chowmein.jpg', 'available'),
('Veg Fried Rice', 'Chinese', 190.00, 'fried-rice.jpg', 'available'),
('Sweet and Sour Chicken', 'Chinese', 280.00, 'sweet-and-sour-chicken.jpg', 'available'),
('Spring Rolls', 'Chinese', 150.00, 'spring-rolls.jpg', 'available'),
('Kung Pao Chicken', 'Chinese', 300.00, 'kung-pao-chicken.jpg', 'available'),
('Hot and Sour Soup', 'Chinese', 140.00, 'hot-and-sour-soup.jpg', 'available'),
('Dim Sum Platter', 'Chinese', 320.00, 'dim-sum-platter.jpg', 'available'),

-- Indian dishes
('Butter Chicken', 'Indian', 320.00, 'butter-chicken.jpg', 'available'),
('Chicken Biryani', 'Indian', 300.00, 'chicken-biryani.jpg', 'available'),
('Garlic Naan', 'Indian', 80.00, 'garlic-naan.jpg', 'available'),
('Paneer Tikka', 'Indian', 280.00, 'paneer-tikka.jpg', 'available'),
('Dal Makhani', 'Indian', 260.00, 'dal-makhani.jpg', 'available'),

-- Italian dishes
('Margherita Pizza', 'Italian', 450.00, 'pizza.jpg', 'available'),
('Truffle Pasta', 'Italian', 420.00, 'truffle-pasta.png', 'available'),
('Spaghetti Carbonara', 'Italian', 380.00, 'spaghetti-carbonara.jpg', 'available'),
('Penne Arrabbiata', 'Italian', 350.00, 'penne-arrabbiata.jpg', 'available'),
('Lasagna', 'Italian', 400.00, 'lasagna.jpg', 'available'),

-- Fast food
('Chicken Burger', 'FastFood', 250.00, 'chicken-burger.jpg', 'available'),
('French Fries', 'FastFood', 120.00, 'french-fries.jpg', 'available'),
('Veggie Wrap', 'FastFood', 180.00, 'veggie-wrap.jpg', 'available'),
('Cheese Burger', 'FastFood', 280.00, 'cheese-burger.jpg', 'available'),
('Chicken Wings', 'FastFood', 320.00, 'chicken-wings.jpg', 'available'),

-- Beverages
('Masala Tea', 'Beverages', 60.00, 'masala-tea.jpg', 'available'),
('Fresh Lime Soda', 'Beverages', 80.00, 'lime-soda.jpg', 'available'),
('Mango Lassi', 'Beverages', 120.00, 'mango-lassi.jpg', 'available'),
('Cold Coffee', 'Beverages', 100.00, 'cold-coffee.jpg', 'available'),
('Iced Tea', 'Beverages', 90.00, 'iced-tea.jpg', 'available'),
('Fresh Orange Juice', 'Beverages', 110.00, 'fresh-orange-juice.jpg', 'available'),
('Hot Chocolate', 'Beverages', 130.00, 'hot-chocolate.jpg', 'available');
