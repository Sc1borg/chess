package dataaccess;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SchemaManager {

    public static void initializeSchema() throws DataAccessException {
        String createUsersTable = "CREATE TABLE IF NOT EXISTS users (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "name VARCHAR(50), " +
                "password VARCHAR(50), " +
                "email VARCHAR(100))";
        String createAuthTable = "CREATE TABLE IF NOT EXISTS auth (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "authToken VARCHAR(50), " +
                "username VARCHAR(50))";
        String createGameTable = "CREATE TABLE IF NOT EXISTS game (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "gameID INT, " +
                "whiteUsername VARCHAR(50)," +
                "blackUsername VARCHAR(50), " +
                "gameName VARCHAR(100), " +
                "gameState JSON)";
        try (Connection conn = DatabaseManager.getConnection()) {
            try (PreparedStatement stmt = conn.prepareStatement(createUsersTable)) {
                stmt.executeUpdate();
            }
            try (PreparedStatement stmt = conn.prepareStatement(createAuthTable)) {
                stmt.executeUpdate();
            }
            try (PreparedStatement stmt = conn.prepareStatement(createGameTable)) {
                stmt.executeUpdate();
            }
        } catch (SQLException ex) {
            throw new DataAccessException("Failed to create tables", ex);
        }
    }
}
