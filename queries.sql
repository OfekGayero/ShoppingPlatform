-- ── 10 MEANINGFUL QUERIES ─────────────────────────────────

-- Query 1 : Showing all items that are from electric category
SELECT serial_number, item_name, price
FROM ItemsTable
WHERE category = 'ELECTRIC';

-- Query 2 : Shows average price of items from each category
SELECT category, ROUND(AVG(price), 2) AS average_price
FROM ItemsTable
GROUP BY category;

-- Query 3 : Shows the user with most items
SELECT u.username, COUNT(i.serial_number) AS items_count
FROM UsersTable u
JOIN ItemsTable i ON u.uid = i.uid
GROUP BY u.uid, u.username
ORDER BY items_count DESC LIMIT 1;

-- Query 4 : Show shopping cart history of alice
SELECT sc.cid, sc.cart_date
FROM ShoppingCartsTable sc
JOIN UsersTable u ON sc.uid = u.uid
WHERE u.username = 'alice';

-- Query 5: Show all items from cart with cid = 3
SELECT i.item_name, i.price, i.category
FROM CartItemsTable ci
JOIN ItemsTable i ON ci.serial_number = i.serial_number
WHERE ci.cid = 3;

-- Query 6: Show all carts total price grouped by cart id
SELECT ci.cid, SUM(i.price) AS total_cart_price
FROM CartItemsTable ci
JOIN ItemsTable i ON ci.serial_number = i.serial_number
GROUP BY ci.cid ORDER BY ci.cid;

-- Query 7: Shows dave_sells most expensive item
SELECT i.item_name, i.price
FROM ItemsTable i JOIN UsersTable u ON i.uid = u.uid
WHERE u.username = 'dave_sells' ORDER BY i.price DESC LIMIT 1;

-- Query 8: Shows number of carts per buyer (that has at least one cart)
SELECT u.username, COUNT(sc.cid) AS order_count
FROM ShoppingCartsTable sc JOIN UsersTable u ON sc.uid = u.uid
GROUP BY u.uid, u.username HAVING COUNT(sc.cid) > 1;

-- Query 9: Shows the item purchased the most
SELECT i.item_name, COUNT(ci.cid) AS times_purchased
FROM CartItemsTable ci JOIN ItemsTable i ON ci.serial_number = i.serial_number
GROUP BY i.serial_number, i.item_name ORDER BY times_purchased DESC LIMIT 1;

-- Query 10: Shows number of orders per country
SELECT b.country, COUNT(sc.cid) AS total_orders
FROM ShoppingCartsTable sc JOIN BuyersTable b ON sc.uid = b.uid
GROUP BY b.country;
