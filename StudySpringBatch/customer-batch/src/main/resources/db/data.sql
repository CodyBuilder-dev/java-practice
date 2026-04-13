-- Sample Data for Customer Batch

-- Insert Customers (50 records with various statuses and tiers)
-- Active customers with recent logins
INSERT INTO customers (id, name, email, status, tier, last_login_at, created_at, updated_at) VALUES
(UNHEX(REPLACE(UUID(), '-', '')), 'Kim Min-ji', 'kim.minji@example.com', 'ACTIVE', 'VIP', DATE_SUB(NOW(), INTERVAL 1 DAY), DATE_SUB(NOW(), INTERVAL 365 DAY), NOW()),
(UNHEX(REPLACE(UUID(), '-', '')), 'Lee Seo-jun', 'lee.seojun@example.com', 'ACTIVE', 'VIP', DATE_SUB(NOW(), INTERVAL 2 DAY), DATE_SUB(NOW(), INTERVAL 400 DAY), NOW()),
(UNHEX(REPLACE(UUID(), '-', '')), 'Park Ji-woo', 'park.jiwoo@example.com', 'ACTIVE', 'GOLD', DATE_SUB(NOW(), INTERVAL 3 DAY), DATE_SUB(NOW(), INTERVAL 300 DAY), NOW()),
(UNHEX(REPLACE(UUID(), '-', '')), 'Choi Ye-jin', 'choi.yejin@example.com', 'ACTIVE', 'GOLD', DATE_SUB(NOW(), INTERVAL 5 DAY), DATE_SUB(NOW(), INTERVAL 250 DAY), NOW()),
(UNHEX(REPLACE(UUID(), '-', '')), 'Jung Do-hyun', 'jung.dohyun@example.com', 'ACTIVE', 'SILVER', DATE_SUB(NOW(), INTERVAL 7 DAY), DATE_SUB(NOW(), INTERVAL 200 DAY), NOW()),
(UNHEX(REPLACE(UUID(), '-', '')), 'Kang Ha-neul', 'kang.haneul@example.com', 'ACTIVE', 'SILVER', DATE_SUB(NOW(), INTERVAL 10 DAY), DATE_SUB(NOW(), INTERVAL 180 DAY), NOW()),
(UNHEX(REPLACE(UUID(), '-', '')), 'Yoon Soo-bin', 'yoon.soobin@example.com', 'ACTIVE', 'BRONZE', DATE_SUB(NOW(), INTERVAL 15 DAY), DATE_SUB(NOW(), INTERVAL 150 DAY), NOW()),
(UNHEX(REPLACE(UUID(), '-', '')), 'Lim Jae-hyun', 'lim.jaehyun@example.com', 'ACTIVE', 'BRONZE', DATE_SUB(NOW(), INTERVAL 20 DAY), DATE_SUB(NOW(), INTERVAL 120 DAY), NOW()),
(UNHEX(REPLACE(UUID(), '-', '')), 'Han Yu-jin', 'han.yujin@example.com', 'ACTIVE', 'BRONZE', DATE_SUB(NOW(), INTERVAL 25 DAY), DATE_SUB(NOW(), INTERVAL 100 DAY), NOW()),
(UNHEX(REPLACE(UUID(), '-', '')), 'Song Eun-chae', 'song.eunchae@example.com', 'ACTIVE', 'BRONZE', DATE_SUB(NOW(), INTERVAL 28 DAY), DATE_SUB(NOW(), INTERVAL 90 DAY), NOW()),

-- Customers approaching dormant status (last login 85-89 days ago)
(UNHEX(REPLACE(UUID(), '-', '')), 'Oh Min-seok', 'oh.minseok@example.com', 'ACTIVE', 'SILVER', DATE_SUB(NOW(), INTERVAL 85 DAY), DATE_SUB(NOW(), INTERVAL 200 DAY), NOW()),
(UNHEX(REPLACE(UUID(), '-', '')), 'Shin Da-eun', 'shin.daeun@example.com', 'ACTIVE', 'BRONZE', DATE_SUB(NOW(), INTERVAL 86 DAY), DATE_SUB(NOW(), INTERVAL 180 DAY), NOW()),
(UNHEX(REPLACE(UUID(), '-', '')), 'Baek Joon-ho', 'baek.joonho@example.com', 'ACTIVE', 'BRONZE', DATE_SUB(NOW(), INTERVAL 87 DAY), DATE_SUB(NOW(), INTERVAL 170 DAY), NOW()),
(UNHEX(REPLACE(UUID(), '-', '')), 'Kwon Na-yeon', 'kwon.nayeon@example.com', 'ACTIVE', 'GOLD', DATE_SUB(NOW(), INTERVAL 88 DAY), DATE_SUB(NOW(), INTERVAL 250 DAY), NOW()),
(UNHEX(REPLACE(UUID(), '-', '')), 'Ahn Tae-yang', 'ahn.taeyang@example.com', 'ACTIVE', 'BRONZE', DATE_SUB(NOW(), INTERVAL 89 DAY), DATE_SUB(NOW(), INTERVAL 150 DAY), NOW()),

