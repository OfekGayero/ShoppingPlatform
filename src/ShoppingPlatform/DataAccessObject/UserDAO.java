package ShoppingPlatform.DataAccessObject;
import ShoppingPlatform.DataBaseConnection;

import java.sql.*;
public class UserDAO {
        public int insertCart(int buyerUID, String date) throws SQLException {
        String sql = "INSERT INTO ShoppingCartsTable (DATE, UID) VALUES (?, ?) RETURNING CID";
        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, date);
            ps.setInt(2, buyerUID);
            ResultSet rs = ps.executeQuery();
            rs.next();
            return rs.getInt("CID");
        }
    }

    public void insertCartItem(int cid, int serial) throws SQLException {
        String sql = "INSERT INTO CartItemsTable (CID, SERIAL_NUMBER) VALUES (?, ?)";
        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, cid);
            ps.setInt(2, serial);
            ps.executeUpdate();
        }
    }

    public ResultSet getCartHistory(int buyerUID) throws SQLException {
        String sql = "SELECT sc.CID, sc.DATE, i.ITEM_NAME, i.PRICE " +
                     "FROM ShoppingCartsTable sc " +
                     "JOIN CartItemsTable ci ON sc.CID = ci.CID " +
                     "JOIN ItemsTable i ON ci.SERIAL_NUMBER = i.SERIAL_NUMBER " +
                     "WHERE sc.UID = ? ORDER BY sc.CID";
        Connection conn = DataBaseConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, buyerUID);
        return ps.executeQuery();
    }

    public double getCartTotal(int cid) throws SQLException {
        String sql = "SELECT SUM(i.PRICE) AS TOTAL FROM CartItemsTable ci " +
                     "JOIN ItemsTable i ON ci.SERIAL_NUMBER = i.SERIAL_NUMBER WHERE ci.CID = ?";
        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, cid);
            ResultSet rs = ps.executeQuery();
            rs.next();
            return rs.getDouble("TOTAL");
        }
    }
}
