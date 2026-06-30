package ShoppingPlatform.DataAccessObject;
import ShoppingPlatform.DataBaseConnection;

import java.sql.*;

public class ItemDAO {
        public int insertItem(String itemName, double price,
                          String category, int sellerUID) throws SQLException {
        String sql = "INSERT INTO ItemsTable (ITEM_NAME, PRICE, CATEGORY, UID) VALUES (?, ?, ?, ?) RETURNING SERIAL_NUMBER";
        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, itemName);
            ps.setDouble(2, price);
            ps.setString(3, category);
            ps.setInt(4, sellerUID);
            ResultSet rs = ps.executeQuery();
            rs.next();
            return rs.getInt("SERIAL_NUMBER");
        }
    }

    public ResultSet getItemsBySeller(int sellerUID) throws SQLException {
        String sql = "SELECT * FROM ItemsTable WHERE UID = ?";
        Connection conn = DataBaseConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, sellerUID);
        return ps.executeQuery();
    }

    public ResultSet getItemsByCategory(String category) throws SQLException {
        String sql = "SELECT i.*, u.USERNAME AS seller_name FROM ItemsTable i " +
                     "JOIN UsersTable u ON i.UID = u.UID WHERE i.CATEGORY = ?";
        Connection conn = DataBaseConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, category);
        return ps.executeQuery();
    }
}
