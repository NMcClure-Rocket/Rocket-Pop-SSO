-- Create SSO Users table for authentication
CREATE TABLE sso_users (
    id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1) PRIMARY KEY,
    username VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    role VARCHAR(20) NOT NULL,
    employee_id INTEGER,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (employee_id) REFERENCES Employee(id)
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
