DELETE FROM customers;

INSERT INTO customers (first_name, last_name, phone_number, email, tariff_id, status)
VALUES ('Іван', 'Іваненко', '+380501112233', 'ivan.ivanenko@gmail.com', 1, 'ACTIVE');

INSERT INTO customers (first_name, last_name, phone_number, email, tariff_id, status)
VALUES ('Марія', 'Петренко', '+380674445566', 'm.petrenko@ukr.net', 2, 'ACTIVE');

INSERT INTO customers (first_name, last_name, phone_number, email, tariff_id, status)
VALUES ('Петро', 'Чорний', '+380637778899', 'petro.black@outlook.com', 1, 'INACTIVE');