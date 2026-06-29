-- ============================================================
--  ShoppingPlatform Database Schema
--  PostgreSQL
-- ============================================================

-- ── 1. USERS (parent table for Buyer and Seller) ────────────
CREATE TABLE users (
    uid         SERIAL          PRIMARY KEY,
    username    VARCHAR(100)    NOT NULL UNIQUE,
    password    VARCHAR(255)    NOT NULL,
    user_type   VARCHAR(10)     NOT NULL CHECK (user_type IN ('BUYER', 'SELLER'))
);

-- ── 2. BUYERS (IS-A users) ───────────────────────────────────
CREATE TABLE buyers (
    uid             INT             PRIMARY KEY,
    street_name     VARCHAR(150)    NOT NULL,
    building_num    INT             NOT NULL,
    city            VARCHAR(100)    NOT NULL,
    country         VARCHAR(100)    NOT NULL,
    FOREIGN KEY (uid) REFERENCES users(uid) ON DELETE CASCADE
);

-- ── 3. SELLERS (IS-A users) ──────────────────────────────────
CREATE TABLE sellers (
    uid     INT     PRIMARY KEY,
    FOREIGN KEY (uid) REFERENCES users(uid) ON DELETE CASCADE
);

-- ── 4. ITEMS ─────────────────────────────────────────────────
CREATE TABLE items (
    serial      SERIAL          PRIMARY KEY,
    item_name   VARCHAR(150)    NOT NULL,
    price       DECIMAL(10,2)   NOT NULL,
    category    VARCHAR(20)     NOT NULL CHECK (category IN ('CHILD','ELECTRIC','OFFICE','CLOTHING','OTHER')),
    uid         INT             NOT NULL,   -- FK to sellers.uid
    FOREIGN KEY (uid) REFERENCES sellers(uid) ON DELETE CASCADE
);

-- ── 5. SHOPPING_CARTS ────────────────────────────────────────
--  Only completed (paid) carts are saved.
--  cart_price is derived — computed via SUM on cart_items, not stored.
CREATE TABLE shopping_carts (
    cid         SERIAL          PRIMARY KEY,
    uid         INT             NOT NULL,   -- FK to buyers.uid
    date        VARCHAR(100)    NOT NULL,
    FOREIGN KEY (uid) REFERENCES buyers(uid) ON DELETE CASCADE
);

-- ── 6. CART_ITEMS (junction — resolves M:N) ──────────────────
CREATE TABLE cart_items (
    cid         INT     NOT NULL,
    serial      INT     NOT NULL,
    PRIMARY KEY (cid, serial),
    FOREIGN KEY (cid)    REFERENCES shopping_carts(cid) ON DELETE CASCADE,
    FOREIGN KEY (serial) REFERENCES items(serial) ON DELETE CASCADE
);
