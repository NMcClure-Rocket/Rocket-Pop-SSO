DELETE from users;

-- Default Admin User
INSERT INTO users (first_name, last_name, title, department, email, country, city, location, username, password, salt)
SELECT 'Admin', 'User', 'admin', 1, 'admin@rocketpop.sso', 'USA', 'New York', 1, 'admin', 'e7ATEGAxGMnh8gl7XJI/HE8fwNpd8lfEyxLIHxK0rOa69LM//lWFD2GVyMRUOCcuJArQjXQ3CtcOgf4eTODOdA==', 'PSJ/Pmy6xGHD4ahMR/71+Q=='
WHERE NOT EXISTS (SELECT 1 FROM users WHERE username = 'admin');

-- Regular Users
INSERT INTO users (first_name, last_name, title, department, email, country, city, location, username, password, salt)
SELECT 'Timothee', 'Greswell', 'Manager', 2, 'Timothee.Greswell@BuzzwordSolutions.com', 'Japan', 'Tokyo', 1, 'tgreswell', 'H+5ncY8nFMnI0Ypoe0Yaj5vqMl3JqOyEj5QUKNe5stNPvM728u0K4/V8BlZ65xZbdSV5HTIqS4fIUWOrLIXqXQ==', '4mdnF/EwKGtO9MBpBWOjZA=='
WHERE NOT EXISTS (SELECT 1 FROM users WHERE username = 'tgreswell');

INSERT INTO users (first_name, last_name, title, department, email, country, city, location, username, password, salt)
SELECT 'Keslie', 'Rosina', 'Manager', 1, 'Keslie.Rosina@BuzzwordSolutions.com', 'Germany', 'Berlin', 5, 'krosina', 'hauMMD8k35sif71G00HVuhUkwubhhFtvcS0KjJ/C79d8FXdeAuQ++2W+3tXjK5N0PREPuLEaRrha9ia0PXEXMQ==', 'Q9o7YMilyuSO6+2cTh6IaQ=='
WHERE NOT EXISTS (SELECT 1 FROM users WHERE username = 'krosina');

INSERT INTO users (first_name, last_name, title, department, email, country, city, location, username, password, salt)
SELECT 'Gratia', 'Gurnee', 'Manager', 4, 'Gratia.Gurnee@BuzzwordSolutions.com', 'United States', 'Dallas', 3, 'ggurnee', 'IqPUUjrFkAxZpoMAM9tS5Xca5yRo8LgruWLU2dC2cgAdI1J6BsGv6f+LGOp89NxUVHf3lO14XFzOXwzuKEbv3g==', 'q9BSzysGyITM0PzahKn6PA=='
WHERE NOT EXISTS (SELECT 1 FROM users WHERE username = 'ggurnee');

INSERT INTO users (first_name, last_name, title, department, email, country, city, location, username, password, salt)
SELECT 'Joleen', 'Fishbie', 'Manager', 3, 'Joleen.Fishbie@BuzzwordSolutions.com', 'Japan', 'Tokyo', 1, 'jfishbie', 'cNHbeohhAJ8+s5i7cKNE1arf09ViAlty+NFyu7SuYPYM82ei7VI2cZrnaLqlWh9bCbuucO4B9OyRmHbm9dr36A==', 'PtruyuhQdwe0DDR3qgJoTQ=='
WHERE NOT EXISTS (SELECT 1 FROM users WHERE username = 'jfishbie');

INSERT INTO users (first_name, last_name, title, department, email, country, city, location, username, password, salt)
SELECT 'Lauren', 'Chambers', 'Developer', 2, 'Lauren.Chambers@BuzzwordSolutions.com', 'Brazil', 'SÃ£o Paulo', 2, 'lchambers', 'thGPA9Z9CqDaI9aRcTka66rWV/dzYLpj2X3ZPrT55CaPmTJ6v9BdHK3UeOu+YcGv1jJGVAaYWS52zmPxMrtiRg==', 'oiUMOR9MyWKQHZHBc32rhw=='
WHERE NOT EXISTS (SELECT 1 FROM users WHERE username = 'lchambers');

INSERT INTO users (first_name, last_name, title, department, email, country, city, location, username, password, salt)
SELECT 'Malorie', 'Di Carli', 'Developer', 2, 'Malorie.Di Carli@BuzzwordSolutions.com', 'Japan', 'Tokyo', 1, 'mdicarli', 'ay93l7yissWGF2JpX8ndbK8v6vaIxvH/XC1t5j0L9j6ytIueesR1LTl53CNiAqxJx+NAhkUYMrTywxO49Q+sjA==', 'nKbOCea/ZCKk6DVgdw5owg=='
WHERE NOT EXISTS (SELECT 1 FROM users WHERE username = 'mdicarli');

INSERT INTO users (first_name, last_name, title, department, email, country, city, location, username, password, salt)
SELECT 'Ara', 'Josipovitz', 'Developer', 2, 'Ara.Josipovitz@BuzzwordSolutions.com', 'United States', 'Dallas', 3, 'ajosipovitz', 'GmLVLvH0o+SWI5bWPHwm5mWU9rNZZFeZPxUtbO5fPHARivPJmLiS6VOV+H5WxErutff4x0QDNyMA5YZnoKL0Iw==', 'nU/mLNTz8aOkbhzS7yTbcQ=='
WHERE NOT EXISTS (SELECT 1 FROM users WHERE username = 'ajosipovitz');

INSERT INTO users (first_name, last_name, title, department, email, country, city, location, username, password, salt)
SELECT 'Thain', 'Avramchik', 'Developer', 2, 'Thain.Avramchik@BuzzwordSolutions.com', 'United States', 'Dallas', 3, 'tavramchik', 'K50ajEOvImmiLHuKeN/+A5H3Jr+WG6xJ3q9kmEr7gpGBhusJwShiDZq5+zHXQ/NZxx53ijVSo+R2Xg2PWW3hpg==', 'gTZHF980MgLIyZo7oCdKpA=='
WHERE NOT EXISTS (SELECT 1 FROM users WHERE username = 'tavramchik');

INSERT INTO users (first_name, last_name, title, department, email, country, city, location, username, password, salt)
SELECT 'Ursa', 'Fabri', 'Developer', 2, 'Ursa.Fabri@BuzzwordSolutions.com', 'United States', 'Dallas', 3, 'ufabri', 'QcnYF6GRLLlGnqWUfxAbFyd7UGCRlDrwS/hFRQJ1l+TGg+lxF4BY0ca1Dm96RFxflS7Kww7uY6krAAnOsqoGyg==', 't1hFIuF9eS1sPo/HXM3TzA=='
WHERE NOT EXISTS (SELECT 1 FROM users WHERE username = 'ufabri');