-- Dormant customers (last login 90+ days ago)
(UNHEX(REPLACE(UUID(), '-', '')), 'Hong Gil-dong', 'hong.gildong@example.com', 'ACTIVE', 'BRONZE', DATE_SUB(NOW(), INTERVAL 91 DAY), DATE_SUB(NOW(), INTERVAL 365 DAY), NOW()),
(UNHEX(REPLACE(UUID(), '-', '')), 'Seo Hyun-woo', 'seo.hyunwoo@example.com', 'ACTIVE', 'SILVER', DATE_SUB(NOW(), INTERVAL 100 DAY), DATE_SUB(NOW(), INTERVAL 400 DAY), NOW()),
(UNHEX(REPLACE(UUID(), '-', '')), 'Moon Chae-won', 'moon.chaewon@example.com', 'ACTIVE', 'BRONZE', DATE_SUB(NOW(), INTERVAL 120 DAY), DATE_SUB(NOW(), INTERVAL 380 DAY), NOW()),
(UNHEX(REPLACE(UUID(), '-', '')), 'Go Ara', 'go.ara@example.com', 'ACTIVE', 'GOLD', DATE_SUB(NOW(), INTERVAL 150 DAY), DATE_SUB(NOW(), INTERVAL 450 DAY), NOW()),
(UNHEX(REPLACE(UUID(), '-', '')), 'Nam Joo-hyuk', 'nam.joohyuk@example.com', 'ACTIVE', 'BRONZE', DATE_SUB(NOW(), INTERVAL 180 DAY), DATE_SUB(NOW(), INTERVAL 500 DAY), NOW()),
(UNHEX(REPLACE(UUID(), '-', '')), 'Lee Sung-kyung', 'lee.sungkyung@example.com', 'ACTIVE', 'SILVER', DATE_SUB(NOW(), INTERVAL 200 DAY), DATE_SUB(NOW(), INTERVAL 550 DAY), NOW()),
(UNHEX(REPLACE(UUID(), '-', '')), 'Kim Woo-bin', 'kim.woobin@example.com', 'ACTIVE', 'BRONZE', DATE_SUB(NOW(), INTERVAL 250 DAY), DATE_SUB(NOW(), INTERVAL 600 DAY), NOW()),
(UNHEX(REPLACE(UUID(), '-', '')), 'Bae Suzy', 'bae.suzy@example.com', 'ACTIVE', 'VIP', DATE_SUB(NOW(), INTERVAL 95 DAY), DATE_SUB(NOW(), INTERVAL 700 DAY), NOW()),
(UNHEX(REPLACE(UUID(), '-', '')), 'Park Bo-gum', 'park.bogum@example.com', 'ACTIVE', 'GOLD', DATE_SUB(NOW(), INTERVAL 110 DAY), DATE_SUB(NOW(), INTERVAL 450 DAY), NOW()),
(UNHEX(REPLACE(UUID(), '-', '')), 'Song Hye-kyo', 'song.hyekyo@example.com', 'ACTIVE', 'VIP', DATE_SUB(NOW(), INTERVAL 130 DAY), DATE_SUB(NOW(), INTERVAL 800 DAY), NOW()),

-- Already dormant customers
(UNHEX(REPLACE(UUID(), '-', '')), 'Hyun Bin', 'hyun.bin@example.com', 'DORMANT', 'BRONZE', DATE_SUB(NOW(), INTERVAL 300 DAY), DATE_SUB(NOW(), INTERVAL 900 DAY), NOW()),
(UNHEX(REPLACE(UUID(), '-', '')), 'Son Ye-jin', 'son.yejin@example.com', 'DORMANT', 'SILVER', DATE_SUB(NOW(), INTERVAL 350 DAY), DATE_SUB(NOW(), INTERVAL 950 DAY), NOW()),
(UNHEX(REPLACE(UUID(), '-', '')), 'Lee Min-ho', 'lee.minho@example.com', 'DORMANT', 'GOLD', DATE_SUB(NOW(), INTERVAL 400 DAY), DATE_SUB(NOW(), INTERVAL 1000 DAY), NOW()),

