DROP TABLE IF EXISTS CartItemsTable CASCADE;
DROP TABLE IF EXISTS ShoppingCartsTable CASCADE;
DROP TABLE IF EXISTS ItemsTable CASCADE;
DROP TABLE IF EXISTS SellersTable CASCADE;
DROP TABLE IF EXISTS BuyersTable CASCADE;
DROP TABLE IF EXISTS UsersTable CASCADE;

-- ── 1. TABLE CREATION (DDL) ──────────────────────────────────

CREATE TABLE UsersTable (
    UID       SERIAL        PRIMARY KEY,
    USERNAME  VARCHAR(50)   NOT NULL UNIQUE,
    PASSWORD  VARCHAR(50)   NOT NULL,
    USER_TYPE VARCHAR(10)   NOT NULL CHECK (USER_TYPE IN ('BUYER','SELLER'))
);

CREATE TABLE BuyersTable (
    UID           INT          PRIMARY KEY,
    STREET_NAME   VARCHAR(50)  NOT NULL,
    BUILDING_NUM  INT          NOT NULL,
    CITY          VARCHAR(50)  NOT NULL,
    COUNTRY       VARCHAR(50)  NOT NULL,
    FOREIGN KEY (UID) REFERENCES UsersTable(UID) ON DELETE CASCADE
);

CREATE TABLE SellersTable (
    UID  INT  PRIMARY KEY,
    FOREIGN KEY (UID) REFERENCES UsersTable(UID) ON DELETE CASCADE
);

CREATE TABLE ItemsTable (
    SERIAL_NUMBER  SERIAL         PRIMARY KEY,
    ITEM_NAME      VARCHAR(50)    NOT NULL,
    PRICE          DECIMAL(10,2)  NOT NULL,
    CATEGORY       VARCHAR(20)    NOT NULL CHECK (CATEGORY IN ('CHILD','ELECTRIC','OFFICE','CLOTHING','OTHER')),
    UID            INT            NOT NULL,
    FOREIGN KEY (UID) REFERENCES SellersTable(UID) ON DELETE CASCADE
);

CREATE TABLE ShoppingCartsTable (
    CID        SERIAL        PRIMARY KEY,
    CART_DATE  VARCHAR(50)   NOT NULL,
    UID        INT           NOT NULL,
    FOREIGN KEY (UID) REFERENCES BuyersTable(UID) ON DELETE CASCADE
);

CREATE TABLE CartItemsTable (
    CID            INT  NOT NULL,
    SERIAL_NUMBER  INT  NOT NULL,
    PRIMARY KEY (CID, SERIAL_NUMBER),
    FOREIGN KEY (CID)           REFERENCES ShoppingCartsTable(CID) ON DELETE CASCADE,
    FOREIGN KEY (SERIAL_NUMBER) REFERENCES ItemsTable(SERIAL_NUMBER) ON DELETE CASCADE
);

-- ── 2. BONUS: TRIGGER IMPLEMENTATION ────────────────────────

CREATE OR REPLACE FUNCTION check_duplicate_username()
RETURNS TRIGGER AS $$
BEGIN
    IF EXISTS (SELECT 1 FROM UsersTable WHERE USERNAME = NEW.USERNAME AND UID <> NEW.UID) THEN
        RAISE EXCEPTION 'Registration Failed: Username % is already taken.', NEW.USERNAME;
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_check_username
BEFORE INSERT OR UPDATE ON UsersTable
FOR EACH ROW
EXECUTE FUNCTION check_duplicate_username();

-- ── 3. SAMPLE DATA INSERTION (DML) ──────────────────────────

INSERT INTO UsersTable (UID, USERNAME, PASSWORD, USER_TYPE) VALUES
(1, 'alice',      'pass123',  'BUYER'),
(2, 'bob',        'pass456',  'BUYER'),
(3, 'carol',      'pass789',  'BUYER'),
(4, 'dave_sells', 'sell123',  'SELLER'),
(5, 'eve_shop',   'sell456',  'SELLER');

