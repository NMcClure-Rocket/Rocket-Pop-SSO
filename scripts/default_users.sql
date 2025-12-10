-- Default Admin User
INSERT INTO users (first_name, last_name, title, department, email, country, city, location, username, password, salt)
SELECT 'Admin', 'User', 'admin', 1, 'admin@rocketpop.sso', 'USA', 'New York', 1, 'admin', '[B@3d646c37', '[B@41cf53f9'
WHERE NOT EXISTS (SELECT 1 FROM users WHERE username = 'admin');

-- Regular Users
INSERT INTO users (first_name, last_name, title, department, email, country, city, location, username, password, salt)
SELECT 'Timothee', 'Greswell', 'Manager', 2, 'Timothee.Greswell@BuzzwordSolutions.com', 'Japan', 'Tokyo', 1, 'tgreswell', '[B@17f052a3', '[B@2e0fa5d3'
WHERE NOT EXISTS (SELECT 1 FROM users WHERE username = 'tgreswell');

INSERT INTO users (first_name, last_name, title, department, email, country, city, location, username, password, salt)
SELECT 'Keslie', 'Rosina', 'Manager', 1, 'Keslie.Rosina@BuzzwordSolutions.com', 'Germany', 'Berlin', 5, 'krosina', '[B@5010be6', '[B@685f4c2e'
WHERE NOT EXISTS (SELECT 1 FROM users WHERE username = 'krosina');

INSERT INTO users (first_name, last_name, title, department, email, country, city, location, username, password, salt)
SELECT 'Gratia', 'Gurnee', 'Manager', 4, 'Gratia.Gurnee@BuzzwordSolutions.com', 'United States', 'Dallas', 3, 'ggurnee', '[B@7daf6ecc', '[B@2e5d6d97'
WHERE NOT EXISTS (SELECT 1 FROM users WHERE username = 'ggurnee');

INSERT INTO users (first_name, last_name, title, department, email, country, city, location, username, password, salt)
SELECT 'Joleen', 'Fishbie', 'Manager', 3, 'Joleen.Fishbie@BuzzwordSolutions.com', 'Japan', 'Tokyo', 1, 'jfishbie', '[B@238e0d81', '[B@31221be2'
WHERE NOT EXISTS (SELECT 1 FROM users WHERE username = 'jfishbie');

INSERT INTO users (first_name, last_name, title, department, email, country, city, location, username, password, salt)
SELECT 'Lauren', 'Chambers', 'Developer', 2, 'Lauren.Chambers@BuzzwordSolutions.com', 'Brazil', 'SÃ£o Paulo', 2, 'lchambers', '[B@377dca04', '[B@728938a9'
WHERE NOT EXISTS (SELECT 1 FROM users WHERE username = 'lchambers');

INSERT INTO users (first_name, last_name, title, department, email, country, city, location, username, password, salt)
SELECT 'Malorie', 'Di Carli', 'Developer', 2, 'Malorie.Di Carli@BuzzwordSolutions.com', 'Japan', 'Tokyo', 1, 'mdicarli', '[B@21b8d17c', '[B@6433a2'
WHERE NOT EXISTS (SELECT 1 FROM users WHERE username = 'mdicarli');

INSERT INTO users (first_name, last_name, title, department, email, country, city, location, username, password, salt)
SELECT 'Ara', 'Josipovitz', 'Developer', 2, 'Ara.Josipovitz@BuzzwordSolutions.com', 'United States', 'Dallas', 3, 'ajosipovitz', '[B@5910e440', '[B@6267c3bb'
WHERE NOT EXISTS (SELECT 1 FROM users WHERE username = 'ajosipovitz');

INSERT INTO users (first_name, last_name, title, department, email, country, city, location, username, password, salt)
SELECT 'Thain', 'Avramchik', 'Developer', 2, 'Thain.Avramchik@BuzzwordSolutions.com', 'United States', 'Dallas', 3, 'tavramchik', '[B@533ddba', '[B@246b179d'
WHERE NOT EXISTS (SELECT 1 FROM users WHERE username = 'tavramchik');

INSERT INTO users (first_name, last_name, title, department, email, country, city, location, username, password, salt)
SELECT 'Ursa', 'Fabri', 'Developer', 2, 'Ursa.Fabri@BuzzwordSolutions.com', 'United States', 'Dallas', 3, 'ufabri', '[B@7a07c5b4', '[B@26a1ab54'
WHERE NOT EXISTS (SELECT 1 FROM users WHERE username = 'ufabri');