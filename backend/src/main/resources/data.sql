-- Insert default roles if they don't exist
INSERT INTO roles (role_name) 
SELECT 'ROLE_USER' 
WHERE NOT EXISTS (SELECT 1 FROM roles WHERE role_name = 'ROLE_USER');

INSERT INTO roles (role_name) 
SELECT 'ROLE_ADMIN' 
WHERE NOT EXISTS (SELECT 1 FROM roles WHERE role_name = 'ROLE_ADMIN');

-- Insert admin user if doesn't exist
INSERT INTO users (username, email, password, account_non_locked, account_non_expired, credentials_non_expired, enabled, role_id, created_date, created_by)
SELECT 'admin', 'admin@mhotoys.com', '$2a$10$VZ1VIcdU3EZp2zNnI9L7iehQNHKMdGEGBLdBQ8H8qLh6XJYYvEUGq', true, true, true, true, 
       (SELECT role_id FROM roles WHERE role_name = 'ROLE_ADMIN'), CURRENT_TIMESTAMP, 'SYSTEM'
WHERE NOT EXISTS (SELECT 1 FROM users WHERE username = 'admin');

-- Insert sample products if none exist
INSERT INTO products (name, description, price, age_range, created_at, created_by)
SELECT 'Color Cuddle Bear', 'A soft and cuddly teddy bear perfect for babies and toddlers. Made with safe, non-toxic materials.', 29.99, '0-2 Years', CURRENT_TIMESTAMP, 'SYSTEM'
WHERE NOT EXISTS (SELECT 1 FROM products WHERE name = 'Color Cuddle Bear');

INSERT INTO products (name, description, price, age_range, created_at, created_by)
SELECT 'Rainbow Dino Teether', 'A colorful dinosaur-shaped teething toy to help soothe sore gums during teething.', 15.99, '3-12 Months', CURRENT_TIMESTAMP, 'SYSTEM'
WHERE NOT EXISTS (SELECT 1 FROM products WHERE name = 'Rainbow Dino Teether');

INSERT INTO products (name, description, price, age_range, created_at, created_by)
SELECT 'Rocket Stacker', 'A fun stacking toy that helps develop hand-eye coordination and fine motor skills.', 24.99, '1-3 Years', CURRENT_TIMESTAMP, 'SYSTEM'
WHERE NOT EXISTS (SELECT 1 FROM products WHERE name = 'Rocket Stacker'); 