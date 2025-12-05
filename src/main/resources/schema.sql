-- Create SSO Users table for authentication
CREATE TABLE IF NOT EXISTS sso_users (
    id INT AUTO_INCREMENT Primary Key,
	first_name VARCHAR(50),
	last_name VARCHAR(50),
	title VARCHAR(11),
	department int,
	email VARCHAR(50),
	country VARCHAR(50),
	city VARCHAR(50),
	location INT,
    username VARCHAR(100) NOT NULL,
    password VARCHAR(256) NOT NULL,
    salt VARCHAR(128) NOT NULL
);

-- Create index on username for faster lookups
CREATE INDEX idx_sso_users_username ON sso_users(username);
CREATE INDEX idx_sso_users_email ON sso_users(email);
CREATE INDEX idx_sso_users_role ON sso_users(role);

-- Insert sample admin user (password is 'admin123' - hashed with BCrypt)
-- You should change this password in production!
INSERT INTO sso_users (username, password, email, role) 
VALUES ('admin', '$2a$10$xQvGHjWfEjKZMJLKX7yx1.R5OkYzF7H7KV5U5rQz8qVxQ5d4Y5X5u', 'admin@rocketpop.com', 'admin');

-- Insert sample regular user (password is 'user123' - hashed with BCrypt)
INSERT INTO sso_users (username, password, email, role, employee_id) 
VALUES ('testuser', '$2a$10$dQvGHjWfEjKZMJLKX7yx1.R5OkYzF7H7KV5U5rQz8qVxQ5d4Y5X5v', 'testuser@rocketpop.com', 'user', 1);

-- Insert sample manager user (password is 'manager123' - hashed with BCrypt)
INSERT INTO sso_users (username, password, email, role, employee_id) 
VALUES ('manager1', '$2a$10$eQvGHjWfEjKZMJLKX7yx1.R5OkYzF7H7KV5U5rQz8qVxQ5d4Y5X5w', 'manager@rocketpop.com', 'manager', 31);
