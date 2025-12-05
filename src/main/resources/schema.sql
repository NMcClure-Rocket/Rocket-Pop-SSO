-- Create SSO Users table for authentication
CREATE TABLE IF NOT EXISTS users (
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

