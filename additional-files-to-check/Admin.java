package ShoppingPlatform;

import java.sql.*;
import java.util.ArrayList;

public class Admin {
    
    public Admin() {
        // No longer storing local static primitive arrays for data management!
    }

    // Supports insert operations for sellers through the database using JDBC
    public boolean addSeller(String name, String password) {
        String insertUserSQL = "INSERT INTO UsersTable (USERNAME, PASSWORD, USER_TYPE) VALUES (?, ?, 'SELLER') RETURNING UID;";
        String insertSellerSQL = "INSERT INTO SellersTable (UID) VALUES (?);";
        
        Connection conn = null;
        PreparedStatement pstmtUser = null;
        PreparedStatement pstmtSeller = null;
        ResultSet rs = null;
        
        try {
            conn = DBManager.getConnection();
            conn.setAutoCommit(false); // Begin Transaction
            
            pstmtUser = conn.prepareStatement(insertUserSQL);
            pstmtUser.setString(1, name);
            pstmtUser.setString(2, password);
            rs = pstmtUser.executeQuery();
            
            int generatedUid = -1;
            if (rs.next()) {
                generatedUid = rs.getInt("UID");
            }
            
            if (generatedUid != -1) {
                pstmtSeller = conn.prepareStatement(insertSellerSQL);
                pstmtSeller.setInt(1, generatedUid);
                pstmtSeller.executeUpdate();
                
                conn.commit(); // Transaction success
                return true;
            } else {
                conn.rollback();
                return false;
            }
        } catch (SQLException e) {
            System.out.println("Error adding seller to DB: " + e.getMessage());
            if (conn != null) { try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); } }
            return false;
        } finally {
            DBManager.closeResources(conn, pstmtUser, rs);
            try { if (pstmtSeller != null) pstmtSeller.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
    }

    // Supports insert operations for buyers through the database using JDBC
    public boolean addBuyer(String name, String password, String streetName, String userBuildingNum, String city, String country) {
        String insertUserSQL = "INSERT INTO UsersTable (USERNAME, PASSWORD, USER_TYPE) VALUES (?, ?, 'BUYER') RETURNING UID;";
        String insertBuyerSQL = "INSERT INTO BuyersTable (UID, STREET_NAME, BUILDING_NUM, CITY, COUNTRY) VALUES (?, ?, ?, ?, ?);";
        
        Connection conn = null;
        PreparedStatement pstmtUser = null;
        PreparedStatement pstmtBuyer = null;
        ResultSet rs = null;
        
        try {
            int buildingNum = Integer.parseInt(userBuildingNum);
            conn = DBManager.getConnection();
            conn.setAutoCommit(false); // Begin Transaction
            
            pstmtUser = conn.prepareStatement(insertUserSQL);
            pstmtUser.setString(1, name);
            pstmtUser.setString(2, password);
            rs = pstmtUser.executeQuery();
            
            int generatedUid = -1;
            if (rs.next()) {
                generatedUid = rs.getInt("UID");
            }
            
            if (generatedUid != -1) {
                pstmtBuyer = conn.prepareStatement(insertBuyerSQL);
                pstmtBuyer.setInt(1, generatedUid);
                pstmtBuyer.setString(2, streetName);
                pstmtBuyer.setInt(3, buildingNum);
                pstmtBuyer.setString(4, city);
                pstmtBuyer.setString(5, country);
                pstmtBuyer.executeUpdate();
                
                conn.commit();
                return true;
            } else {
                conn.rollback();
                return false;
            }
        } catch (SQLException | NumberFormatException e) {
            System.out.println("Error adding buyer to DB: " + e.getMessage());
            if (conn != null) { try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); } }
            return false;
        } finally {
            DBManager.closeResources(conn, pstmtUser, rs);
            try { if (pstmtBuyer != null) pstmtBuyer.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
    }

    // Supports search operations to verify username availability
    public boolean checkName(String name) {
        String sql = "SELECT 1 FROM UsersTable WHERE USERNAME = ?;";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = DBManager.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, name);
            rs = pstmt.executeQuery();
            return !rs.next(); // Returns true if username does not exist
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            DBManager.closeResources(conn, pstmt, rs);
        }
    }

    public boolean wrongTypeHandling(String convertToInt) {
        try {
            Integer.parseInt(convertToInt);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}