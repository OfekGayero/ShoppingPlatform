package ShoppingPlatform;

import ShoppingPlatform.DataAccessObject.CartDAO;
import ShoppingPlatform.DataAccessObject.ItemDAO;
import ShoppingPlatform.DataAccessObject.UserDAO;

import java.sql.*;
import java.util.Arrays;
import java.util.Date;

public class Admin {
    private Buyer[] buyerList;
    private int buyerCount;
    private Seller[] sellerList;
    private int sellerCount;

    private final CartDAO cartDAO = new CartDAO();
    private final ItemDAO itemDAO = new ItemDAO();
    private final UserDAO userDAO = new UserDAO();

    public Admin() {
        sellerList = new Seller[0];
        buyerList = new Buyer[0];
        loadFromDB();
    }

    // ── Startup: load existing DB data into memory ───────────────

    private void loadFromDB() {
        try {
            ResultSet rs = userDAO.getAllSellers();
            while (rs.next()) {
                int uid = rs.getInt("UID");
                Seller seller = new Seller(rs.getString("USERNAME"), rs.getString("PASSWORD"));
                seller.setUid(uid);
                ResultSet itemRs = itemDAO.getItemsBySeller(uid);
                while (itemRs.next()) {
                    Item item = new Item(
                        itemRs.getString("ITEM_NAME"),
                        itemRs.getDouble("PRICE"),
                        Catagory.valueOf(itemRs.getString("CATEGORY"))
                    );
                    item.setSerial(itemRs.getInt("SERIAL_NUMBER"));
                    seller.getSellingList().addToList(item);
                }
                addSellerToMemory(seller);
            }
        } catch (SQLException e) {
            System.out.println("Error loading sellers from DB: " + e.getMessage());
        }

        try {
            ResultSet rs = userDAO.getAllBuyers();
            while (rs.next()) {
                int uid = rs.getInt("UID");
                Address address = new Address(
                    rs.getString("STREET_NAME"),
                    rs.getInt("BUILDING_NUM"),
                    rs.getString("CITY"),
                    rs.getString("COUNTRY")
                );
                Buyer buyer = new Buyer(rs.getString("USERNAME"), rs.getString("PASSWORD"), address);
                buyer.setUid(uid);
                addBuyerToMemory(buyer);
            }
        } catch (SQLException e) {
            System.out.println("Error loading buyers from DB: " + e.getMessage());
        }
    }

    private void addSellerToMemory(Seller seller) {
        if (sellerCount == sellerList.length) {
            sellerList = (sellerList.length == 0)
                ? new Seller[1]
                : Arrays.copyOf(sellerList, sellerList.length * 2);
        }
        sellerList[sellerCount++] = seller;
    }

    private void addBuyerToMemory(Buyer buyer) {
        if (buyerCount == buyerList.length) {
            buyerList = (buyerList.length == 0)
                ? new Buyer[1]
                : Arrays.copyOf(buyerList, buyerList.length * 2);
        }
        buyerList[buyerCount++] = buyer;
    }

    // ── User management (DB-backed, with transaction) ────────────