INSERT INTO BuyersTable (UID, STREET_NAME, BUILDING_NUM, CITY, COUNTRY) VALUES
(1, 'Main Street',   10,  'New York',  'USA'),
(2, 'Oak Avenue',     3,  'London',    'UK'),
(3, 'Pine Road',     22,  'Tel Aviv',  'Israel');

INSERT INTO SellersTable (UID) VALUES (4), (5);

INSERT INTO ItemsTable (SERIAL_NUMBER, ITEM_NAME, PRICE, CATEGORY, UID) VALUES
(1, 'Laptop',     999.99,  'ELECTRIC',  4),
(2, 'Mouse',       29.99,  'ELECTRIC',  4),
(3, 'Notebook',     5.99,  'OFFICE',    4),
(4, 'Pen Set',     12.50,  'OFFICE',    4),
(5, 'Baby Toy',    19.99,  'CHILD',     5),
(6, 'T-Shirt',     24.99,  'CLOTHING',  5),
(7, 'Kids Book',   14.99,  'CHILD',     5);

INSERT INTO ShoppingCartsTable (CID, CART_DATE, UID) VALUES
(1, 'Mon Jan 06 10:30:00 2025',  1),
(2, 'Tue Jan 07 14:15:00 2025',  2),
(3, 'Wed Jan 08 09:00:00 2025',  3),
(4, 'Thu Jan 09 16:45:00 2025',  1);

INSERT INTO CartItemsTable (CID, SERIAL_NUMBER) VALUES
(1, 1), (1, 2),
(2, 6), (2, 7),
(3, 3), (3, 4), (3, 5),
(4, 2), (4, 7);

SELECT setval('userstable_uid_seq', (SELECT MAX(uid) FROM UsersTable));
SELECT setval('itemstable_serial_number_seq', (SELECT MAX(serial_number) FROM ItemsTable));
SELECT setval('shoppingcartstable_cid_seq', (SELECT MAX(cid) FROM ShoppingCartsTable));

-- ── 4. 10 MEANINGFUL QUERIES ─────────────────────────────────

SELECT serial_number, item_name, price FROM ItemsTable WHERE category = 'ELECTRIC';

SELECT category, ROUND(AVG(price), 2) AS average_price FROM ItemsTable GROUP BY category;

SELECT u.username, COUNT(i.serial_number) AS items_count FROM UsersTable u JOIN ItemsTable i ON u.uid = i.uid GROUP BY u.uid, u.username ORDER BY items_count DESC LIMIT 1;

SELECT sc.cid, sc.cart_date FROM ShoppingCartsTable sc JOIN UsersTable u ON sc.uid = u.uid WHERE u.username = 'alice';

SELECT i.item_name, i.price, i.category FROM CartItemsTable ci JOIN ItemsTable i ON ci.serial_number = i.serial_number WHERE ci.cid = 3;

SELECT ci.cid, SUM(i.price) AS total_cart_price FROM CartItemsTable ci JOIN ItemsTable i ON ci.serial_number = i.serial_number GROUP BY ci.cid ORDER BY ci.cid;

SELECT i.item_name, i.price FROM ItemsTable i JOIN UsersTable u ON i.uid = u.uid WHERE u.username = 'dave_sells' ORDER BY i.price DESC LIMIT 1;

SELECT u.username, COUNT(sc.cid) AS order_count FROM ShoppingCartsTable sc JOIN UsersTable u ON sc.uid = u.uid GROUP BY u.uid, u.username HAVING COUNT(sc.cid) > 1;

SELECT i.item_name, COUNT(ci.cid) AS times_purchased FROM CartItemsTable ci JOIN ItemsTable i ON ci.serial_number = i.serial_number GROUP BY i.serial_number, i.item_name ORDER BY times_purchased DESC LIMIT 1;

SELECT b.country, COUNT(sc.cid) AS total_orders FROM ShoppingCartsTable sc JOIN BuyersTable b ON sc.uid = b.uid GROUP BY b.country;