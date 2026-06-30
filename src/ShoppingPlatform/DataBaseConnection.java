package ShoppingPlatform;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DataBaseConnection {
    private static String URL;
    private static String USER;
    private static String PASSWORD;

    static {
        try (InputStream input = DataBaseConnection.class
                .getClassLoader().getResourceAsStream("db.properties")) {
            Properties prop = new Properties();
            prop.load(input);
            URL      = prop.getProperty("DB_URL");
            USER     = prop.getProperty("DB_USER");
            PASSWORD = prop.getProperty("DB_PASSWORD");
        } catch (Exception e) {
            throw new RuntimeException("Could not load db.properties", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}