-- Customers with high purchase amounts (for VIP tier upgrade testing)
(UNHEX(REPLACE(UUID(), '-', '')), 'Kim Tae-ri', 'kim.taeri@example.com', 'ACTIVE', 'GOLD', DATE_SUB(NOW(), INTERVAL 5 DAY), DATE_SUB(NOW(), INTERVAL 200 DAY), NOW()),
(UNHEX(REPLACE(UUID(), '-', '')), 'Ryu Jun-yeol', 'ryu.junyeol@example.com', 'ACTIVE', 'SILVER', DATE_SUB(NOW(), INTERVAL 3 DAY), DATE_SUB(NOW(), INTERVAL 180 DAY), NOW()),
(UNHEX(REPLACE(UUID(), '-', '')), 'Jung Hae-in', 'jung.haein@example.com', 'ACTIVE', 'BRONZE', DATE_SUB(NOW(), INTERVAL 2 DAY), DATE_SUB(NOW(), INTERVAL 150 DAY), NOW()),

-- Additional test data
(UNHEX(REPLACE(UUID(), '-', '')), 'IU Lee', 'iu.lee@example.com', 'ACTIVE', 'VIP', DATE_SUB(NOW(), INTERVAL 1 DAY), DATE_SUB(NOW(), INTERVAL 500 DAY), NOW()),
(UNHEX(REPLACE(UUID(), '-', '')), 'Yoo Ah-in', 'yoo.ahin@example.com', 'ACTIVE', 'GOLD', DATE_SUB(NOW(), INTERVAL 10 DAY), DATE_SUB(NOW(), INTERVAL 300 DAY), NOW()),
(UNHEX(REPLACE(UUID(), '-', '')), 'Jun Ji-hyun', 'jun.jihyun@example.com', 'ACTIVE', 'VIP', DATE_SUB(NOW(), INTERVAL 4 DAY), DATE_SUB(NOW(), INTERVAL 600 DAY), NOW()),
(UNHEX(REPLACE(UUID(), '-', '')), 'Lee Jong-suk', 'lee.jongsuk@example.com', 'ACTIVE', 'SILVER', DATE_SUB(NOW(), INTERVAL 15 DAY), DATE_SUB(NOW(), INTERVAL 250 DAY), NOW()),
(UNHEX(REPLACE(UUID(), '-', '')), 'Han So-hee', 'han.sohee@example.com', 'ACTIVE', 'BRONZE', DATE_SUB(NOW(), INTERVAL 20 DAY), DATE_SUB(NOW(), INTERVAL 100 DAY), NOW()),
(UNHEX(REPLACE(UUID(), '-', '')), 'Park Seo-joon', 'park.seojoon@example.com', 'ACTIVE', 'GOLD', DATE_SUB(NOW(), INTERVAL 8 DAY), DATE_SUB(NOW(), INTERVAL 280 DAY), NOW()),
(UNHEX(REPLACE(UUID(), '-', '')), 'Kim Go-eun', 'kim.goeun@example.com', 'ACTIVE', 'SILVER', DATE_SUB(NOW(), INTERVAL 12 DAY), DATE_SUB(NOW(), INTERVAL 220 DAY), NOW()),
(UNHEX(REPLACE(UUID(), '-', '')), 'Gong Yoo', 'gong.yoo@example.com', 'ACTIVE', 'VIP', DATE_SUB(NOW(), INTERVAL 2 DAY), DATE_SUB(NOW(), INTERVAL 700 DAY), NOW()),
(UNHEX(REPLACE(UUID(), '-', '')), 'Kim Soo-hyun', 'kim.soohyun@example.com', 'ACTIVE', 'GOLD', DATE_SUB(NOW(), INTERVAL 6 DAY), DATE_SUB(NOW(), INTERVAL 350 DAY), NOW()),
(UNHEX(REPLACE(UUID(), '-', '')), 'Seo Ye-ji', 'seo.yeji@example.com', 'ACTIVE', 'SILVER', DATE_SUB(NOW(), INTERVAL 18 DAY), DATE_SUB(NOW(), INTERVAL 200 DAY), NOW()),
(UNHEX(REPLACE(UUID(), '-', '')), 'Cha Eun-woo', 'cha.eunwoo@example.com', 'ACTIVE', 'BRONZE', DATE_SUB(NOW(), INTERVAL 22 DAY), DATE_SUB(NOW(), INTERVAL 120 DAY), NOW()),
(UNHEX(REPLACE(UUID(), '-', '')), 'Shin Min-a', 'shin.mina@example.com', 'ACTIVE', 'GOLD', DATE_SUB(NOW(), INTERVAL 9 DAY), DATE_SUB(NOW(), INTERVAL 400 DAY), NOW()),
(UNHEX(REPLACE(UUID(), '-', '')), 'Kim Seon-ho', 'kim.seonho@example.com', 'ACTIVE', 'SILVER', DATE_SUB(NOW(), INTERVAL 14 DAY), DATE_SUB(NOW(), INTERVAL 180 DAY), NOW()),
(UNHEX(REPLACE(UUID(), '-', '')), 'Park Min-young', 'park.minyoung@example.com', 'ACTIVE', 'BRONZE', DATE_SUB(NOW(), INTERVAL 25 DAY), DATE_SUB(NOW(), INTERVAL 150 DAY), NOW()),
(UNHEX(REPLACE(UUID(), '-', '')), 'Ji Chang-wook', 'ji.changwook@example.com', 'ACTIVE', 'SILVER', DATE_SUB(NOW(), INTERVAL 11 DAY), DATE_SUB(NOW(), INTERVAL 270 DAY), NOW()),
(UNHEX(REPLACE(UUID(), '-', '')), 'Im Yoon-ah', 'im.yoonah@example.com', 'ACTIVE', 'GOLD', DATE_SUB(NOW(), INTERVAL 7 DAY), DATE_SUB(NOW(), INTERVAL 320 DAY), NOW()),
(UNHEX(REPLACE(UUID(), '-', '')), 'Lee Dong-wook', 'lee.dongwook@example.com', 'ACTIVE', 'VIP', DATE_SUB(NOW(), INTERVAL 3 DAY), DATE_SUB(NOW(), INTERVAL 550 DAY), NOW()),
(UNHEX(REPLACE(UUID(), '-', '')), 'Sulli Choi', 'sulli.choi@example.com', 'ACTIVE', 'BRONZE', DATE_SUB(NOW(), INTERVAL 30 DAY), DATE_SUB(NOW(), INTERVAL 90 DAY), NOW());

