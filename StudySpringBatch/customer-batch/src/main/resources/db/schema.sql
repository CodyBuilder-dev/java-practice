-- Customer Batch Database Schema

-- Customers Table
CREATE TABLE IF NOT EXISTS customers (
    id BINARY(16) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    status VARCHAR(20) NOT NULL,
    tier VARCHAR(20) NOT NULL,
    last_login_at DATETIME(6),
    created_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    INDEX idx_status (status),
    INDEX idx_tier (tier),
    INDEX idx_last_login_at (last_login_at),
    INDEX idx_created_at (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Marketing Consents Table
CREATE TABLE IF NOT EXISTS marketing_consents (
    id BINARY(16) PRIMARY KEY,
    customer_id BINARY(16) NOT NULL,
    channel VARCHAR(20) NOT NULL,
    consented BOOLEAN NOT NULL DEFAULT FALSE,
    consented_at DATETIME(6),
    expires_at DATETIME(6),
    created_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    FOREIGN KEY (customer_id) REFERENCES customers(id) ON DELETE CASCADE,
    INDEX idx_customer_id (customer_id),
    INDEX idx_channel (channel),
    INDEX idx_expires_at (expires_at),
    UNIQUE KEY uk_customer_channel (customer_id, channel)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Customer Orders Table (for VIP tier calculation)
CREATE TABLE IF NOT EXISTS customer_orders (
    id BINARY(16) PRIMARY KEY,
    customer_id BINARY(16) NOT NULL,
    order_amount DECIMAL(15, 2) NOT NULL,
    order_date DATETIME(6) NOT NULL,
    status VARCHAR(20) NOT NULL,
    created_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    FOREIGN KEY (customer_id) REFERENCES customers(id) ON DELETE CASCADE,
    INDEX idx_customer_id (customer_id),
    INDEX idx_order_date (order_date),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Batch Job Execution Tables (Spring Batch Meta Tables - will be created by Spring Batch)
-- These are created automatically by Spring Batch when initialize-schema is set to 'always'
