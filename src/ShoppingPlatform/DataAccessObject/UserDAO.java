package ShoppingPlatform.DataAccessObject;

import ShoppingPlatform.DataBaseConnection;

import java.sql.*;

public class UserDAO {

    public ResultSet getAllSellers() throws SQLException {
        String sql = "SELECT u.UID, u.USERNAME, u.PASSWORD " +
                     "FROM UsersTable u JOIN SellersTable s ON u.UID = s.UID";
        Connection conn = DataBaseConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
        return ps.executeQuery();
    }

    public ResultSet getAllBuyers() throws SQLException {
        String sql = "SELECT u.UID, u.USERNAME, u.PASSWORD, " +
                     "b.STREET_NAME, b.BUILDING_NUM, b.CITY, b.COUNTRY " +
                     "FROM UsersTable u JOIN BuyersTable b ON u.UID = b.UID";
        Connection conn = DataBaseConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
        return ps.executeQuery();
    }

    public int getUIDByUsername(String username) throws SQLException {
        String sql = "SELECT UID FROM UsersTable WHERE USERNAME = ?";
        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("UID");
            }
            return -1;
        }
    }
}
