# CLAUDE.md — ShoppingPlatform

## Project Overview

A Java console application backed by a PostgreSQL database, built as a university Database Systems course project (course 10127).

Users are either **Buyers** or **Sellers**. Sellers list Items. Buyers add items to a ShoppingCart and pay, which saves the cart to history.

## Build & Run

Plain IntelliJ IDEA project — no Maven/Gradle.

```bash
# Compile (from project root)
javac -cp src -d out $(find src -name "*.java")

# Run
java -cp out ShoppingPlatform.Main
```

The PostgreSQL JDBC driver must be on the classpath. In IntelliJ: Project Structure → Libraries.

## Database Setup

1. Create a PostgreSQL database named `ShoppingPlatform`
2. Run `schema.sql` to create tables and the duplicate-username trigger
3. Run `sample_data.sql` to load test data
4. Create `src/db.properties` (gitignored):

```properties
DB_URL=jdbc:postgresql://localhost:5432/ShoppingPlatform
DB_USER=postgres
DB_PASSWORD=your_password
```

`db.properties` is loaded at class-load time by `DataBaseConnection`. Missing file → `RuntimeException` on startup.

## Naming Convention

All SQL table and column names use **PascalCase with Table suffix**:
`UsersTable`, `BuyersTable`, `SellersTable`, `ItemsTable`, `ShoppingCartsTable`, `CartItemsTable`

Column names are UPPERCASE: `UID`, `USERNAME`, `SERIAL_NUMBER`, `CART_DATE`, etc.

This is the authoritative convention. All Java DAO queries must match it.

## SQL Files

| File | Purpose |
|---|---|
| `schema.sql` | DDL — CREATE all tables + duplicate-username trigger |
| `sample_data.sql` | Seed data (5 users, 7 items, 4 carts) |
| `queries.sql` | 10 reference/analytical queries (not used at runtime) |
| `additional-files-to-check/ShoppingPlatform_DB.sql` | Combined reference file (DROP + CREATE + trigger + data + queries) |

## Architecture

### Source files (`src/ShoppingPlatform/`)

**Domain model (in-memory layer):**
- `Main.java` — CLI menu, delegates everything to `Admin`
- `Admin.java` — central controller, owns all business logic
- `User.java` (abstract) → `Buyer.java` / `Seller.java`
- `Item.java`, `ItemList.java`, `ShoppingCart.java`
- `Address.java`, `Catagory.java` (enum), `PaymentEmptyCartException.java`

**Database layer (`DataAccessObject/`):**
- `DataBaseConnection.java` — loads `db.properties`, provides `getConnection()`
- `CartDAO.java` — insert completed carts/items, fetch cart history and totals
- `ItemDAO.java` — insert items, fetch by seller or category
- `UserDAO.java` — currently a copy-paste of CartDAO (broken, needs to be rewritten)

### Key design notes
- `Item.serialGenerator` is a static in-memory counter, independent of the DB sequence. Once items come from DB, serial numbers must come from `RETURNING SERIAL_NUMBER`.
- `ShoppingCart` and `Item` implement `Cloneable` for deep-clone cart history copies (menu option 9).
- Arrays use manual doubling pattern — no `ArrayList`.
- `getCartHistory` and `getCartTotal` in CartDAO return open `ResultSet`/`Connection` — callers must close them.

---

## Implementation Plan

### Step 1 — Fix SQL files ✅
- [x] Replace `schema.sql` with uppercase schema (from `additional-files-to-check/CreateTheTables.sql`) and add the trigger (from `additional-files-to-check/Triggers.sql`)
- [x] Update `sample_data.sql` to match the uppercase schema exactly (align with `additional-files-to-check/DataForTables.sql`)
- [x] Create `queries.sql` from `additional-files-to-check/QueriesForProject.sql`

### Step 2 — Create `DBManager.java` ✅
- [x] Create `src/ShoppingPlatform/DBManager.java`
- [x] Implement `getConnection()` (delegates to `DataBaseConnection`) and `closeResources(Connection, Statement, ResultSet)` helper
- [x] The new `Admin.java` (from `additional-files-to-check/`) depends on this class

### Step 3 — Fix `UserDAO.java` ✅
- [x] Rewrite `src/ShoppingPlatform/DataAccessObject/UserDAO.java` — was a broken copy of CartDAO
- [x] Add methods: `getAllSellers()`, `getAllBuyers()` (to load DB data into memory at startup)
- [x] Add `getUIDByUsername(String username)` — needed in Step 4 to resolve seller/buyer UID when inserting items and carts

