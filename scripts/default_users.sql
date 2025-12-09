-- Default Admin User
INSERT INTO users (first_name, last_name, title, department, email, country, city, location, username, password, salt)
SELECT 'Admin', 'User', 'admin', 1, 'admin@rocketpop.sso', 'USA', 'New York', 1, 'admin', 'admin123', 'salt_admin'
WHERE NOT EXISTS (SELECT 1 FROM users WHERE username = 'admin');

-- Regular Users
INSERT INTO users (first_name, last_name, title, department, email, country, city, location, username, password, salt)
SELECT 'John', 'Doe', 'Mr.', 1, 'john.doe@example.com', 'USA', 'New York', 1, 'john', 'password', 'salt'
WHERE NOT EXISTS (SELECT 1 FROM users WHERE username = 'john');

INSERT INTO users (first_name, last_name, title, department, email, country, city, location, username, password, salt)
SELECT 'Jane', 'Smith', 'Ms.', 2, 'jane.smith@example.com', 'USA', 'Los Angeles', 2, 'jane', 'password2', 'salt2'
WHERE NOT EXISTS (SELECT 1 FROM users WHERE username = 'jane');
