package ShoppingPlatform.DataAccessObject;
import ShoppingPlatform.DataBaseConnection;
import java.sql.*;

public class QueryDAO {

    public ResultSet getElectricItems() throws SQLException {
        String sql = "SELECT SERIAL_NUMBER, ITEM_NAME, PRICE FROM ItemsTable WHERE CATEGORY = 'ELECTRIC'";
        Connection conn = DataBaseConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
        return ps.executeQuery();
    }

    public ResultSet getAverageOfEachCategory() throws SQLException {
        String sql = "SELECT category, ROUND(AVG(price), 2) AS average_price\n" +
                     "FROM ItemsTable\n" +
                     "GROUP BY category;";
        Connection conn = DataBaseConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
        return ps.executeQuery();

    }

    public ResultSet userWithMostItems() throws SQLException {
        String sql = "SELECT u.username, COUNT(i.serial_number) AS items_count\n" +
                     "FROM UsersTable u\n" +
                     "JOIN ItemsTable i ON u.uid = i.uid\n" +
                     "GROUP BY u.uid, u.username\n" +
                     "ORDER BY items_count DESC LIMIT 1;";
        Connection conn = DataBaseConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
        return ps.executeQuery();

    }
    public ResultSet aliceCartHistory() throws SQLException {
        String sql = "SELECT sc.cid, sc.cart_date\n" +
                     "FROM ShoppingCartsTable sc\n" +
                     "JOIN UsersTable u ON sc.uid = u.uid\n" +
                     "WHERE u.username = 'alice';";
        Connection conn = DataBaseConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
        return ps.executeQuery();
    }

    public ResultSet itemsFromCartID3() throws SQLException {
        String sql = "SELECT i.item_name, i.price, i.category\n" +
                     "FROM CartItemsTable ci\n" +
                     "JOIN ItemsTable i ON ci.serial_number = i.serial_number\n" +
                     "WHERE ci.cid = 3;\n";
        Connection conn = DataBaseConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
        return ps.executeQuery();
    }

    public ResultSet totalPriceByCartID() throws SQLException {
        String sql = "SELECT ci.cid, SUM(i.price) AS total_cart_price\n" +
                     "FROM CartItemsTable ci\n" +
                     "JOIN ItemsTable i ON ci.serial_number = i.serial_number\n" +
                     "GROUP BY ci.cid ORDER BY ci.cid;";
        Connection conn = DataBaseConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
        return ps.executeQuery();
    }
    public ResultSet davesMostExpensiveItem() throws SQLException {
        String sql = "SELECT i.item_name, i.price\n" +
                     "FROM ItemsTable i JOIN UsersTable u ON i.uid = u.uid\n" +
                     "WHERE u.username = 'dave_sells' ORDER BY i.price DESC LIMIT 1;";
        Connection conn = DataBaseConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
        return ps.executeQuery();
    }
    public ResultSet cartsPerBuyer() throws SQLException {
        String sql = "SELECT u.username, COUNT(sc.cid) AS order_count\n" +
                     "FROM ShoppingCartsTable sc JOIN UsersTable u ON sc.uid = u.uid\n" +
                     "GROUP BY u.uid, u.username HAVING COUNT(sc.cid) > 1;";
        Connection conn = DataBaseConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
        return ps.executeQuery();
    }
    public ResultSet mostPurchasedItem() throws SQLException {
        String sql = "SELECT i.item_name, COUNT(ci.cid) AS times_purchased\n" +
                     "FROM CartItemsTable ci JOIN ItemsTable i ON ci.serial_number = i.serial_number\n" +
                     "GROUP BY i.serial_number, i.item_name ORDER BY times_purchased DESC LIMIT 1;";
        Connection conn = DataBaseConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
        return ps.executeQuery();
    }

    public ResultSet ordersPerCountry() throws SQLException {
        String sql = "SELECT b.country, COUNT(sc.cid) AS total_orders\n" +
                     "FROM ShoppingCartsTable sc JOIN BuyersTable b ON sc.uid = b.uid\n" +
                     "GROUP BY b.country;";
        Connection conn = DataBaseConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
        return ps.executeQuery();
    }


}
