-- ============================================================
--  ShoppingPlatform Sample Data
-- ============================================================

-- ── 1. USERS ─────────────────────────────────────────────────
-- 3 buyers, 2 sellers
INSERT INTO UsersTable (UID, USERNAME, PASSWORD, USER_TYPE) VALUES
(1, 'alice',      'pass123',  'BUYER'),
(2, 'bob',        'pass456',  'BUYER'),
(3, 'carol',      'pass789',  'BUYER'),
(4, 'dave_sells', 'sell123',  'SELLER'),
(5, 'eve_shop',   'sell456',  'SELLER');

-- ── 2. BUYERS ────────────────────────────────────────────────
INSERT INTO BuyersTable (UID, STREET_NAME, BUILDING_NUM, CITY, COUNTRY) VALUES
(1, 'Main Street',   10,  'New York',  'USA'),
(2, 'Oak Avenue',     3,  'London',    'UK'),
(3, 'Pine Road',     22,  'Tel Aviv',  'Israel');

-- ── 3. SELLERS ───────────────────────────────────────────────
INSERT INTO SellersTable (UID) VALUES
(4),
(5);

-- ── 4. ITEMS ─────────────────────────────────────────────────
-- dave_sells (UID=4): electronics and office supplies
-- eve_shop   (UID=5): children and clothing
INSERT INTO ItemsTable (SERIAL_NUMBER, ITEM_NAME, PRICE, CATEGORY, UID) VALUES
(1, 'Laptop',     999.99,  'ELECTRIC',  4),
(2, 'Mouse',       29.99,  'ELECTRIC',  4),
(3, 'Notebook',     5.99,  'OFFICE',    4),
(4, 'Pen Set',     12.50,  'OFFICE',    4),
(5, 'Baby Toy',    19.99,  'CHILD',     5),
(6, 'T-Shirt',     24.99,  'CLOTHING',  5),
(7, 'Kids Book',   14.99,  'CHILD',     5);

-- ── 5. SHOPPING_CARTS ────────────────────────────────────────
-- Only completed (paid) carts are stored
INSERT INTO ShoppingCartsTable (CID, CART_DATE, UID) VALUES
(1, 'Mon Jan 06 10:30:00 2025',  1),  -- alice
(2, 'Tue Jan 07 14:15:00 2025',  2),  -- bob
(3, 'Wed Jan 08 09:00:00 2025',  3),  -- carol
(4, 'Thu Jan 09 16:45:00 2025',  1);  -- alice second order

-- ── 6. CART_ITEMS ────────────────────────────────────────────
INSERT INTO CartItemsTable (CID, SERIAL_NUMBER) VALUES
-- Cart 1 (alice): Laptop + Mouse
(1, 1),
(1, 2),
-- Cart 2 (bob): T-Shirt + Kids Book
(2, 6),
(2, 7),
-- Cart 3 (carol): Notebook + Pen Set + Baby Toy
(3, 3),
(3, 4),
(3, 5),
-- Cart 4 (alice second order): Mouse + Kids Book
(4, 2),
(4, 7);
