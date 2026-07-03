-- Data into the tables

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

