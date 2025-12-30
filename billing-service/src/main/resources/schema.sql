CREATE TABLE IF NOT EXISTS tariffs (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    monthly_price DECIMAL(19,4) NOT NULL,
    minutes_package INT NOT NULL,
    sms_package INT NOT NULL,
    gb_package DECIMAL(4,1) NOT NULL
);

CREATE TABLE IF NOT EXISTS balances (
    customer_id BIGINT PRIMARY KEY,
    amount DECIMAL(19,4) NOT NULL DEFAULT 0.0,
    last_updated TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS usage_records (
    customer_id BIGINT PRIMARY KEY,
    call_minutes INT NOT NULL,
    sms_count INT NOT NULL,
    internet_count DECIMAL(4,1) NOT NULL,
    usage_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS invoices (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    customer_id BIGINT NOT NULL,
    amount DECIMAL(19,4) NOT NULL,
    issue_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    paid_at TIMESTAMP,
    status VARCHAR(20) NOT NULL DEFAULT 'UNPAID' -- UNPAID, PAID, CANCELLED
);

CREATE INDEX IF NOT EXISTS idx_usage_customer ON usage_records(customer_id);
CREATE INDEX IF NOT EXISTS idx_invoices_customer ON invoices(customer_id);