-- Insert Marketing Consents (linked to customers)
-- Active consents
INSERT INTO marketing_consents (id, customer_id, channel, consented, consented_at, expires_at, created_at, updated_at)
SELECT
    UNHEX(REPLACE(UUID(), '-', '')),
    id,
    'EMAIL',
    TRUE,
    DATE_SUB(NOW(), INTERVAL 30 DAY),
    DATE_ADD(NOW(), INTERVAL 335 DAY),
    DATE_SUB(NOW(), INTERVAL 30 DAY),
    NOW()
FROM customers
WHERE email IN ('kim.minji@example.com', 'lee.seojun@example.com', 'park.jiwoo@example.com',
                'choi.yejin@example.com', 'jung.dohyun@example.com');

-- Consents expiring soon (within 30 days)
INSERT INTO marketing_consents (id, customer_id, channel, consented, consented_at, expires_at, created_at, updated_at)
SELECT
    UNHEX(REPLACE(UUID(), '-', '')),
    id,
    'SMS',
    TRUE,
    DATE_SUB(NOW(), INTERVAL 340 DAY),
    DATE_ADD(NOW(), INTERVAL 25 DAY),
    DATE_SUB(NOW(), INTERVAL 340 DAY),
    NOW()
FROM customers
WHERE email IN ('kang.haneul@example.com', 'yoon.soobin@example.com', 'lim.jaehyun@example.com');

-- Expired consents
INSERT INTO marketing_consents (id, customer_id, channel, consented, consented_at, expires_at, created_at, updated_at)
SELECT
    UNHEX(REPLACE(UUID(), '-', '')),
    id,
    'PUSH',
    TRUE,
    DATE_SUB(NOW(), INTERVAL 400 DAY),
    DATE_SUB(NOW(), INTERVAL 35 DAY),
    DATE_SUB(NOW(), INTERVAL 400 DAY),
    NOW()
FROM customers
WHERE email IN ('han.yujin@example.com', 'song.eunchae@example.com', 'oh.minseok@example.com',
                'shin.daeun@example.com', 'baek.joonho@example.com');