    public boolean addSeller(String name, String password) {
        String insertUserSQL   = "INSERT INTO UsersTable (USERNAME, PASSWORD, USER_TYPE) VALUES (?, ?, 'SELLER') RETURNING UID;";
        String insertSellerSQL = "INSERT INTO SellersTable (UID) VALUES (?);";
        Connection conn = null;
        PreparedStatement pstmtUser = null;
        PreparedStatement pstmtSeller = null;
        ResultSet rs = null;
        try {
            conn = DBManager.getConnection();
            conn.setAutoCommit(false);
            pstmtUser = conn.prepareStatement(insertUserSQL);
            pstmtUser.setString(1, name);
            pstmtUser.setString(2, password);
            rs = pstmtUser.executeQuery();
            int uid = rs.next() ? rs.getInt("UID") : -1;
            if (uid != -1) {
                pstmtSeller = conn.prepareStatement(insertSellerSQL);
                pstmtSeller.setInt(1, uid);
                pstmtSeller.executeUpdate();
                conn.commit();
                Seller seller = new Seller(name, password);
                seller.setUid(uid);
                addSellerToMemory(seller);
                return true;
            }
            conn.rollback();
            return false;
        } catch (SQLException e) {
            System.out.println("Error adding seller: " + e.getMessage());
            if (conn != null) { try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); } }
            return false;
        } finally {
            DBManager.closeResources(conn, pstmtUser, rs);
            try { if (pstmtSeller != null) pstmtSeller.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
    }

    public boolean addBuyer(String name, String password, String streetName, String userBuildingNum, String city, String country) {
        String insertUserSQL  = "INSERT INTO UsersTable (USERNAME, PASSWORD, USER_TYPE) VALUES (?, ?, 'BUYER') RETURNING UID;";
        String insertBuyerSQL = "INSERT INTO BuyersTable (UID, STREET_NAME, BUILDING_NUM, CITY, COUNTRY) VALUES (?, ?, ?, ?, ?);";
        Connection conn = null;
        PreparedStatement pstmtUser = null;
        PreparedStatement pstmtBuyer = null;
        ResultSet rs = null;
        try {
            int buildingNum = Integer.parseInt(userBuildingNum);
            conn = DBManager.getConnection();
            conn.setAutoCommit(false);
            pstmtUser = conn.prepareStatement(insertUserSQL);
            pstmtUser.setString(1, name);
            pstmtUser.setString(2, password);
            rs = pstmtUser.executeQuery();
            int uid = rs.next() ? rs.getInt("UID") : -1;
            if (uid != -1) {
                pstmtBuyer = conn.prepareStatement(insertBuyerSQL);
                pstmtBuyer.setInt(1, uid);
                pstmtBuyer.setString(2, streetName);
                pstmtBuyer.setInt(3, buildingNum);
                pstmtBuyer.setString(4, city);
                pstmtBuyer.setString(5, country);
                pstmtBuyer.executeUpdate();
                conn.commit();
                Buyer buyer = new Buyer(name, password, new Address(streetName, buildingNum, city, country));
                buyer.setUid(uid);
                addBuyerToMemory(buyer);
                return true;
            }
            conn.rollback();
            return false;
        } catch (SQLException | NumberFormatException e) {
            System.out.println("Error adding buyer: " + e.getMessage());
            if (conn != null) { try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); } }
            return false;
        } finally {
            DBManager.closeResources(conn, pstmtUser, rs);
            try { if (pstmtBuyer != null) pstmtBuyer.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
    }

    public boolean checkName(String name) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = DBManager.getConnection();
            pstmt = conn.prepareStatement("SELECT 1 FROM UsersTable WHERE USERNAME = ?;");
            pstmt.setString(1, name);
            rs = pstmt.executeQuery();
            return !rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            DBManager.closeResources(conn, pstmt, rs);
        }
    }

    // ── Item management ──────────────────────────────────────────

    public void addItemtoSeller(String name, String price, Catagory catagory, String addition, int index) {
        double actualPrice = Integer.parseInt(price) + Integer.parseInt(addition);
        int sellerUid = sellerList[index].getUid();
        try {
            int serial = itemDAO.insertItem(name, actualPrice, catagory.name(), sellerUid);
            Item newItem = new Item(name, actualPrice, catagory);
            newItem.setSerial(serial);
            sellerList[index].getSellingList().addToList(newItem);
        } catch (SQLException e) {
            System.out.println("Error adding item to DB: " + e.getMessage());
        }
    }

    public Item findItemInSeller(String itemName, int sellerIndex) {
        return sellerList[sellerIndex].getSellingList().findItem(itemName);
    }

    public void addItemtoBuyer(Item item, int buyerIndex) {
        buyerList[buyerIndex].getCurrent().getItemList().addToList(item);
    }

    public void updateItem(int sellerIndex, String itemName, String newName, String newPrice) {
        Item item = sellerList[sellerIndex].getSellingList().findItem(itemName);
        if (item == null) {
            System.out.println("Item not found.");
            return;
        }
        double price = Double.parseDouble(newPrice);
        try {
            itemDAO.updateItem(item.getSerial(), newName, price);
            item.setItemName(newName);
            item.setPrice(price);
        } catch (SQLException e) {
            System.out.println("Error updating item in DB: " + e.getMessage());
        }
    }

    public void deleteItem(int sellerIndex, String itemName) {
        Item item = sellerList[sellerIndex].getSellingList().findItem(itemName);
        if (item == null) {
            System.out.println("Item not found.");
            return;
        }
        try {
            itemDAO.deleteItem(item.getSerial());
            sellerList[sellerIndex].getSellingList().removeItem(item.getSerial());
        } catch (SQLException e) {
            System.out.println("Error deleting item from DB: " + e.getMessage());
        }
    }

    // ── Cart / payment ───────────────────────────────────────────

    public void payForShoppingCart(int buyerIndex) {
        String dateStr = new Date().toString();
        int buyerUid = buyerList[buyerIndex].getUid();
        try {
            int cid = cartDAO.insertCart(buyerUid, dateStr);
            ItemList items = buyerList[buyerIndex].getCurrent().getItemList();
            for (int i = 0; i < items.getListLen(); i++) {
                cartDAO.insertCartItem(cid, items.getList()[i].getSerial());
            }
        } catch (SQLException e) {
            System.out.println("Error saving cart to DB: " + e.getMessage());
        }
        buyerList[buyerIndex].getCurrent().setDate(dateStr);
        buyerList[buyerIndex].addToHistory();
        buyerList[buyerIndex].setCurrent(new ShoppingCart());
    }

    public String printHistory(int buyerIndex) {
        int buyerUid = buyerList[buyerIndex].getUid();
        StringBuilder sb = new StringBuilder();
        try {
            ResultSet rs = cartDAO.getCartHistory(buyerUid);
            int lastCid = -1;
            int cartNum = 0;
            while (rs.next()) {
                int cid = rs.getInt("CID");
                if (cid != lastCid) {
                    cartNum++;
                    sb.append("\nHistory cart ").append(cartNum)
                      .append(" (").append(rs.getString("CART_DATE")).append("):\n");
                    lastCid = cid;
                }
                sb.append("  ").append(rs.getString("ITEM_NAME"))
                  .append(" - $").append(rs.getDouble("PRICE")).append("\n");
            }
        } catch (SQLException e) {
            System.out.println("Error reading cart history: " + e.getMessage());
        }
        return sb.toString();
    }

    // ── Category search (DB) ─────────────────────────────────────

    public String[] catagoryList(Catagory catagory) {
        String[] itemToPrint = new String[0];
        int counter = 0;
        try {
            ResultSet rs = itemDAO.getItemsByCategory(catagory.name());
            while (rs.next()) {
                String entry = "\nItem Name:'" + rs.getString("ITEM_NAME") +
                               "', Price:" + rs.getDouble("PRICE") +
                               " Serial Number: " + rs.getInt("SERIAL_NUMBER") +
                               " Category: " + rs.getString("CATEGORY") +
                               " Seller: " + rs.getString("seller_name");
                if (counter == itemToPrint.length) {
                    itemToPrint = (itemToPrint.length == 0)
                        ? new String[1]
                        : Arrays.copyOf(itemToPrint, itemToPrint.length + 1);
                }
                itemToPrint[counter++] = entry;
            }
        } catch (SQLException e) {
            System.out.println("Error fetching category items: " + e.getMessage());
        }
        return itemToPrint;
    }

    // ── Getters / setters ────────────────────────────────────────

    public Buyer[] getBuyerList() { return buyerList; }
    public void setBuyerList(Buyer[] buyerList) { this.buyerList = buyerList; }
    public int getBuyerCount() { return buyerCount; }
    public void setBuyerCount(int buyerCount) { this.buyerCount = buyerCount; }
    public Seller[] getSellerList() { return sellerList; }
    public void setSellerList(Seller[] sellerList) { this.sellerList = sellerList; }
    public int getSellerCount() { return sellerCount; }
    public void setSellerCount(int sellerCount) { this.sellerCount = sellerCount; }

    public Seller[] getSortedSellers() {
        Seller[] sortedSellers = new Seller[sellerCount];
        for (int i = 0; i < sellerCount; i++) sortedSellers[i] = sellerList[i];
        Arrays.sort(sortedSellers);
        return sortedSellers;
    }

    public Buyer[] getSortedBuyers() {
        Buyer[] sortedBuyer = new Buyer[buyerCount];
        for (int i = 0; i < buyerCount; i++) sortedBuyer[i] = buyerList[i];
        Arrays.sort(sortedBuyer);
        return sortedBuyer;
    }

    // ── Lookup helpers ───────────────────────────────────────────

    public int isInSellers(String sellerName) {
        for (int i = 0; i < sellerCount; i++) {
            if (sellerName.equals(sellerList[i].getUserName())) return i;
        }
        return -1;
    }

    public int isInBuyers(String buyerName) {
        for (int i = 0; i < buyerCount; i++) {
            if (buyerName.equals(buyerList[i].getUserName())) return i;
        }
        return -1;
    }

    public int getTotalPrice(int buyerIndex) {
        return buyerList[buyerIndex].getCurrent().getItemList().getTotalPrice();
    }

    public String printSellerList(int sellerIndex) {
        return sellerList[sellerIndex].getSellingList().toString();
    }

    public int getBuyerHistoryLen(int buyerIndex) {
        return buyerList[buyerIndex].getHistoryLen();
    }

    public void cloneShoppingCart(int buyerIndex, int cartIndex) {
        try {
            buyerList[buyerIndex].setCurrent(buyerList[buyerIndex].getHistory()[cartIndex].clone());
        } catch (CloneNotSupportedException e) {
            System.out.println(e.getMessage());
        }
    }

    // ── Validation helpers ───────────────────────────────────────

    public boolean wrongTypeHandling(String convertToInt) {
        try {
            Integer.parseInt(convertToInt);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public boolean checkBuyerListEmpty() { return buyerCount == 0; }
    public boolean checkSellerListEmpty() { return sellerCount == 0; }

    public boolean checkEmptyCart(int buyerIndex) {
        try {
            buyerList[buyerIndex].getCurrent().getItemAmount();
            return true;
        } catch (PaymentEmptyCartException e) {
            return false;
        }
    }
}