### Step 4 — Replace `Admin.java` and wire DB ✅
- [x] Replace `Admin.java` with the new DB-backed version from `additional-files-to-check/Admin.java` as the base (covers `addSeller`, `addBuyer`, `checkName`)
- [x] Keep all methods that still need to work: `addItemtoSeller`, `findItemInSeller`, `addItemtoBuyer`, `payForShoppingCart`, `catagoryList`, `printHistory`, `cloneShoppingCart`, `checkEmptyCart`, `checkBuyerListEmpty`, `checkSellerListEmpty`, `getTotalPrice`, `printSellerList`, `getBuyerHistoryLen`
- [x] Wire `addItemtoSeller` → `ItemDAO.insertItem`
- [x] Wire `payForShoppingCart` → `CartDAO.insertCart` + `CartDAO.insertCartItem`
- [x] Wire `printHistory` → `CartDAO.getCartHistory`
- [x] Wire `catagoryList` → `ItemDAO.getItemsByCategory`
- [x] Add startup `loadFromDB()` — call `UserDAO.getAllSellers()` and `UserDAO.getAllBuyers()` to populate in-memory arrays

### Step 5 — Add update & delete operations (required by course) ✅
- [x] Add `updateItem(int serialNumber, String newName, double newPrice)` to `ItemDAO`
- [x] Add `deleteItem(int serialNumber)` to `ItemDAO`
- [x] Add corresponding methods to `Admin.java`
- [x] Add new menu options to `Main.java` (option 10: update item, option 11: delete item)

### Step 6 — End-to-end verification ✅
- [x] Compile cleanly with no errors — all 16 classes produced
- [ ] Run app against live DB, test all menu paths
- [ ] Verify insert (add seller/buyer/item), update item, delete item, search by category, pay/cart history all hit the DB correctly

---

## Progress Log

### Step 6 — End-to-end verification (compile done)
- Compiled cleanly with zero errors — all 16 classes produced (`javac -cp src -d out`)
- Runtime testing against live DB pending (requires `src/db.properties` with valid credentials)

### Step 5 — Update & delete operations (done)
- `src/ShoppingPlatform/DataAccessObject/ItemDAO.java` — added `updateItem(int serial, String newName, double newPrice)` and `deleteItem(int serial)`
- `src/ShoppingPlatform/ItemList.java` — added `removeItem(int serial)`: shifts elements left after removal
- `src/ShoppingPlatform/Admin.java` — added `updateItem(sellerIndex, itemName, newName, newPrice)` and `deleteItem(sellerIndex, itemName)`; both update DB first then sync in-memory list
- `src/ShoppingPlatform/Main.java` — added menu options 10 (update item) and 11 (delete item) with full input handling

### Step 4 — Wire DB into Admin (done)
- `src/ShoppingPlatform/Admin.java` — fully rewritten: DB-backed `addSeller`/`addBuyer`/`checkName` (with transactions); `loadFromDB()` on startup loads all sellers+items and buyers from DB into memory; `addItemtoSeller` persists via `ItemDAO`; `payForShoppingCart` persists via `CartDAO`; `printHistory` reads from DB; `catagoryList` queries DB
- `src/ShoppingPlatform/User.java` — added `uid` field with `getUid()`/`setUid()` so DB-assigned UIDs are stored on each User object
- `src/ShoppingPlatform/Item.java` — added `setSerial(int)` so DB-assigned serial numbers can be set when loading items from DB
- `src/ShoppingPlatform/DataAccessObject/CartDAO.java` — fixed column name `DATE` → `CART_DATE` in both `insertCart` and `getCartHistory`
- `src/ShoppingPlatform/Main.java` — updated `addSellerToArray` → `addSeller`, `addBuyerToArray` → `addBuyer`

### Step 3 — Fix `UserDAO.java` (done)
- `src/ShoppingPlatform/DataAccessObject/UserDAO.java` — fully rewritten; removed the CartDAO copy-paste; added `getAllSellers()`, `getAllBuyers()` (return open ResultSets for startup population), and `getUIDByUsername(String)` for UID lookups needed in Step 4

### Step 2 — Create `DBManager.java` (done)
- `src/ShoppingPlatform/DBManager.java` — created; delegates `getConnection()` to `DataBaseConnection` and provides `closeResources(Connection, Statement, ResultSet)` used by the new `Admin.java`

### Step 1 — Fix SQL files (done)
- `schema.sql` — rewritten with uppercase table names (`UsersTable`, `BuyersTable`, `SellersTable`, `ItemsTable`, `ShoppingCartsTable`, `CartItemsTable`) and the duplicate-username trigger included
- `sample_data.sql` — rewritten to match the new schema (uppercase names, correct column names including `CART_DATE`)
- `queries.sql` — created with 10 analytical queries using the uppercase schema
