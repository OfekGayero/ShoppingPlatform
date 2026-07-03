package ShoppingPlatform;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBManager {

    public static Connection getConnection() throws SQLException {
        return DataBaseConnection.getConnection();
    }

    public static void closeResources(Connection conn, Statement stmt, ResultSet rs) {
        try { if (rs   != null) rs.close();   } catch (SQLException e) { e.printStackTrace(); }
        try { if (stmt != null) stmt.close();  } catch (SQLException e) { e.printStackTrace(); }
        try { if (conn != null) conn.close();  } catch (SQLException e) { e.printStackTrace(); }
    }
}
