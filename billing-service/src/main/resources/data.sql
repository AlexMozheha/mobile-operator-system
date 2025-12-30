DELETE FROM usage_records;
DELETE FROM balances;
DELETE FROM invoices;
DELETE FROM tariffs;

INSERT INTO tariffs (name, monthly_price, minutes_package, sms_package, gb_package)
VALUES ('Базовий', 150.00, 300, 100, 10.0);

INSERT INTO tariffs (name, monthly_price, minutes_package, sms_package, gb_package)
VALUES ('Безлім+', 350.00, 1000, 500, 50.0);

INSERT INTO tariffs (name, monthly_price, minutes_package, sms_package, gb_package)
VALUES ('Соціальний', 75.00, 100, 50, 2.5);

INSERT INTO balances (customer_id, amount, last_updated) VALUES (1, 1000.00, CURRENT_TIMESTAMP);
INSERT INTO balances (customer_id, amount, last_updated) VALUES (2, 1000.50, CURRENT_TIMESTAMP);
INSERT INTO balances (customer_id, amount, last_updated) VALUES (3, 10000.00, CURRENT_TIMESTAMP);

-- Тільки один рядок для клієнта №1 (сумуємо дані або залишаємо актуальні)
INSERT INTO usage_records (customer_id, call_minutes, sms_count, internet_count, usage_date)
VALUES (1, 15, 2, 1.7, CURRENT_TIMESTAMP);

-- Один рядок для клієнта №2
INSERT INTO usage_records (customer_id, call_minutes, sms_count, internet_count, usage_date)
VALUES (2, 45, 10, 5.0, CURRENT_TIMESTAMP);

-- Один рядок для клієнта №3
INSERT INTO usage_records (customer_id, call_minutes, sms_count, internet_count, usage_date)
VALUES (3, 2, 1, 0.1, CURRENT_TIMESTAMP);

INSERT INTO invoices (customer_id, amount, issue_date, status)
VALUES (3, 150.00, CURRENT_TIMESTAMP, 'UNPAID');

INSERT INTO invoices (customer_id, amount, issue_date, status)
VALUES (1, 150.00, CURRENT_TIMESTAMP, 'UNPAID');

INSERT INTO invoices (customer_id, amount, issue_date, status)
VALUES (2, 150.00, CURRENT_TIMESTAMP, 'UNPAID');