-- Multiple channel consents for VIP customers
INSERT INTO marketing_consents (id, customer_id, channel, consented, consented_at, expires_at, created_at, updated_at)
SELECT
    UNHEX(REPLACE(UUID(), '-', '')),
    c.id,
    ch.channel,
    TRUE,
    DATE_SUB(NOW(), INTERVAL 60 DAY),
    DATE_ADD(NOW(), INTERVAL 305 DAY),
    DATE_SUB(NOW(), INTERVAL 60 DAY),
    NOW()
FROM customers c
CROSS JOIN (
    SELECT 'EMAIL' as channel
    UNION ALL SELECT 'SMS'
    UNION ALL SELECT 'PUSH'
) ch
WHERE c.tier = 'VIP'
  AND c.email IN ('iu.lee@example.com', 'gong.yoo@example.com', 'lee.dongwook@example.com')
  AND NOT EXISTS (
    SELECT 1 FROM marketing_consents mc
    WHERE mc.customer_id = c.id AND mc.channel = ch.channel
  );

-- Insert Customer Orders (for VIP tier calculation)
-- High-value customers (eligible for VIP upgrade)
INSERT INTO customer_orders (id, customer_id, order_amount, order_date, status, created_at)
SELECT
    UNHEX(REPLACE(UUID(), '-', '')),
    c.id,
    CASE
        WHEN RAND() < 0.3 THEN 150000 + (RAND() * 100000)  -- 15만~25만
        WHEN RAND() < 0.6 THEN 100000 + (RAND() * 50000)   -- 10만~15만
        ELSE 50000 + (RAND() * 50000)                       -- 5만~10만
    END,
    DATE_SUB(NOW(), INTERVAL FLOOR(1 + (RAND() * 180)) DAY),
    'COMPLETED',
    DATE_SUB(NOW(), INTERVAL FLOOR(1 + (RAND() * 180)) DAY)
FROM customers c
CROSS JOIN (SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4 UNION ALL SELECT 5) nums
WHERE c.email IN ('kim.taeri@example.com', 'ryu.junyeol@example.com', 'jung.haein@example.com')
  AND c.tier != 'VIP'
LIMIT 15;

-- Medium-value customers
INSERT INTO customer_orders (id, customer_id, order_amount, order_date, status, created_at)
SELECT
    UNHEX(REPLACE(UUID(), '-', '')),
    c.id,
    30000 + (RAND() * 70000),  -- 3만~10만
    DATE_SUB(NOW(), INTERVAL FLOOR(1 + (RAND() * 180)) DAY),
    'COMPLETED',
    DATE_SUB(NOW(), INTERVAL FLOOR(1 + (RAND() * 180)) DAY)
FROM customers c
CROSS JOIN (SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3) nums
WHERE c.tier IN ('GOLD', 'SILVER')
  AND c.status = 'ACTIVE'
  AND c.last_login_at > DATE_SUB(NOW(), INTERVAL 90 DAY)
LIMIT 30;

-- Low-value customers
INSERT INTO customer_orders (id, customer_id, order_amount, order_date, status, created_at)
SELECT
    UNHEX(REPLACE(UUID(), '-', '')),
    c.id,
    10000 + (RAND() * 40000),  -- 1만~5만
    DATE_SUB(NOW(), INTERVAL FLOOR(1 + (RAND() * 180)) DAY),
    'COMPLETED',
    DATE_SUB(NOW(), INTERVAL FLOOR(1 + (RAND() * 180)) DAY)
FROM customers c
WHERE c.tier = 'BRONZE'
  AND c.status = 'ACTIVE'
LIMIT 20;

-- VIP customers with consistent high orders
INSERT INTO customer_orders (id, customer_id, order_amount, order_date, status, created_at)
SELECT
    UNHEX(REPLACE(UUID(), '-', '')),
    c.id,
    200000 + (RAND() * 300000),  -- 20만~50만
    DATE_SUB(NOW(), INTERVAL FLOOR(1 + (RAND() * 180)) DAY),
    'COMPLETED',
    DATE_SUB(NOW(), INTERVAL FLOOR(1 + (RAND() * 180)) DAY)
FROM customers c
CROSS JOIN (SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4 UNION ALL SELECT 5 UNION ALL SELECT 6) nums
WHERE c.tier = 'VIP'
  AND c.status = 'ACTIVE'
LIMIT 30;
